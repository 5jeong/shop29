# 29CM 쇼핑몰 벤치마킹
## 멤버구성 및 역할
- 김병훈 : 장바구니, 결제, 주문
- 김정민 : 1:1문의, 첨부파일
- 류은기 : 상품 분류, 상품 옵션
- 오정훈 : 회원가입, 회원수정, 로그인, 계정찾기
- 임정훈 : 공지사항, FAQ

## 개발환경
- Web BackEnd : Java 17, Spring Boot 3.3.2, Mybatis, MySql 8.0이상, Gradle
- Web FrontEnd : thymeleaf , HTML/CSS/JS
- Versioning : Git, GitHub, Notion

## 실행방법
- DB 테이블 생성문 실행 (상품관련 테이블은 컬럼 추가 필요)
- `application.properties` 세팅
- `application.properties`에 개인정보가 있는 관계로, 해 당파일은 commit하지 않았습니다.
- slack으로 멤버에게 다이렉트 메시지 주시면, `application.properties` 파일 전달드리겠습니다.
- Shop29Application 클래스 main() 메서드 실행
