# [2조] 토이프로젝트3 챗봇 기능 구현

# _[챗봇 PPT](https://docs.google.com/presentation/d/1ySOBfHEXxCUV4AeJFUnlCxB7Egob18_8LCzPv0B33kI/edit?usp=sharing)_

## 개발환경
- Web BackEnd : Java 17, Spring Boot 3.3.2, Mybatis, MySql 8.0이상, Gradle
- Web FrontEnd : thymeleaf , HTML/CSS/JS
- Versioning : Git, GitHub, Notion

**[챗봇 관련 설정]**

/chatbot 폴더에 .env파일 생성하여 아래 값 수정
```
OPENAI_API_KEY = "OPENAI API 키"
OPENAI_MODEL_NAME = "OPENAI 모델명"
DATABASE_URL = "localhost:3306/DB명?charset=utf8"
DATABASE_USERNAME = "DB아이디"
DATABASE_PASSWORD = "DB비밀번호"
```

터미널 아래 코드 실행

`pip install -r requirements.txt`

Chroma 벡터DB 생성

`python /chatbot/qna/vector_db/create_vector_db.py`

## 실행방법
- DB 테이블 생성문 실행 (상품관련 테이블은 컬럼 추가 필요)
- `application.properties` 세팅
- `application.properties`에 개인정보가 있는 관계로, 해당 파일은 commit하지 않았습니다.
- slack으로 멤버에게 다이렉트 메시지 주시면, `application.properties` 파일 전달드리겠습니다.
- Shop29Application 클래스 main() 메서드 실행
