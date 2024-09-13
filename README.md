# [2조] 토이프로젝트3 챗봇 기능 구현

# _[챗봇 PPT](https://docs.google.com/presentation/d/1ySOBfHEXxCUV4AeJFUnlCxB7Egob18_8LCzPv0B33kI/edit?usp=sharing)_

## 시연영상 링크
- 영상1 링크 : https://www.youtube.com/watch?v=drDO_L3ZQfo&t=1s
- 영상2 링크 : https://www.youtube.com/watch?v=5ZW4JTOFNbI

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

벡터BD 생성

`python /chatbot/qna/vector_db/create_vector_db.py`

## 실행방법
- DB 테이블 생성문 실행 (상품관련 테이블은 컬럼 추가 필요)
- `application.properties` 세팅
- `application.properties`에 개인정보가 있는 관계로, 해당 파일은 commit하지 않았습니다.
- slack으로 멤버에게 다이렉트 메시지 주시면, `application.properties` 파일 전달드리겠습니다.
- Shop29Application 클래스 main() 메서드 실행
=======
# KDT_DBE1_Toy_Project3

### [프로젝트 개요] 
- **프로젝트 명** : Flask를 활용한 OpenAI 챗봇 개발 및 자바 스프링 서버와의 연동
- **상세 내용** : [프로젝트 RFP 노션 링크](https://www.notion.so/Toy-Project-3-8fc76ea7c79c47e089f6abcc41b35a0c?pvs=4)
- **수행 및 결과물 제출 기한** : 9/2 (월) ~ 9/13 (금) 13:00

### [프로젝트 지도 일정]
- 1차 지도 : 2024.09.06(금) 15:00 ~ 19:00
- 2차 지도 : 2024.09.10(화) 15:00 ~ 19:00

### [프로젝트 진행 및 제출 방법]
- 본 패스트캠퍼스 Github의 Repository를 각 조별의 Github Repository로 Fork합니다.
    - 패스트캠퍼스 깃헙은 Private 형태 (Public 불가)
- 조별 레포의 최종 branch → 패스트캠퍼스 업스트림 Repository의 main branch의 **PR 상태**로 제출합니다.
    - **PR TITLE : N조 최종 제출**
    - Pull Request 링크를 LMS로도 제출해 주셔야 최종 제출 완료 됩니다. (제출자: 조별 대표자 1인)
    - LMS를 통한 과제 미제출 시 점수가 부여되지 않습니다. 

### [PR 제출 시 유의사항]
  - 프로젝트 진행 결과 및 과업 수행 내용은 README.md에 상세히 작성 부탁 드립니다. 
  - 멘토님들께서 어플리케이션 실행을 위해 확인해야 할 환경설정 값 등도 반드시 PR 부가 설명란 혹은 README.md에 작성 부탁 드립니다.
  - **Pull Request에서 제출 후 절대 병합(Merge)하지 않도록 주의하세요!**
  - 수행 및 제출 과정에서 문제가 발생한 경우, 운영진 또는 김인섭 강사님과의 소통방 통하여 강사님께 말씀해주시길 바랍니다.
