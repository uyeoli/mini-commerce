# MapStruct 기초

> MapStruct를 처음 접하는 개발자를 위한 문서입니다.

---

## 무엇인가

MapStruct는 Java 객체 간 변환(매핑) 코드를 **컴파일 타임에 자동 생성**해주는 코드 생성 라이브러리다. Entity ↔ DTO 변환처럼 반복적으로 작성해야 하는 필드 복사 코드를 어노테이션 기반 인터페이스 정의만으로 대체할 수 있다. 런타임 리플렉션 없이 순수 Java 메서드로 생성되므로 성능도 수동 변환과 동일하다.

---

## 왜 등장했는가

1. **문제 상황** — Spring Boot 애플리케이션에서 DB 엔티티(`UserEntity`)를 API 응답 DTO(`UserResponse`)로 변환해야 할 때, 필드가 10개 이상이면 단순 getter/setter 복사 코드가 수십 줄에 달한다. 필드가 추가되거나 이름이 바뀌면 변환 코드를 일일이 찾아 수정해야 한다.

2. **기존 방식의 한계** — `ModelMapper` 같은 리플렉션 기반 라이브러리를 쓰면 코드는 줄지만, 런타임에 필드 이름을 문자열로 매핑하기 때문에 **컴파일 타임에 오류를 잡을 수 없다**. 필드명 오타나 타입 불일치가 프로덕션 배포 후에야 발견된다. 또한 리플렉션 오버헤드로 인해 성능이 떨어진다.

3. **MapStruct가 해결하는 방식** — 인터페이스에 메서드 시그니처만 선언하면 컴파일 시 APT(Annotation Processing Tool)가 실제 변환 구현체를 생성한다. 필드 불일치는 **컴파일 에러**로 즉시 감지되고, 생성된 코드는 일반 Java 코드이므로 리플렉션 오버헤드가 없다.

---

## 직관적 비유

> **번역 대행사**에 비유할 수 있다.

영어 문서(Entity)를 한국어 문서(DTO)로 바꿔야 할 때, 매번 직접 번역하는 대신 "이 항목은 저 항목으로 번역해줘"라는 **번역 지시서(Mapper 인터페이스)** 를 제출한다. 번역 대행사(MapStruct APT)는 지시서를 보고 실제 번역본(구현 클래스)을 미리 만들어둔다. 이후에는 번역 대행사를 거치지 않고 미리 만들어진 번역본을 그냥 읽기만 하면 된다.

---

## 핵심 특징

- **컴파일 타임 코드 생성** — APT가 빌드 시 구현체를 생성하므로, 매핑 오류를 런타임 전에 발견한다.
- **리플렉션 없는 순수 Java 코드** — 생성된 코드는 일반 getter/setter 호출이므로 ModelMapper 대비 성능이 월등히 빠르다.
- **필드명 불일치 처리** — `@Mapping(source = "userName", target = "name")` 으로 이름이 다른 필드도 간단히 매핑한다.
- **중첩 객체 및 컬렉션 자동 처리** — List, Set 등 컬렉션과 중첩 객체도 별도 설정 없이 자동으로 변환한다.
- **Spring 통합 지원** — `componentModel = "spring"` 설정 한 줄로 생성된 구현체가 Spring Bean으로 등록된다.

---

## 기존 기술과 비교

| 특징 | 수동 변환 (getter/setter) | ModelMapper | MapStruct |
|------|--------------------------|-------------|-----------|
| 코드 양 | 많음 (필드 수만큼) | 적음 | 적음 |
| 컴파일 타임 오류 감지 | 가능 | 불가 (런타임) | 가능 |
| 성능 | 빠름 | 느림 (리플렉션) | 빠름 (생성 코드) |
| 타입 안정성 | 높음 | 낮음 | 높음 |
| 학습 곡선 | 없음 | 낮음 | 낮음 |
| 디버깅 용이성 | 쉬움 | 어려움 | 쉬움 (코드 확인 가능) |

---

## 구조 설명

```
[ 개발자가 작성 ]
  @Mapper
  interface UserMapper {
      UserResponse toResponse(UserEntity entity);
  }

          │ 컴파일 (APT)
          ▼

[ MapStruct가 생성 ]
  class UserMapperImpl implements UserMapper {
      public UserResponse toResponse(UserEntity entity) {
          UserResponse response = new UserResponse();
          response.setId(entity.getId());       // 자동 생성
          response.setName(entity.getName());   // 자동 생성
          ...
          return response;
      }
  }

          │ Spring Bean 등록
          ▼

[ 서비스에서 사용 ]
  @Autowired UserMapper userMapper;
  UserResponse dto = userMapper.toResponse(entity);
```

---

## 기본 명령어 / 최소 코드 예제

**build.gradle 의존성 추가:**
```groovy
dependencies {
    implementation 'org.mapstruct:mapstruct:1.5.5.Final'
    annotationProcessor 'org.mapstruct:mapstruct-processor:1.5.5.Final'
}
```

MapStruct는 APT 기반이므로 `annotationProcessor` 선언이 반드시 필요하다.

**Spring Boot 예제:**

```java
// Entity
@Entity
public class UserEntity {
    private Long id;
    private String userName;  // 필드명이 DTO와 다름
    private String email;
}

// DTO
public class UserResponse {
    private Long id;
    private String name;      // "userName" → "name" 매핑 필요
    private String email;
}
```

```java
// Mapper 인터페이스 정의
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "userName", target = "name")  // 필드명 불일치 처리
    UserResponse toResponse(UserEntity entity);

    List<UserResponse> toResponseList(List<UserEntity> entities);  // 컬렉션 자동 처리
}
```

```java
// Service에서 사용
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;  // Spring Bean으로 자동 주입

    public UserResponse getUser(Long id) {
        UserEntity entity = userRepository.findById(id).orElseThrow();
        return userMapper.toResponse(entity);  // 변환 코드 한 줄
    }
}
```

`componentModel = "spring"` 덕분에 `UserMapperImpl`이 Spring 컨텍스트에 Bean으로 등록되어 `@Autowired` / 생성자 주입으로 사용할 수 있다.

**생성된 코드 확인 위치:**
```bash
# 빌드 후 생성된 구현체 확인
build/generated/sources/annotationProcessor/java/main/com/.../UserMapperImpl.java
```

생성된 `UserMapperImpl.java`를 직접 열어보면 MapStruct가 작성한 매핑 로직을 확인할 수 있다.

---

## Summary

| 개념 | 설명 |
|------|------|
| `@Mapper` | Mapper 인터페이스 선언 어노테이션. `componentModel = "spring"` 으로 Bean 등록 가능 |
| `@Mapping` | 필드명이 다를 때 source→target 명시. 생략 시 동일 이름 자동 매핑 |
| APT | Annotation Processing Tool. 컴파일 시 `@Mapper` 인터페이스를 읽고 구현체를 생성하는 도구 |
| `MapperImpl` | MapStruct가 생성한 실제 구현 클래스. `build/generated/` 경로에 위치 |
| `annotationProcessor` | Gradle/Maven에서 APT를 활성화하는 의존성 scope |

---

## When to Use

- **Entity ↔ DTO 변환이 빈번한 경우** — Controller 계층에서 Entity를 그대로 반환하지 않고 DTO로 변환해야 할 때 (보안, API 안정성)
- **필드가 많은 객체 변환** — 10개 이상의 필드를 수동으로 복사하는 코드가 생기기 시작할 때
- **도메인 모델이 자주 변경되는 프로젝트** — 필드 추가/삭제 시 컴파일 에러로 누락된 매핑을 즉시 발견해야 할 때
- **성능이 중요한 서비스** — 대량의 목록 응답을 처리하는 API에서 ModelMapper의 리플렉션 오버헤드를 제거하고 싶을 때
- **코드 리뷰 효율화** — 변환 로직을 Mapper 인터페이스 한 곳에 모아 변경 이력을 추적하기 쉽게 만들고 싶을 때

---

## Pitfalls

- **`annotationProcessor` 누락** — `implementation`만 추가하고 `annotationProcessor`를 빠뜨리면 구현체가 생성되지 않아 런타임에 `NullPointerException`이 발생한다. Lombok과 함께 사용 시 Lombok의 `annotationProcessor`도 반드시 함께 선언해야 한다.
- **Lombok과의 순서 문제** — Gradle에서 Lombok과 MapStruct를 함께 쓸 때, Lombok이 먼저 처리되어야 MapStruct가 Lombok이 생성한 getter/setter를 인식한다. `annotationProcessor` 선언 순서를 `lombok` → `mapstruct-processor` 순으로 유지한다.
- **`@Mapping` 없이 다른 필드명 사용** — 필드명이 다른데 `@Mapping`을 생략하면 해당 필드는 `null`로 매핑된다. 컴파일 에러가 나지 않으므로 주의해야 한다. `unmappedTargetPolicy = ReportingPolicy.ERROR` 설정으로 미매핑 필드를 컴파일 에러로 만들 수 있다.
- **중첩 객체 Mapper 미등록** — 중첩 객체(예: `OrderEntity` 안의 `UserEntity`)를 변환할 때, 해당 타입의 Mapper도 `@Mapper(uses = {UserMapper.class})` 로 등록해야 자동 위임이 된다. 누락 시 중첩 객체가 `null`로 변환된다.
- **생성된 코드를 직접 수정하지 말 것** — `MapperImpl`은 빌드마다 덮어쓰여진다. 커스텀 로직이 필요하면 Mapper 인터페이스에 `default` 메서드로 추가하거나 `@BeforeMapping` / `@AfterMapping`을 활용한다.