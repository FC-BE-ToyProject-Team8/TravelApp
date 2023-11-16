# TravelApp

## 📍 소개

***
**프로젝트 내용**: 여행 여정을 기록, 관리하는 SNS 서비스

**프로젝트 주제 및 필수 구현 기능 제안**: 야놀자

**프로젝트 목적**: Spring Security, JWT, JUnit, OpenApi 활용

**프로젝트 기간**: 2023년 11월 10일 (월) ~ 11월 16일(목)

### 기술 스택

- **언어**: Java 17
- **개발 환경**: IntelliJ, Gradle, Spring Boot
- **라이브러리**: Spring web, Spring Security, JPA, JUnit5, Lombok, MapStruct
- **CI**: GitHub Actions
- **API 명세** : Swagger

### API 명세
![api명세1.png](resultimage/api명세1.png)
![api명세2.png](resultimage/api명세2.png)

### 로컬 데이터베이스 H2 접속 경로

1. http://localhost:8080/h2-console 에 들어간다.
2. 아래 정보대로 입력 칸을 채우고 Connect를 누른다.

- Driver Class: org.h2.Driver
- JDBC URL: jdbc:h2:mem:test;MODE=MYSQL
- User Name: sa
- Password: (빈칸)

## ⚖️ [컨벤션](https://github.com/FC-BE-ToyProject-Team8/TravelApp/wiki)

----

### 1. 코딩 컨벤션

- 커스텀 구글 코딩 컨벤션을 사용합니다.

### 2. API 설계

- WIKI :  https://github.com/FC-BE-ToyProject-Team8/TravelApp/wiki/API-%EC%84%A4%EA%B3%84
- Swagger : http://localhost:8080/swagger-ui/index.html

### 3. Git Flow

- 브랜치 전략으로 Git Flow를 사용합니다
- 다만, 프로젝트의 규모상 버전을 관리하는 release, 버그를 수정하는 hotfix 브랜치는 따로 생성하지 않았습니다.
- 모든 팀원의 Approve를 받아야 main에 Merge 할 수 있습니다.

### <브랜치별 역할>

### `feature/#`

- 실제 작업을 하는 브랜치
- 이슈 번호가 1이라면 feature/1로 만들면 된다.
- 'develop'을 베이스 브랜치로 하여 만들어야 한다.
    - ( 브랜치 생성은 베이스 브랜치[ 체크아웃되어있는 브랜치 ]를 기준으로 만들어진다.)
- 작업이 완료되면 develop으로 Pull Request를 날린다.
- 4명의 Approve를 받았다면 Merge한다.

### `develop`

- 다음 버전 개발을 위해 main으로 가기 전 기능 코드들을 모아두는 브랜치
- 작성한 기능이 잘 작동되는지 확인하고, main으로 PR 및 Merge를 하면 된다.
- main으로 Pull Request를 날릴때는 Approve 받는 것은 선택사항이다.

### `main`

- 실제 서비스를 운영할 수 있는 브랜치
- main에 배포가 되고 나서도 기능이 잘 작동하는지 확인해야 한다.

### 4. 테스트 코드

- **Repository**: `@Query` 어노테이션 등을 사용한 메서드에 대해 테스트 코드 작성 필수
- **Service**: 단순 Repository 메서드 호출 이상의 로직을 수행하는 메서드에 대해 테스트 코드 작성 필수
- **Controller**: 모든 public 메서드 테스트 코드 작성 필수

### 5. 성공/오류 코드 번호

- **200** : 모든 성공
- **400** : 클라이언트가 입력을 잘못한 경우
- **401** : 인증 정보가 없는 경우
- **403** : 접근 권한이 없는 경우
- **500** : 서버 내부 에러

### 6. 기타 합의사항

- **협업 관련**
    - 데일리 스크럼: 매일 오전 10:00 Slack에 진행상황 공유
      <br><br>
- **커밋 메시지 관련**
    - 커밋 제목은 `prefix: 커밋 메시지` 형태로 합니다.
    - prefix의 목록과 각각의 용도는 IntelliJ 플러그인에서 볼 수 있습니다.
    - IntelliJ 플러그인 적용법: https://blog.naver.com/rinjyu/222157082860
    - 커밋 내용을 자세하게 적습니다. (커밋 제목과 내용만 보더라도 코드 내용이 파악 가능할 정도로)

## 🗺️ 기획안

---

## ERD

![img.png](resultimage/ERD.JPG)
- `create_at`/`update_at` 컬럼은 자동으로 값을 넣어주기 위해`JPA Auditing` 기능을 사용하였습니다.

## 초기 실행 화면

![img.png](resultimage/스프링부트초기실행화면.png)

## API 테스트
### `POST /signup`
회원가입 (요청/응답 - 성공)

![회원가입.png](resultimage/member/회원가입.png)
![회원가입db.JPG](resultimage/member/회원가입db.JPG)

회원가입 (요청/응답 - 실패 *이미 존재하는 닉네임*)
![중복닉네임.png](resultimage/member/중복닉네임.png)

회원가입 (요청/응답 - 실패 *이미 존재하는 이메일*)
![중복닉네임.png](resultimage/member/중복이메일.png)

### `POST /login`
로그인 (요청/응답 - 성공)

![로그인.png](resultimage/member/로그인.png)

로그인 (요청/응답 - 실패 *잘못된 비밀번호*)
![잘못된비밀번호.png](resultimage/member/잘못된비밀번호.png)
### `POST /trips` 
여행 등록 (요청/응답)

![여행등록.png](resultimage/trip/여행등록.png)
![여행등록db.JPG](resultimage/trip/여행등록db.JPG)

### `POST /itineraries` 
여정 복수 추가 (요청/응답)

![여정등록요청.png](resultimage/itinerary/여정등록요청.png)
![여정등록응답.png](resultimage/itinerary/여정등록응답.png)
![여정등록db.JPG](resultimage/itinerary/여정등록db.JPG)

### `PUT /trips/{tripId}` 
여행 수정 (요청/응답)'

![여행수정.png](resultimage/trip/여행수정.png)
![여행수정db.png](resultimage/trip/여행수정db.png)
### `PUT /itineraries/{itineraryId}` 
여정 수정 (요청/응답)

![여정수정요청.png](resultimage/itinerary/여정수정요청.png)
![여정수정응답.png](resultimage/itinerary/여정수정응답.png)
여정 수정 (요청/응답)

### `GET /trips` 
여행 목록 조회 (요청/응답)

![여행목록.png](resultimage/trip/여행목록.png)

### `GET /trips/{tripid}` 
여행+여정 조회 (요청/응답)

![여행조회.png](resultimage/trip/여행조회.png)

### `GET /trips/my-likes?page={page}`
본인이 좋아요 누른 여행 조회 (요청/응답)
![좋아요여행리스트.png](resultimage/trip/좋아요여행리스트.png)

### `DELETE /trips/{tripId}` 
여행 삭제 (요청/응답)

![여행삭제이전db.png](resultimage/trip/여행삭제이전db.png)
![여행삭제.png](resultimage/trip/여행삭제.png)
![여행삭제이후db.png](resultimage/trip/여행삭제이후db.png)

### `DELETE /itineraries/{itineraryId}` 
여정 삭제 (요청/응답)

![여정삭제이전db.JPG](resultimage/itinerary/여정삭제이전db.JPG)
![여정삭제.png](resultimage/itinerary/여정삭제.png)
![여정삭제이후db.JPG](resultimage/itinerary/여정삭제이후db.JPG)

### `POST /likes?tripId={tripId}`
좋아요 등록 (요청/응답)

![좋아요등록.png](resultimage/like/좋아요등록.png)
![좋아요결과.png](resultimage/like/좋아요결과.png)

### `DELETE /likes?tripId={tripId}`
좋아요 삭제 (요청/응답)

![좋아요삭제.png](resultimage/like/좋아요삭제.png)
![좋아요삭제결과.png](resultimage/search/좋아요삭제결과.png)

### `POST /comments?tripId={tripId}`
댓글 등록 (요청/응답)

![댓글 작성.png](resultimage/comment/댓글작성.png)

### `PUT /comments/{commentId}`
댓글 수정 (요청/응답)

![댓글 수정.png](resultimage/comment/댓글수정.png)

### `DELETE /comments/{commentId}` 
댓글 삭제 (요청/응답)

![댓글삭제.png](resultimage/comment/댓글삭제.png)

### `GET /trips/search-by-nickname?query={query}&page={page}` 
사용자 닉네임으로 여행 검색 (요청/응답)

![닉네임찾기요청.png](resultimage/search/닉네임찾기요청.png)
![닉네임찾기응답.png](resultimage/search/닉네임찾기응답.png)

### `GET /trips/search-by-trip-name?query={query}&page={page}` 
여행 이름으로 여행 검색 (요청/응답)

![여행이름검색1.png](resultimage/trip/여행이름검색1.png)
![여행이름검색2.png](resultimage/trip/여행이름검색2.png)

### `GET /search-place?query={query}&page={page}&size={size}` 
카카오 OpenApi를 이용한 키워드를 통한 장소 검색 (요청/응답)

![카카오장소검색.png](resultimage/search/카카오장소검색.png)
----

## 🖥 평가항목 별 커멘트

---

| 구분       | 내용                                                                                                                                                                                                                |
|----------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| API 설계   | REST API URL 설계 규칙에 어긋나지 않게 URL이 잘 구현되었는가.                                                                                                                                                                        |
|          | -> 네, 리소스에 대한 표현은 현업 컨벤션과 맞게 복수형 명사로 구성하였고, 추가 사항으로 프론트 구현도 생각에 두어 모든 API앞에 `/api`를 붙여주었습니다.                                                                                                                      |
|          | 적절한 HTTP method를 사용했는가                                                                                                                                                                                            |
|          | -> 데이터 조회 시 `GET`, 추가 시 `POST`, 삭제 시 `DELETE`를 사용했고, 업데이트 시 `PATCH`가 아닌 `PUT`을 사용하여 클라이언트로부터 모든 필드의 정보를 받아 기존 리소스를 완전히 대체하는 방식으로 구현하였습니다.                                                                         |
| ERD 설계   | 각 테이블의 모든 컬럼들이 적절한 자료형을 사용했는가                                                                                                                                                                                     |
|          | ->  자바 자료형에 맞는 MySQL자료형을 사용하였습니다.                                                                                                                                                                                 |
|          | 테이블 간 연관관계를 적절히 설정하였는가                                                                                                                                                                                            |
|          | -> 테이블을 여행, 여정 총 2가지로 두었고, 여행을 여정이 1:n으로 참조하는 방식을 사용하였습니다. 여기서 route, lodeg, stay 테이블을 따로 만들지 않고 하나의 여정에 담은 이유는 여정 테이블에서 모든 컬럼을 생성해 만들어도 컬럼의 수가 충분히 많지 않고 개발 부분에서도 편의성을 얻을 수 있다 생각하여 여정 테이블 하나에 모든 데이터를 담게 되었습니다. |
| 프로젝트 구조  | Controller - Service - Repository를 각 계층에 맞게 사용했는가                                                                                                                                                                 |
|          | -> Controller는 사용자의 요청을 받아 Service를 호출하고, Service의 응답값을 Response DTO로 변환하여 반환해주는 역할, Service는 Repository 호출 및 트랜잭션 등 비즈니스 로직을 수행하는 역할, Repository는 DB를 접근하는 역할로 각 계층에 맞게 사용하였습니다.                                 |
|          | Entity와 DTO를 적절히 사용했는가                                                                                                                                                                                            |
|          | -> 클라이언트에게 데이터를 받을 땐 Reqeust DTO를 생성하여 받아주었고, 로직을 처리한 후 응답을 줄 땐 Response DTO를 사용하였습니다.                                                                                                                            |
|          | 일시에 대한 공통 유틸리티 클래스를 생성하여 활용했는가                                                                                                                                                                                    |
|          | -> 일시를 처리 뿐만 아니라, Dto↔Entity 변환해주는 `TravelDtoConverter`라는 공통 유틸리티 클래스를 생성하여 활용하였습니다.                                                                                                                              |
| 기능 확인    | 하나의 여행을 저장하는 기능이 구현되었는가                                                                                                                                                                                           |
|          | -> `POST / api/trips` , (이름, 시작일, 종료일, 해외/국내) 정보를 받아 저장하도록 하였습니다.                                                                                                                                                 |
|          | 여행 일정에 장소에 대한 정보도 추가하여 구현되었는가                                                                                                                                                                                     |
|          | -> 여정의 숙박, 이동, 체류에서 장소명과 주소를 받도록 하여 구현되었습니다.                                                                                                                                                                      |
|          | 특정 여행에 여러 개의 여정을 기록하는 기능이 구현되었는가                                                                                                                                                                                  |
|          | -> `POST /api/trips/{tripId}/itineraries` , 한 개의 여정부터 복수 개의 여정까지 기록이 가능하도록 여정 정보를 List로 받아 처리하였습니다.                                                                                                               |
|          | 전체 여행 리스트를 조회하는 기능이 구현되었는가                                                                                                                                                                                        |
|          | -> `GET /api/trips` , 여행 목록 조회로, 여정을 제외한 여행 정보만 볼 수 있는 조회 기능을 만들었습니다.                                                                                                                                             |
|          | 특정 여행(에 포함된 여정 리스트도) 상세 정보를 조회하는 기능이 구현되었는가                                                                                                                                                                       |
|          | -> `GET /api/trips/{tripId}` , 여행 id를 받아 특정 여행을 선택하고 해당 여행 id로부터 여행 정보를 DB에서 찾아오고, 이 때 fetch join 을 통해 여정에 대한 엔티티도 함께 로드되도록 구현하였습니다.                                                                              |
|          | 특정 여행(에 포함된 여정 리스트는 제외하고)의 정보를 수정할 수 있는 기능이 구현되었는가                                                                                                                                                                |
|          | -> `PUT /api/trips/{tripId}` , 여행의 id를 통해 DB에서 특정 여행을 찾고 클라이언트로 부터 받은 정보로 수정할 수 있도록 구현하였습니다.                                                                                                                      |
|          | 특정 여정의 상세 정보를 수정할 수 있는 기능이 구현되었는가                                                                                                                                                                                 |
|          | -> `PUT /api/itineraries/{itineraryId}` , 여정의 id또한 고유하기 때문에 여행의 id없이 여정의 id만으로 DB에서 여정을 찾고, 수정할 수 있도록 구현하였습니다.                                                                                                    |
| 프로젝트 완성도 | API 문서에 모든 기능을 포함하고, Request/Response에 대해 명확히 설명하였는가                                                                                                                                                              |
|          | -> 네. Swagger에 명확히 설명되어 있습니다.                                                                                                                                                                                     |
|          | 여행 등록 컬럼에 맞지 않는 데이터 등록 시도 시 오류 메세지를 출력하는가                                                                                                                                                                         |
|          | -> 성공은 200, 바인딩 및 클라이언트 에러는 400, 그 외 서버 에러는 500으로 컨벤션을 정하였습니다. 날짜에 대한 검증은 자바에서 기본으로 제공해주는 LocalDateTime을 사용하였고, 잘못된 값이 들어왔을 땐 400 에러와 함께 에러 메세지를 보내는데, 에러 메시지를 커스텀하는 대신 개발자가 보기 쉽도록 에러 메세지를 그대로 return하게 해주었습니다.  |
|          | 여행 조회 및 수정 시 없는 데이터라면 오류 메세지를 출력하는가                                                                                                                                                                               |
|          | -> 성공은 200, 바인딩 및 클라이언트 에러는 400, 그 외 서버 에러는 500으로 컨벤션을 정하였습니다. 날짜에 대한 검증은 자바에서 기본으로 제공해주는 LocalDateTime을 사용하였고, 잘못된 값이 들어왔을 땐 400 에러와 함께 에러 메세지를 보내는데, 에러 메시지를 커스텀하는 대신 개발자가 보기 쉽도록 에러 메세지를 그대로 return하게 해주었습니다.  |
|          | 모든 기능이 정상적으로 동작하는가                                                                                                                                                                                                |
|          | -> 네. 테스트를 모두 마쳤으며 모든 기능이 잘 작동합니다.                                                                                                                                                                                |
