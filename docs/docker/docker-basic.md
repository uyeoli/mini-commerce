# Docker 기초

> Docker를 처음 접하는 개발자를 위한 문서입니다.

---

## 무엇인가

Docker는 애플리케이션과 그 실행에 필요한 모든 환경(라이브러리, 설정, OS 의존성 등)을 **컨테이너(Container)** 라는 격리된 단위로 묶어 어디서든 동일하게 실행할 수 있게 해주는 플랫폼이다. "내 PC에서는 되는데 서버에서는 안 돼요" 문제를 근본적으로 해결한다.

---

## 왜 등장했는가

1. **문제 상황** — 개발자 A의 로컬에서는 Java 11, MySQL 8.0 환경으로 정상 작동하던 앱이, 팀원 B의 PC(Java 17, MySQL 5.7)에서 실행하면 오류가 발생한다. 배포 서버 환경은 또 다르다.

2. **기존 방식의 한계** — VM(가상머신)으로 환경을 통일할 수 있었지만, VM은 OS 전체를 포함하기 때문에 이미지 크기가 수 GB에 달하고, 부팅 시간도 수 분이 걸린다. 팀원 수만큼 무거운 VM을 나눠 갖는 건 비효율적이다.

3. **Docker가 해결하는 방식** — OS 커널은 호스트와 공유하고, 앱 실행에 필요한 파일 시스템과 프로세스만 격리한다. 이미지 크기는 수십 MB~수백 MB로 가볍고, 컨테이너는 수 초 안에 기동된다. `docker run`  한 줄로 누구나 동일한 환경을 재현할 수 있다.

---

## 직관적 비유

> **도시락 통**에 비유할 수 있다.

음식(앱)을 만들 때마다 주방(서버 환경)을 세팅하는 대신, 음식과 필요한 소스·수저까지 하나의 도시락 통에 담아버린다. 상대방은 주방이 없어도 도시락 통만 받으면 똑같이 먹을 수 있다. 도시락 통의 **설계도**가 `Dockerfile`, 완성된 도시락 통이 **이미지(Image)**, 실제로 열어 먹고 있는 상태가 **컨테이너(Container)** 다.

---

## 핵심 특징

- **이식성(Portability)** — 개발·테스트·운영 환경에서 동일한 이미지를 사용하므로 환경 차이로 인한 버그가 없다.
- **격리성(Isolation)** — 컨테이너끼리 파일 시스템·네트워크·프로세스가 분리되어 서로 간섭하지 않는다.
- **경량성(Lightweight)** — VM과 달리 호스트 OS 커널을 공유하므로 오버헤드가 적고 빠르게 기동된다.
- **레이어 캐싱(Layer Caching)** — 이미지는 변경된 레이어만 재빌드하므로 빌드 속도가 빠르다.
- **선언적 구성(Declarative Config)** — `Dockerfile`과 `docker-compose.yml`로 인프라를 코드로 관리한다.

---

## 기존 기술과 비교

| 특징 | 전통적 배포 | 가상머신(VM) | Docker 컨테이너 |
|------|------------|-------------|----------------|
| 환경 재현성 | 낮음 (수동 세팅) | 높음 | 높음 |
| 기동 시간 | 즉시 (이미 설치됨) | 수 분 | 수 초 |
| 이미지 크기 | 없음 | 수 GB (OS 포함) | 수십~수백 MB |
| 격리 수준 | 없음 | 완전 격리 (하이퍼바이저) | 프로세스 격리 (커널 공유) |
| 이식성 | 낮음 | 중간 | 높음 |
| 자원 오버헤드 | 없음 | 높음 | 낮음 |

---

## 구조 설명

```
[ Dockerfile ]
     │ docker build
     ▼
[ Image (이미지) ]          ← 읽기 전용 스냅샷 (레이어 구조)
     │ docker run
     ▼
[ Container (컨테이너) ]    ← 실행 중인 프로세스 (쓰기 가능 레이어 추가)
     │
     ├── 포트 매핑 (-p 8080:8080)
     ├── 볼륨 마운트 (-v ./data:/data)
     └── 환경변수 (-e SPRING_PROFILES_ACTIVE=prod)


[ docker-compose.yml 구성 예시 ]

  ┌─────────────┐     ┌──────────────┐     ┌───────────────┐
  │  app 컨테이너 │────▶│  db 컨테이너  │     │ redis 컨테이너 │
  │ (Spring Boot)│     │  (MySQL)     │     │   (Cache)     │
  └─────────────┘     └──────────────┘     └───────────────┘
         └──────────────────┴─────────────────┘
                   Docker 내부 네트워크 (bridge)
```

---

## 기본 명령어 / 최소 코드 예제

**CLI 예제:**
```bash
# 이미지 빌드
docker build -t my-app:latest .

# 컨테이너 실행 (백그라운드, 포트 매핑)
docker run -d -p 8080:8080 --name my-app my-app:latest

# 실행 중인 컨테이너 목록 확인
docker ps

# 컨테이너 로그 확인
docker logs -f my-app

# 컨테이너 접속 (bash)
docker exec -it my-app bash

# 컨테이너 중지 및 삭제
docker stop my-app && docker rm my-app

# 이미지 목록 확인 및 삭제
docker images
docker rmi my-app:latest
```

**Dockerfile 예제 (Spring Boot):**
```dockerfile
# 1단계: 빌드
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle bootJar --no-daemon

# 2단계: 실행 이미지 (경량)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

멀티 스테이지 빌드를 사용해 빌드 도구는 최종 이미지에 포함되지 않으므로 이미지 크기를 최소화할 수 있다.

**docker-compose.yml 예제 (Spring Boot + MySQL + Redis):**
```yaml
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/commerce
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: secret
      SPRING_DATA_REDIS_HOST: redis
    depends_on:
      - db
      - redis

  db:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: secret
      MYSQL_DATABASE: commerce
    volumes:
      - db-data:/var/lib/mysql
    ports:
      - "3306:3306"

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"

volumes:
  db-data:
```

`docker compose up -d` 한 줄로 세 컨테이너를 동시에 기동하고, `depends_on`으로 기동 순서를 제어한다.

---

## Summary

| 개념 | 설명 |
|------|------|
| Image (이미지) | 컨테이너 실행을 위한 읽기 전용 템플릿. `docker build`로 생성 |
| Container (컨테이너) | 이미지를 실행한 인스턴스. 격리된 프로세스 |
| Dockerfile | 이미지 빌드 절차를 기술한 스크립트 |
| docker-compose.yml | 여러 컨테이너를 함께 정의하고 orchestrate하는 설정 파일 |
| Layer (레이어) | 이미지를 구성하는 읽기 전용 파일 시스템 단계. 캐싱의 단위 |
| Volume (볼륨) | 컨테이너가 종료돼도 데이터가 유지되는 영구 저장소 |
| Port Mapping | 호스트 포트와 컨테이너 포트를 연결 (`-p 호스트:컨테이너`) |
| Bridge Network | Docker가 기본 생성하는 내부 가상 네트워크. 컨테이너명으로 통신 가능 |

---

## When to Use

- **팀 개발 환경 통일** — 신규 팀원이 `docker compose up` 한 줄로 DB·캐시 포함 전체 개발 환경을 구성해야 할 때
- **CI/CD 파이프라인** — GitHub Actions, Jenkins 등에서 빌드·테스트 환경을 코드로 정의하고 재현 가능하게 유지할 때
- **MSA(마이크로서비스) 로컬 테스트** — 여러 서비스를 로컬에서 함께 띄워 통합 테스트를 수행할 때
- **운영 배포** — AWS ECS, Kubernetes 등 컨테이너 오케스트레이션 플랫폼에 배포할 이미지를 준비할 때
- **레거시 앱 격리** — 서로 다른 Java 버전이 필요한 여러 앱을 한 서버에서 충돌 없이 운영할 때

---

## Pitfalls

- **컨테이너 내부에 데이터를 저장하지 말 것** — 컨테이너가 삭제되면 내부 데이터도 사라진다. DB 데이터, 파일 업로드 등은 반드시 `Volume`을 사용한다.
- **`latest` 태그 남용 금지** — `latest`는 버전이 명시되지 않아 예상치 못한 업데이트로 장애가 발생할 수 있다. `mysql:8.0.36` 처럼 구체적인 태그를 지정한다.
- **`depends_on`은 기동 순서만 제어** — `depends_on`은 컨테이너가 시작되는 순서만 보장하며, DB가 실제로 준비됐는지(health check)는 보장하지 않는다. Spring의 재시도 설정 또는 `healthcheck`를 함께 사용한다.
- **`.dockerignore` 누락** — `.dockerignore` 없이 빌드하면 `node_modules`, `.git`, 빌드 캐시 등 불필요한 파일이 이미지에 포함되어 크기가 커진다.
- **루트 유저로 실행 금지** — 기본적으로 컨테이너는 root로 실행된다. `Dockerfile`에 `USER` 지시어로 비권한 사용자를 지정해 보안 리스크를 낮춘다.
