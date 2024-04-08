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
- Apache JMeter

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

트래픽이 가장 많이 발생 될 것으로 예상되는 여행계획 조회 API를 기준으로 부하 테스트를 진행하였습니다. <br> 
테스팅툴로 Apache JMeter 를 사용하였으며, 모든 테스트는 20명의 가상 이용자가 지속적인 조회 API를 호출하는 시나리오로 진행 하였습니다.

#### 1. 어떠한 최적화도 적용되어있지 않은 초기 상황에서의 테스트

![2](https://github.com/kt2790/tripsync_server/assets/138543028/7be6b8fe-4f62-48b2-aabc-8eedd3e4256e)
<br>
<br>
평균적으로 8초 정도의 응답시간을 소요, 현 상황에서 서비스를 배포한다면 나쁜 사용자 경험을 제공할 가능성이 클 것으로 예상됩니다.

#### 2. 쿼리 최적화를 통한 성능 개선

첫번째로 생각한 최적화 방안은 조회시 발생하는 쿼리 수를 최소화 시키는 것 입니다. <br>

![1](https://github.com/kt2790/tripsync_server/assets/138543028/5b1c9029-d7bd-4dae-b121-d46777b09f0e)
<br> <br>
위 그림은 Plan Entity 를 조회할 때, 이와 연관된 PlanGroup Entity 를 조회하는 단건 쿼리가 추가적으로 발생함을 보여주고 있습니다. <br>
만약, 조회한 Plan Entity 의 개수가 100개 라고 가정한다면 연관된 Entity 를 조회하기 위해 100개의 추가적인 단건 쿼리가 발생 할 것 입니다. <br>
흔히 잘 알려져있는 N + 1 문제에 해당하고, 이러한 문제를 해결하기 위해 fetch join, batch fetch 과 같은 솔루션을 활용 할 수 있습니다. <br>

fetch join 의 경우 하나의 Entity로 부터 To-Many 관계를 맺고있는 Entity 가 2개 이상 존재할 경우, 중복된 데이터 발생으로 인한 MultipleBagFetchException 예외가 발생합니다. 이와 같은 이유로 본 프로젝트에 적용하기엔 제한되는 부분이 있어, batch fetch 방식을 도입하여 최적화를 시도하였습니다. <br>

batch fetch 방식은 연관된 Entity 조회시 한건씩 단건 쿼리를 실행하는 것이 아니라, 사전에 조회할 개수를 설정하고 <br>
조회 발생시, 정해둔 개수만큼 SQL 에서 제공하는 IN 구문을 활용해 일괄적으로 Entity 를 조회하는 방식입니다. <br>
이러한 방법으로, 추가적으로 발생하는 N 개의 쿼리를 (N / 조회할 개수) 개의 쿼리 수로 줄일 수 있을 것 입니다. <br>

**batch fetch 방식 적용 후**
<br>
![3](https://github.com/kt2790/tripsync_server/assets/138543028/9e485d02-4c15-49a0-a0be-c20c853de694)
<br> <br>
기존 단건으로 조회하는 쿼리에서, IN 구문을 통해 일괄적으로 조회하는 쿼리로 변경되었음을 확인 할 수 있습니다.

![4](https://github.com/kt2790/tripsync_server/assets/138543028/7a009125-473a-42cd-b4dc-6e1809462e8e)
<br> <br>
기존 8000ms 의 응답 시간에서 줄어들어 200ms 약 40배 차이의 성능 개선 효과를 볼 수 있었습니다.













