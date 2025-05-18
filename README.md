# 대출심사 프로젝트
**📑 대출심사 시스템 (Loan Review System)**

## 📌 프로젝트 개요
# 📦 CB사 대출심사 모듈 (Credit Bureau Loan Review Module)
대출신청자의 연소득을 기반으로 **이자율**과 **대출한도를 산출하는 대출심사 서비스 모듈**입니다.  
대출 서비스를 제공하는 금융사의 백엔드 시스템과 연동하여, 대출 심사 결과를 응답하는 역할을 담당합니다.

## 📌 프로젝트 목적
- 대출 신청 고객의 소득정보를 기반으로 대출 심사 기준을 적용하여 이자율 및 대출 한도를 산출
- CB사(신용평가사) 모듈을 별도의 독립적인 서비스로 설계하여 시스템 간 연동 및 유지보수 효율화
- 스프링부트 기반 REST API 설계 및 AOP, 로깅, 테스트 환경 구축

## 🛠️ 사용 기술 스택

| 분류       | 기술명                                                        | 설명 |
|:-----------|:---------------------------------------------------------------|:------|
| Language   | **Kotlin**                                                     | JVM 기반 언어로, 간결하고 안전한 코드 작성 가능 |
| Framework  | **Spring Boot**                                                | 애플리케이션의 설정과 구동을 손쉽게 처리하는 프레임워크 |
| Framework  | **Spring Data JPA (2.7.6)**                                    | JPA 기반 ORM 프레임워크, 객체와 테이블 매핑 |
| Framework  | **Spring AOP**                                                 | 횡단 관심사 처리 (로깅, 트랜잭션 관리 등) |
| DB         | **H2 Database**                                                | 개발/테스트용 인메모리 데이터베이스 |
| Cache      | **Redis (spring-data-redis)**                                  | 캐싱 및 세션 저장소 (예정) |
| Build Tool | **Gradle (Kotlin DSL)**                                        | Kotlin 기반 빌드 스크립트 |
| Logging    | **kotlin-logging (3.0.4)**                                     | Kotlin 전용 경량 로깅 라이브러리 |
| API Docs   | **Springfox Swagger UI (3.0.0)**                               | API 문서 자동화 도구 |
| Test       | **MockK (1.12.0)**                                             | Kotlin 전용 Mocking 프레임워크 |
| 메시징     | **Kafka (의존성만 포함, 추후 연동 예정)**                      | 분산 메시징 시스템, 비동기 데이터 처리 |

## 주요 기능
**📌 주요 기능 (Detailed & Professional Ver.)**

📌 1️⃣ **대출 심사 요청 처리 (Loan Application Request Handling)**
- **LoanReceiveController**를 통해 외부에서 들어오는 대출 심사 요청을 RESTful API로 수신합니다.
- 클라이언트가 전송한 **JSON** 형식의 **LoanRequestDto.RequestInputDto** 데이터를 바탕으로 대출 심사 프로세스를 트리거합니다.   

**[기술 요소]**

**@RestController, @PostMapping, @RequestBody**<br><br>

📌 2️⃣ **심사 기준에 따른 대출 한도 및 금리 산출 (Dynamic Loan Evaluation Logic)**
- LoanReviewService에서 사용자의 소득 금액(userIncomeAmount) 에 따라 심사 기준을 적용하여 대출 한도와 금리를 산출합니다.
- 유효성 검사를 통해 음수나 비정상적인 입력값에 대한 예외 처리를 수행하고, 적절한 메시지와 함께 예외를 발생시켜 안정적인 API 운영을 보장합니다.

**주요 로직**

if (loanRequestDto.userIncomeAmount < 10000000) 
    -> 0.0% 금리, 1,000만 원 한도
    
else if (loanRequestDto.userIncomeAmount < 20000000)
    -> 10.0% 금리, 2,000만 원 한도
    
... 이하 생략 ...


**[기술 요소]**

**Kotlin, Service Layer, 예외 처리(RuntimeException)**<br><br>

📌 3️⃣ **도메인 중심의 계층형 설계 (Domain-Centric Layered Architecture)**
- Controller → Service → DTO 형태의 계층형 아키텍처로 설계하여 역할 분리를 명확히 하고, 유지보수성을 향상시킴.
- 데이터베이스 연동이나 외부 API 호출 같은 인프라 레이어를 **별도의 모듈(project :domain, :kafka)**로 분리하여 확장성과 독립성을 확보.

**[기술 요소]**

**Domain-Driven Design(DDD) 구조, Spring MVC**<br><br>

📌 4️⃣ **Swagger 기반 API 문서 자동화 (API Documentation with Swagger)**
- springfox-boot-starter를 이용하여 API 스펙을 자동으로 문서화하고, 대출 심사 API의 엔드포인트, 요청 파라미터, 응답 값 등을 직관적으로 확인 가능.
- 개발 및 테스트 단계에서 API 호출과 응답 데이터를 쉽게 검증할 수 있도록 지원.

**[기술 요소]**

**Springfox Swagger 3.0.0**<br><br>

📌 5️⃣ **테스트 데이터베이스 환경 구성 (In-Memory DB for Testing)**
- 테스트 시 H2 Database를 활용하여 경량의 인메모리 데이터베이스 환경을 구성, 실제 운영 데이터베이스와 분리된 테스트 환경 제공.
- Mockk 라이브러리로 단위 테스트 작성 시 외부 의존성을 제거하고, 테스트의 독립성과 정확성을 확보.

**[기술 요소]**

**H2 Database, Mockk**<br><br>



📌 6️⃣ **AOP 기반 로깅 및 공통 처리 (Cross-Cutting Concern Management)**
- spring-boot-starter-aop를 활용하여 공통적으로 필요한 로깅, 예외 처리 등의 기능을 AOP로 구현 가능.
- 서비스 호출 전후의 주요 정보(입력값, 실행 시간 등)를 공통 처리하여 시스템 안정성과 추적성을 높임.

**[기술 요소]**

**Spring AOP, Kotlin Logging**<br><br>

## 프로젝트 구조
```
/src
│
├── /main
│   ├── /java
│   │   └── com.example.project
│   │       ├── /config
│   │       ├── /controller
│   │       ├── /domain
│   │       ├── /repository
│   │       ├── /service
│   │       └── /SpringMvcProjectApplication.java
│   └── /resources
│       ├── /static
│       ├── /templates
│       └── application.yml
└── /test
```

## 데이터베이스
- **데이터베이스**는 MySQL 또는 개발/테스트 환경에서는 H2 Database를 사용하여 데이터 관리 및 검증을 수행합니다.
- 데이터 처리는 **JPA (Java Persistence API)** 기반의 ORM(Object-Relational Mapping)을 통해 이루어지며, 엔티티 클래스와 Repository 인터페이스를 활용해 데이터베이스와의 연동을 처리합니다.
- 향후 확장성을 고려해 데이터 접근 레이어를 별도의 모듈(:domain)로 구성하여 **유연한 데이터베이스 마이그레이션과 관리**가 가능하도록 설계했습니다.

## 프로필
👨‍💻 프로필
- 백엔드 개발자로서 **Spring Boot, Java, JPA, myBatis** 등을 기반으로 도메인 중심 설계와 RESTful API 개발 경험을 보유하고 있습니다.
- 실무 및 사이드 프로젝트를 통해 **API 설계, 데이터 처리, 시스템 구조 설계**를 직접 수행해왔으며, 효율적인 코드와 안정적인 서비스 운영을 목표로 개발합니다.

## 기여
- 본 프로젝트는 개인 프로젝트로, 모든 코드는 직접 작성하였습니다.

## 라이선스
📜 라이선스
- MIT 라이선스를 준수합니다.

## 문의
📮 문의
- 이 프로젝트에 대한 문의 사항은 언제든지 연락 주시기 바랍니다! 🚀

**📧 이메일 : kchs1230@naver.com**

**📱 깃허브 : https://github.com/jungkyu1234/loanProject**

---


