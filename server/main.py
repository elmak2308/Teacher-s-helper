from fastapi import FastAPI, Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from pydantic import BaseModel
from datetime import datetime, timedelta
from jose import JWTError, jwt
from passlib.context import CryptContext
from typing import Optional
import sqlalchemy as db
from sqlalchemy.orm import sessionmaker, Session
from server.config import SECRET_KEY, ALGORITHM, ACCESS_TOKEN_EXPIRE_MINUTES
from typing import List

SQLALCHEMY_DATABASE_URL = "sqlite:///./server/test.db"
engine = db.create_engine(SQLALCHEMY_DATABASE_URL, connect_args={"check_same_thread": False}) 
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

metadata = db.MetaData()
users = db.Table(
    "users",
    metadata,
    db.Column("id", db.Integer, primary_key=True, index=True),
    db.Column("phone", db.String, unique=True, index=True),
    db.Column("email", db.String, unique=True, index=True),
    db.Column("full_name", db.String),
    db.Column("hashed_password", db.String),
    db.Column("disabled", db.Boolean, default=False), 
)
'''
subjects = db.Table(
    "subjects",
    metadata,
    db.Column("id", db.Integer, primary_key=True, index=True),
    db.Column("name", db.String, unique=True),
)
'''
metadata.create_all(bind=engine)

app = FastAPI()

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

class UserCreate(BaseModel):
    phone: str
    email: str
    full_name: str
    password: str

class UserInDB(BaseModel): 
    phone: str
    email: str
    full_name: str
    hashed_password: str
    disabled: Optional[bool] = None

class User(BaseModel):
    phone: str
    email: str
    full_name: str

class Token(BaseModel):
    access_token: str
    token_type: str

class TokenData(BaseModel):
    phone: Optional[str] = None

class Subject(BaseModel):
    id: int
    name: str

def verify_password(plain_password: str, hashed_password: str):
    return pwd_context.verify(plain_password, hashed_password)

def get_password_hash(password: str):
    return pwd_context.hash(password)

def get_user(db_session: Session, phone: str):
    query = db.select([users]).where(users.c.phone == phone)
    result = db_session.execute(query).fetchone()
    if result:
        return dict(result)

def authenticate_user(db_session: Session, phone: str, password: str):
    user = get_user(db_session, phone)
    if not user:
        return False
    if not verify_password(password, user["hashed_password"]):
        return False
    return user

def create_access_token(data: dict, expires_delta: Optional[timedelta] = None):
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.utcnow() + expires_delta
    else:
        expire = datetime.utcnow() + timedelta(minutes=15)
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt

def get_db():
    db_session = SessionLocal()
    try:
        yield db_session
    finally:
        db_session.close()

@app.post("/signup", response_model=User)
def signup(user: UserCreate, db_session: Session = Depends(get_db)):
    existing_user_phone = get_user(db_session, user.phone)
    existing_user_email = db_session.execute(
        db.select([users]).where(users.c.email == user.email)
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

@app.post("/token", response_model=Token)
def login_for_access_token(
    form_data: OAuth2PasswordRequestForm = Depends(),
    db_session: Session = Depends(get_db)
):
    user = authenticate_user(db_session, form_data.username, form_data.password)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect phone number or password",
            headers={"WWW-Authenticate": "Bearer"},
        )
    if user.get("disabled", False):
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Inactive user"
        )
    
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
@app.get("/subjects/", response_model=List[Subject])
def get_subjects():
    subjects = [
        {"id": 1, "name": "Математика"},
        {"id": 2, "name": "Физика"},
        {"id": 3, "name": "Химия"},
        {"id": 4, "name": "Биология"},
        {"id": 5, "name": "История"},
        {"id": 6, "name": "Литература"},
        {"id": 7, "name": "Информатика"},
        {"id": 8, "name": "География"},
        {"id": 9, "name": "Обществознание"},
        {"id": 10, "name": "Английский язык"}
    ]
    return subjects
'''
@app.get("/protected/subjects/", response_model=List[Subject])
def get_protected_subjects(
    token: str = Depends(oauth2_scheme),
    db_session: Session = Depends(get_db)
):
    current_user = await read_users_me(token, db_session)
    
    query = db.select([subjects])
    result = db_session.execute(query).fetchall()
    return [dict(row) for row in result]
'''