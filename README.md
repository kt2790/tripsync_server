# tripsync_server

## 1. 프로젝트 소개

- 여행 계획 작성 및 공유 서비스를 제공하는 TripSync 앱의 백엔드 프로젝트 입니다. <br>
- [TripSync 안드로이드 프로젝트 github](https://github.com/NBCAndroid15/TripSync)

## 2. 사용 기술

- Spring Boot 3.3.2
- Spring Web MVC
- Spring Cache
- Spring REST Docs
- Spring Data JPA
- Query DSL
- Junit5
- Mockito

## 3. ERD

<img src="https://github.com/kt2790/tripsync_server/assets/138543028/81d23fd9-9c43-4ac2-b513-012f6f299256" width="600" height="400" />

## 4. API 문서

### **예시)**<br>
<img src="https://github.com/kt2790/tripsync_server/assets/138543028/f93220ca-d0fe-42c9-9643-8cd68fffd0d0" width="700" height="700" />

[상세 문서](https://kt2790.github.io/tripsync_api/)

## 5. 테스트

지속 가능한 어플리케이션을 위해 프로젝트의 모든 단위 모듈들에 대한 테스트 코드를 작성하였습니다. <br> <br>
![image](https://github.com/kt2790/tripsync_server/assets/138543028/d8307589-c863-4c06-8cff-7bb866bba2aa)

## 6. 성능 개선

트래픽이 가장 많이 발생 될 것으로 예상되는 여행계획 조회 API를 기준으로 부하 테스트를 진행하였습니다.
테스팅툴로 Apache JMeter 를 사용하였으며, 모든 테스트는 20명의 가상 이용자가 지속적인 조회 API를 호출하는
시나리오로 진행 하였습니다.

### 1. 최적화를 진행하지않은 초기상태에서의 테스트



