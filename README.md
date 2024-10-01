# 개발환경
- tomcat-embed-core-10.1.28
- java 21
- kotlin 2.0.0
- spring boot 3.3.3
- h2-embed-database mysql mode

# Table
- employee : 직원 테이블

# Test
[Swagger](http://localhost:8080/swagger-ui/index.html) 접속 테스트
![EMPLOYEE_TABLE](src/main/resources/API_GET_EXAMPLE.png)
#
[1.직원 정보 저장 API](http://localhost:8080/swagger-ui/index.html#/%ED%81%B4%EB%A1%9C%EB%B2%84%EC%B6%94%EC%96%BC%ED%8C%A8%EC%85%98%20%EC%A7%81%EC%9B%90%20api/addEmployees)
multiprt form-data 형식(postman 으로 호출 테스트 진행하였음.)
(201 응답)

1) csv 파일로 호출하여 저장 
key: employeeFile, value: resouces 의 employees.csv 파일
2) json 데이터 형식으로 호출하여 저장
key: employeeRequestBody, value: json 데이터
   [{
   "name": "양기열",
   "email": "ykycome00@gmail.com",
   "tel": "010-1234-5678",
   "joined": "2024-09-29"
   },
   {
   "name": "김민기",
   "email": "kimminki@naver.com",
   "tel": "010-1234-5888",
   "joined": "2024-09-30"
   }
   ]

[2.직원 정보 조회 API](http://localhost:8080/swagger-ui/index.html#/%ED%81%B4%EB%A1%9C%EB%B2%84%EC%B6%94%EC%96%BC%ED%8C%A8%EC%85%98%20%EC%A7%81%EC%9B%90%20api/getEmployees)  
default (page: 1, pageSize: 10)
페이징 관련 정보는 응답값의 meta 안에, 직원 정보는 data 안에 응답값을 내려주었음.
(200 응답)

[3.직원 상세 정보 조회 API](http://localhost:8080/swagger-ui/index.html#/%ED%81%B4%EB%A1%9C%EB%B2%84%EC%B6%94%EC%96%BC%ED%8C%A8%EC%85%98%20%EC%A7%81%EC%9B%90%20api/getEmployeeByName)  
직원 이름으로 uri query path 에 담아 호출하여 직원 정보 조회
(200 응답)

1) DDD 설계를 고려하여 ui 와 도메인 패키징 분리하였음.
2) RestAPI URI 디자인은 요구사항에 정의된 URI 를 사용하였음.
3) CQRS 패턴과 확장성을 고려하여 Query(Get), Command(Post,Put,Delete) 서비스로 분리하였음.
4) aop logging aspect 를 구현하여, api 호출시 request, response 로깅 하도록 구현하였음.
차후에는 필터 클래스를 구현하여 aop 를 적용할 것 같음.
5) 기본적으로 openapi doc 스웨거를 통해 api 테스트 및 schema 구조를 확인 가능하도록 하였음.

```
*테이블 데이터 확인
http://localhost:8080/h2-console
JDBC URL : jdbc:h2:~/test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL
User Name : sa
```
![EMPLOYEE_TABLE](src/main/resources/H2_TABLE_SELECT.png)

