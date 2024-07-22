# 대규모 예약 구매 프로젝트

## 프로젝트 소개
예약구매는 e-Commerce 플랫폼입니다. 사용자는 동시에 다양한 상품을 결제하고 예약 구매할 수 있는 통합 서비스를 경험할 수 있습니다. 이 서비스는 사용자 중심의 웹 사이트로 원하는 상품을 쉽고 편리하게 예약 구매할 수 있는 환경을 제공합니다.

## 프로젝트 기간
- 2024-06-19 ~ 2024-07-17 (4주간)

## 기술 스택
- **Framework:** Spring Boot 2.7.16
- **Language:** Java 17
- **Security:** Spring Security
- **ORM:** JPA
- **Database:** MySQL
- **Caching:** Redis
- **Containerization:** Docker, Docker-compose

## 실행 방법
1. Docker와 Docker-compose가 설치되어 있어야 합니다.
2. 프로젝트 루트 디렉토리에서 다음 명령어를 실행하여 서비스를 시작합니다:
    ```sh
    docker-compose up -d
    ```

## 프로젝트 아키텍처
프로젝트 아키텍처는 다음과 같은 구성 요소로 이루어져 있습니다:

(아키텍처 다이어그램 추가하기)

아키텍처 다이어그램을 아래에 추가합니다:
![프로젝트 아키텍처](architecture-diagram.png)

## ERD
(ERD 정리해서 사진추가하기)

ERD(Entity-Relationship Diagram)는 다음과 같습니다:
![ERD](erd-diagram.png)

## API 명세서
(API 명세 정리하고 추가하기)

API 명세서는 [API 명세서 링크](api-specs)에서 확인할 수 있습니다.

## 성능 최적화, 기술 결정 및 트러블 슈팅

### 성능 최적화
- **캐싱:** 자주 조회되는 상품 상세 정보에 캐싱을 적용하여 데이터베이스 조회 빈도를 줄였고, 평균 응답 시간이 20ms에서 8ms로 약 60% 감소하였습니다. [자세히 보기](https://baileyton.tistory.com/57)
- **비동기 처리:** 대용량 트래픽을 처리하기 위해 일부 작업을 비동기 처리하였습니다.

### 기술 결정
- **Docker:** 일관된 개발 환경과 배포를 위해 사용 하였습니다.
- **Redis:** 빠른 데이터 접근을 위한 캐시 시스템으로 선택하였습니다.
- **Java:** 최신 LTS(Long-Term Support) 버전으로, 성능 개선 및 새로운 기능을 제공하여 안정적이고 효율적인 개발을 지원합니다. [자세히 보기](https://baileyton.tistory.com/37)
- **DataBase :** 높은 안정성과 성능, 광범위한 커뮤니티 지원을 통해 신뢰할 수 있는 데이터베이스 솔루션을 제공합니다. [자세히 보기](https://baileyton.tistory.com/38)

### 트러블 슈팅
- **문제:** 여러 사용자가 동시에 주문을 할 때, 재고 수량이 올바르게 감소하지 않는 문제가 발생
    - **해결:** Pessimistic Lock을 사용하여 동시성 문제를 해결