import sqlalchemy as db
from sqlalchemy.orm import sessionmaker, Session


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
SUBJECTS_DB_URL = "sqlite:///./server/user_subjects.db"
subjects_engine = db.create_engine(SUBJECTS_DB_URL, connect_args={"check_same_thread": False})
SubjectsSessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=subjects_engine)

subjects_metadata = db.MetaData()
user_subjects = db.Table(
    "user_subjects",
    subjects_metadata,
    db.Column("id", db.Integer, primary_key=True),
    db.Column("user_phone", db.String), 
    db.Column("subject_name", db.String),
)

metadata.create_all(bind=engine)
subjects_metadata.create_all(bind=subjects_engine)