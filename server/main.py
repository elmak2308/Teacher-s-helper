from fastapi import FastAPI, Depends, HTTPException, status, Request, Form
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from pydantic import BaseModel
from datetime import datetime, timedelta
from jose import JWTError, jwt
from passlib.context import CryptContext
from typing import Optional, List, Dict, Any
import sqlalchemy as db
from sqlalchemy.orm import sessionmaker, Session
import requests
from server.config import SECRET_KEY, ALGORITHM, ACCESS_TOKEN_EXPIRE_MINUTES, API_KEY
from server.Kdb import *
from server.class_and_def import *
from openai import *
app = FastAPI()
two_step_auth = TwoStepAuth()
current_phone = None
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
@app.post("/login", response_model=dict)
async def login(
    phone: str = Form(...),
    password: str = Form(...),
    db_session: Session = Depends(get_db)
):
    user = authenticate_user(db_session, phone, password)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect phone or password"
        )
    return {"auth_token": "zxc"}

@app.post("/login/step1", response_model=LoginStep1Response)
async def login_step1(
    phone: str = Form(...),
    db_session: Session = Depends(get_db)
):
    global current_phone
    user = get_user(db_session, phone)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="User not found"
        )
    current_phone = phone  
    return {"message": "Please enter your password"}

@app.post("/login/step2", response_model=LoginStep2Response)
async def login_step2(
    password: str = Form(...),
    db_session: Session = Depends(get_db)
):
    global current_phone
    
    if not current_phone:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Complete step 1 first"
        )

    user = authenticate_user(db_session, current_phone, password)
    if not user:
        current_phone = None 
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect password"
        )
    current_phone = None
    return {
        "auth_token": "zxc"
    }

@app.get("/users/me", response_model=User)
async def read_users_me(
    auth_token: str,
    db_session: Session = Depends(get_db)
):
    if auth_token != "zxc":
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid authentication"
        )
    query = db.select(users)
    user = db_session.execute(query).fetchone()
    if not user:
        raise HTTPException(status_code=404, detail="No users found")
    
    return {
        "phone": user.phone,
        "email": user.email,
        "full_name": user.full_name
    }

@app.post("/subjects/add")
def add_subject_for_user(
    subject_in: UserSubjectCreate,
    auth_token: str,
    db_session_users: Session = Depends(get_db)):

    if auth_token != "zxc":
        raise HTTPException(status_code=401, detail="Invalid authentication")

    phone = "1234567890"

    with SubjectsSessionLocal() as sdb:
        query_check = db.select(user_subjects).where( 
            (user_subjects.c.user_phone == phone) & 
            (user_subjects.c.subject_name == subject_in.subject_name)
        )
        result = sdb.execute(query_check).fetchone()
        
        if result:
            raise HTTPException(status_code=400, detail="Subject already added for this user")
        
        insert_query = db.insert(user_subjects).values(
            user_phone=phone,
            subject_name=subject_in.subject_name
        )
        sdb.execute(insert_query)
        sdb.commit()
        
    return {"message": "Subject added successfully"}

@app.delete("/subjects/delete/{subject_name}")
def delete_subject(
    subject_name: str,
    auth_token: str,
    db_session_users: Session = Depends(get_db)
):
    if auth_token != "zxc":
        raise HTTPException(status_code=401, detail="Invalid authentication")

    # For demo purposes, just use a dummy phone
    phone = "1234567890"

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
def get_user_subjects(auth_token: str):
    if auth_token != "zxc":
        raise HTTPException(status_code=401, detail="Invalid authentication")

    return [
        {"id": 1, "name": "Math"},
        {"id": 2, "name": "Physics"}
    ]


@app.post("/chat", response_model=ChatResponse)
async def chat_with_ai(
    chat_request: ChatRequest,
    auth_token: str,
    db_session: Session = Depends(get_db)
):
    if auth_token != "zxc":
        raise HTTPException(status_code=401, detail="Invalid authentication")
    input_data = {
        "is_sync": chat_request.is_sync,
        "messages": [
            {
                "role": "user",
                "content": chat_request.message
            }
        ]
    }
    headers = {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': f'Bearer {API_KEY}'
    }
    url_endpoint = "https://api.gen-api.ru/api/v1/networks/deepseek-v3"
    try:
        if 'response' in ai_response and len(ai_response['response']) > 0:
            first_response = ai_response['response'][0]
            if 'choices' in first_response and len(first_response['choices']) > 0:
                message = first_response['choices'][0].get('message', {})
                if 'content' in message:
                    return {"response": message['content']}
            return {"response": "Не удалось обработать ответ от AI"}            
    except requests.exceptions.RequestException as e:
        raise HTTPException(
            status_code=502,
            detail=f"Ошибка при обращении к AI сервису: {str(e)}"
            )