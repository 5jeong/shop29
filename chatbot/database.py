from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
import os
from dotenv import load_dotenv

load_dotenv()
url = os.getenv("DATABASE_URL")
username = os.getenv("DATABASE_USERNAME")
password = os.getenv("DATABASE_PASSWORD")


DATABASE_URL = f'mysql+pymysql://{username}:{password}@{url}'
engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

def get_db():
    db = SessionLocal()
    try:
        yield db
    except:
        db.close()
        