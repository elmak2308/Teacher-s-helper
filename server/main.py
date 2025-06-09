from fastapi import FastAPI, Depends, HTTPException, status, Request, Form
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from pydantic import BaseModel
from datetime import datetime, timedelta
from jose import JWTError, jwt
from passlib.context import CryptContext
from typing import Optional
import sqlalchemy as db
from sqlalchemy.orm import sessionmaker, Session
from typing import List
from server.config import SECRET_KEY, ALGORITHM, ACCESS_TOKEN_EXPIRE_MINUTES
from server.Kdb import *
from server.class_and_def import *

app = FastAPI()
two_step_auth = TwoStepAuth()
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

@app.post("/signup", response_model=User)
def signup(user: UserCreate, db_session: Session = Depends(get_db)):
    existing_user_phone = get_user(db_session, user.phone)
    existing_user_email = db_session.execute(
    db.select(users).where(users.c.email == user.email) 
    ).fetchone()
    
    if existing_user_phone:
        raise HTTPException(status_code=400, detail="Phone number already registered")
    if existing_user_email:
        raise HTTPException(status_code=400, detail="Email already registered")

    hashed_password = get_password_hash(user.password)
    
    query = users.insert().values(
        phone=user.phone,
        email=user.email,
        full_name=user.full_name,
        hashed_password=hashed_password,
        disabled=False
    )
    db_session.execute(query)
    db_session.commit()
    
    return {"phone": user.phone, "email": user.email, "full_name": user.full_name}

@app.post("/token/init", response_model=dict)
def init_login(
    request: Request,
    phone: str = Form(...),
    db_session: Session = Depends(get_db)
):
    user = get_user(db_session, phone)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="User with this phone number not found"
        )
    temp_token = create_access_token(
        data={"sub": phone, "step": "password_required"},
        expires_delta=timedelta(minutes=5)
    )
    two_step_auth.pending_auth[temp_token] = {
        "phone": phone,
        "user_agent": request.headers.get("user-agent"),
        "timestamp": datetime.utcnow()
    }
    return {
        "message": "Password required",
        "temp_token": temp_token,
        "next_step": "/token/complete"
    }

@app.post("/token/complete", response_model=Token)
def complete_login(
    password: str = Form(...),
    temp_token: str = Form(...),
    db_session: Session = Depends(get_db)
):
    try:
        payload = jwt.decode(temp_token, SECRET_KEY, algorithms=[ALGORITHM])
        if payload.get("step") != "password_required":
            raise JWTError("Invalid token step")
        phone = payload.get("sub")
        if not phone:
            raise JWTError("No phone in token")
        if temp_token not in two_step_auth.pending_auth:
            raise JWTError("Token not found in pending auth")
        
    except JWTError as e:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid or expired temporary token"
        )
    user = authenticate_user(db_session, phone, password)
    if not user:
        if temp_token in two_step_auth.pending_auth:
            del two_step_auth.pending_auth[temp_token]  
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect password",
            headers={"WWW-Authenticate": "Bearer"},
        )
    if user.get("disabled", False):
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Inactive user"
        )
    if temp_token in two_step_auth.pending_auth:
        del two_step_auth.pending_auth[temp_token]
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={"sub": user["phone"]}, expires_delta=access_token_expires
    ) 
    return {"access_token": access_token, "token_type": "bearer"}

@app.get("/users/me", response_model=User)
async def read_users_me(  
    token: str = Depends(oauth2_scheme),
    db_session: Session = Depends(get_db)
):
    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Could not validate credentials",
        headers={"WWW-Authenticate": "Bearer"},
    )
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        phone: str = payload.get("sub")
        if phone is None:
            raise credentials_exception
        token_data = TokenData(phone=phone)
    except JWTError:
        raise credentials_exception
    
    user = get_user(db_session, phone=token_data.phone)
    if user is None:
        raise credentials_exception
    if user.get("disabled", False):
        raise HTTPException(status_code=400, detail="Inactive user")
    
    return user

@app.post("/subjects/add")
def add_subject_for_user(
    subject_in: UserSubjectCreate,
    token:str=Depends(oauth2_scheme),
    db_session_users : Session=Depends(get_db)):

    payload=jwt.decode(token ,SECRET_KEY , algorithms=[ALGORITHM])
    phone=payload.get("sub")
    if not phone:
        raise HTTPException(status_code=401 ,detail="Invalid authentication")

    with SubjectsSessionLocal() as sdb:
        query_check = db.select(user_subjects).where( 
(user_subjects.c.user_phone == phone) & 
(user_subjects.c.subject_name == subject_in.subject_name)
)
        result=sdb.execute(query_check).fetchone()
        
        if result:
            raise HTTPException(status_code=400 ,detail="Subject already added for this user")
        
        insert_query=db.insert(user_subjects).values(user_phone=phone ,subject_name=subject_in.subject_name)
        sdb.execute(insert_query)
        sdb.commit()
        
    return {"message":"Subject added successfully"}

@app.delete("/subjects/delete/{subject_name}")
def delete_subject(
    subject_name: str,
    token: str = Depends(oauth2_scheme),
    db_session_users: Session = Depends(get_db)
):
    payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
    phone = payload.get("sub")
    if not phone:
        raise HTTPException(status_code=401, detail="Invalid authentication")

    with SubjectsSessionLocal() as sdb:
        query_check = db.select(user_subjects).where(
            (user_subjects.c.user_phone == phone) &
            (user_subjects.c.subject_name == subject_name)
        )
        result = sdb.execute(query_check).fetchone()  
        if not result:
            raise HTTPException(
                status_code=404,
                detail="Subject not found or you don't have permission to delete it"
            )
        delete_query = db.delete(user_subjects).where(
            (user_subjects.c.user_phone == phone) &
            (user_subjects.c.subject_name == subject_name)
        )
        sdb.execute(delete_query)
        sdb.commit()
        
    return {"message": "Subject deleted successfully"}

@app.get("/subjects/", response_model=List[Subject])
def get_user_subjects(token:str=Depends(oauth2_scheme)):
    payload=jwt.decode(token ,SECRET_KEY , algorithms=[ALGORITHM])
    phone=payload.get("sub")
    if not phone:
        raise HTTPException(status_code=401 ,detail="Invalid authentication")
    with SubjectsSessionLocal() as sdb:
        query = db.select(user_subjects).where(user_subjects.c.user_phone == phone)
        results=sdb.execute(query).fetchall()
        
        subjects_list=[]
        for row in results:
            subjects_list.append({"id":row.id,"name":row.subject_name})
            
    return subjects_list