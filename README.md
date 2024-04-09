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

테스트 기반으로 API 문서 생성을 자동화 해주는 Spring REST Docs 기술을 활용하였습니다. 

**예시)**
<br>
<img src="https://github.com/kt2790/tripsync_server/assets/138543028/f93220ca-d0fe-42c9-9643-8cd68fffd0d0" width="700" height="700" />

[상세 문서](https://kt2790.github.io/tripsync_api/)

## 5. 테스트

지속 가능한 어플리케이션을 위해 프로젝트의 모든 단위 모듈들에 대한 테스트 코드를 작성하였습니다. <br> <br>
![image](https://github.com/kt2790/tripsync_server/assets/138543028/d8307589-c863-4c06-8cff-7bb866bba2aa)

## 6. 성능 개선

트래픽이 가장 많이 발생 될 것으로 예상되는 여행계획 조회 API를 기준으로 부하 테스트를 진행하였습니다. <br> 
테스팅툴로 Apache JMeter 를 사용하였으며, 모든 테스트는 20명의 가상 이용자가 지속적으로 조회 API를 호출하는 시나리오로 진행 하였습니다.

#### 1. 어떠한 최적화도 적용되어있지 않은 초기 상황에서의 테스트

***응답 시간 그래프***
<br>
![2](https://github.com/kt2790/tripsync_server/assets/138543028/7be6b8fe-4f62-48b2-aabc-8eedd3e4256e)
<br>
<br>
평균적으로 8초 정도의 응답시간을 소요, 현 상황에서 서비스를 배포한다면 좋지 않은 사용자 경험을 제공할 가능성이 높을 것 입니다.

#### 2. 쿼리 최적화를 통한 성능 개선

첫번째로 생각한 최적화 방안은 조회시 발생하는 쿼리 수를 최소화 시키는 것 입니다.

***기존 쿼리***
<br>
![1](https://github.com/kt2790/tripsync_server/assets/138543028/5b1c9029-d7bd-4dae-b121-d46777b09f0e)
<br>
위 그림은 Plan Entity 를 조회할 때, 이와 연관된 PlanGroup Entity 를 조회하는 단건 쿼리가 추가적으로 발생함을 보여주고 있습니다. <br>
만약, 조회한 Plan Entity 의 개수가 100개 라고 가정한다면 연관된 Entity 를 조회하기 위해 100개의 추가적인 단건 쿼리가 발생 할 것 입니다. <br>
흔히 잘 알려져있는 N + 1 문제에 해당하고, 이러한 문제를 해결하기 위해 fetch join, batch fetch 과 같은 솔루션을 활용 할 수 있습니다. <br>

fetch join 의 경우 To-Many 관계를 맺고있는 Entity 가 2개 이상인 Entity 에 적용할 경우, 중복된 데이터 발생으로 인한 MultipleBagFetchException 예외가 발생합니다. 이와 같은 이유로 본 프로젝트에 적용하기엔 제한되는 부분이 있어, batch fetching 방식을 도입하여 최적화를 시도하였습니다.

batch fetching 방식은 연관된 Entity 조회시 한건씩 단건 쿼리를 실행하는 것이 아니라, 사전에 조회할 개수를 설정하고 <br>
조회 시, 정해둔 개수만큼 SQL 에서 제공하는 IN 구문을 활용해 일괄적으로 Entity 를 조회하는 방식입니다. <br>
이러한 방법으로, 추가적으로 발생하는 N 개의 쿼리를 (N / 조회할 개수) 개의 쿼리 수 만큼 줄일 수 있을 것 입니다.

***batch fetch 방식 적용 후***
<br>
![3](https://github.com/kt2790/tripsync_server/assets/138543028/9e485d02-4c15-49a0-a0be-c20c853de694)
<br> <br>
기존 단건으로 조회하는 쿼리에서, IN 구문을 통해 일괄적으로 조회하는 쿼리로 변경되었음을 확인 할 수 있습니다.

***응답 시간 그래프***
<br>
![4](https://github.com/kt2790/tripsync_server/assets/138543028/7a009125-473a-42cd-b4dc-6e1809462e8e)
<br> <br>
기존 8000ms 의 응답 시간에서 줄어들어 200ms 약 40배 차이의 성능 개선 효과를 볼 수 있었습니다.

#### 3. in-memory cache 기반 성능 개선

batch fetching 방식을 통해 쿼리 수를 크게 줄였지만, 사용자가 매번 조회 요청을 할 때 마다 큰 비용을 소모하는 DB로의 접근은 여전히 발생하고 있었습니다.
해당 문제점을 해결하기 위해, 일반적으로 많이 사용하는 방법은 캐싱 메커니즘을 활용 하는 것 입니다.

사용자가 조회 요청을 할때마다, 최초 요청에 대한 반환값을 디스크 보다 상대적으로 접근속도가 빠른 메모리에 저장하고
이후 똑같은 요청이 온다면, 메모리에 저장해놓은 값을 가져와 바로 반환 해주는 방식으로 구현하면 될 것 입니다.

스프링 프레임워크의 경우 캐싱 기능을 구현하기 위한 다양한 인터페이스와 구현체를 이미 제공해주고 있습니다. <br>
본 프로젝트 에서는 가장 간단한 캐시매니저 구현체인 ConcurrentMapCacheManager 를 활용해서 테스트를 진행하였습니다.

***캐시 기능 적용***
<br>
![1](https://github.com/kt2790/tripsync_server/assets/138543028/01d2c59a-5dab-45c8-b66e-af92537d9297)
<br>
사용자가 작성한 계획 목록을 조회하는 getPlanByUid 메소드에 @Cacheable 어노테이션을 적용하여 캐시 기능을 활성화 및
사용자의 uid 를 key, 반환되는 List<PlanDTO> 를 value 로 하여 ConcurrentHashMap 에 저장되는 형태로 구현하였습니다.

***응답 시간 그래프***
<br>
![2](https://github.com/kt2790/tripsync_server/assets/138543028/6095129a-a7e3-414e-8a62-8f7f285261b3)
<br>
이전 평균 응답시간인 200ms 에서 5ms 의 응답시간으로 40배 정도의 성능 향상을 확인할 수 있었습니다.



















