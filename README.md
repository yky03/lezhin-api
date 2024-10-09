# 개발 환경
- Tomcat Embed Core: 10.1.28
- Java: 21
- Kotlin: 2.0.0
- Spring Boot: 3.3.3
- H2 Embed Database (MySQL Mode)

# 테이블
- comic: 작품 테이블
- buy: 구매 내역 테이블
- member: 회원 테이블
- ViewHistoy: 조회 이력 테이블

# API 테스트
[Swagger](http://localhost:8080/swagger-ui/index.html) 접속 테스트

![lezhin_swagger](src/main/resources/lezhin_swagger.png)

1) DDD 설계를 고려하여 ui 와 도메인 패키징 분리하였음.
2) RestAPI URI 디자인을 고려하여 설계.
3) LoggingFilter(request, response 로깅 하도록 구현하였음), AdultCheckFilter(성인인증 필터), AuthInterceptor(컨트롤러 유입 전 인증키 인터셉터)
차후에는 필터 클래스를 구현하여 aop 를 적용할 것 같음.
4) 기본적으로 openapi doc 스웨거를 통해 api 테스트 및 schema 구조를 확인 가능하도록 하였음.
5) 페이징 처리를 추가하여 데이터 양이 많아졌을때도 문제 없도록 구현.
6) 데모 시나리오 특성상 SampleDataInitializer 를 구현하여 스프링부트 실행시 member(회원), comic(작품) 기초 데이터 삽입.
회원 1번, 작품 1번 요청값으로 스웨거에서 대부분의 테스트가 가능하며, 스웨거 자물쇠를 클릭 후 고정키를 헤더에 담아야 정상 호출 가능.
key: 7725010aed35e854d819a3de7cac78aae6f054d14d9f95b3b9f5075ec956c77a 
AuthInterceptor 에 하드코딩 되어 있으며 yml, 시크릿매니저, ddb 에서 키관리 검토. 보안성 강화를 위해 차후에는 jwt 토큰 검증 방식으로 교체.

```
*테이블 데이터 확인
http://localhost:8080/h2-console
JDBC URL : jdbc:h2:~/test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL
User Name : sa
```