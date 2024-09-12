import os

# JSON Loader
from langchain_community.document_loaders import JSONLoader
# Embedding model
from langchain_openai import OpenAIEmbeddings
from langchain_chroma import Chroma
from dotenv import load_dotenv

CUR_DIR = os.path.dirname(os.path.abspath(__file__))
JSON_DATA_PATH = os.path.join(os.path.dirname(CUR_DIR), ".." , "faq_data.json")

CHROMA_PERSIST_DIR = os.path.join(CUR_DIR, "chroma-persist")
CHROMA_COLLECTION_NAME = "qna"

# Document 객체의 metadata에 qna title 추가
def metadata_func(record: dict, metadata: dict) -> dict:
    metadata["title"] = record.get("title")

    return metadata

def load_and_store():
    try:
        # JSON 파일 로드
        loader = JSONLoader(
            file_path=JSON_DATA_PATH,
            jq_schema='.[]',
            content_key="contents",
            metadata_func=metadata_func
        )

        json_data = loader.load()

        if not json_data:
            raise Exception("JSON 데이터가 비어 있습니다.")

        # OpenAIEmbedding 을 사용하여 Chroma DB 에 벡터 데이터 적재
        return Chroma.from_documents(
            json_data,
            OpenAIEmbeddings(),
            collection_name=CHROMA_COLLECTION_NAME,
            persist_directory=CHROMA_PERSIST_DIR)
    except Exception as e:
        print(f"JSON 데이터 로드 및 적재 실패: {e}")

if __name__ == "__main__":
    load_and_store()