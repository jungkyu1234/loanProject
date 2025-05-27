# 대출심사 프로젝트
**📑 대출심사 시스템 (Loan Review System)**

## 📌 프로젝트 개요
# 📦 CB사 대출심사 모듈 (Credit Bureau Loan Review Module)
대출 신청자의 연소득을 기반으로 **이자율과 대출한도를 산출하는 대출심사 서비스 모듈**입니다.  
본 프로젝트는 **신용평가사(Credit Bureau, 이하 CB사)의 대출심사 모듈**을 독립적인 서비스로 개발한 시스템입니다.
대출 신청자의 연소득을 기반으로 대출 이자율과 한도를 동적으로 산출하여, 금융사의 백엔드 대출 서비스와 연동해 신속하고 정확한 대출 심사 결과를 제공하는 역할을 수행합니다.
본 프로젝트는 **신용평가사(Credit Bureau)의 대출심사 모듈을 독립적인 마이크로서비스 형태**로 개발하여, 금융사의 대출 백엔드 시스템과 느슨하게 연동되는 구조로 설계했습니다.

**Spring Boot 기반의 RESTful API**로 구현되었으며, **확장성과 유지보수성을 고려한 모듈화 설계와 테스트 자동화, AOP 기반 공통 관심사 처리**로 안정성과 품질을 확보한 프로젝트입니다.

## 📌 프로젝트 목적
- **정확하고 신뢰성 있는 대출 심사 로직 구현**

  **→** 대출 신청자의 소득정보를 토대로 심사 기준을 엄격하게 적용하여 적합한 이자율과 대출 한도를 산출함으로써, 금융사 리스크를 최소화하고 고객 맞춤형 대출 상품 제공 지원
- **독립 모듈화로 시스템 통합 효율성 제고**
  
  **→** CB사 대출심사 모듈을 별도의 마이크로서비스 형태로 설계해 금융사의 기존 백엔드 시스템과 느슨하게 연동되도록 하여, 시스템 간 의존성 감소 및 유지보수 편의성 증대
- **최신 기술 스택 활용 및 안정성 강화**

  **→**  Kotlin과 Spring Boot를 활용한 간결하고 안전한 코드베이스 구축, AOP 기반의 공통관심사 처리(로깅, 예외처리) 적용, 테스트 자동화로 서비스 신뢰성 확보

## 🛠️ 사용 기술 스택

| 분류       | 기술명                                                        | 설명 |
|:-----------|:---------------------------------------------------------------|:------|
| Language   | **Kotlin**                                                     | JVM 기반 언어로, 간결하고 안전한 코드 작성 가능 |
| Framework  | **Spring Boot**                                                | 애플리케이션의 설정과 구동을 손쉽게 처리하는 프레임워크 |
| Framework  | **Spring Data JPA**                                           | JPA 기반 ORM 프레임워크, 객체와 테이블 매핑 |
| Framework  | **Spring AOP**                                                 | 횡단 관심사 처리 (로깅, 트랜잭션 관리 등) |
| DB         | **H2 Database**                                                | 개발/테스트용 인메모리 데이터베이스 |
| Cache      | **Redis (spring-data-redis)**                                  | 캐싱 및 세션 저장소 (예정) |
| Build Tool | **Gradle (Kotlin DSL)**                                        | Kotlin 기반 빌드 스크립트 |
| Logging    | **kotlin-logging (3.0.4)**                                     | Kotlin 전용 경량 로깅 라이브러리 |
| API Docs   | **Springfox Swagger UI (3.0.0)**                               | API 문서 자동화 도구 |
| Test       | **MockK (1.12.0)**                                             | Kotlin 전용 Mocking 프레임워크 |
| 메시징     | **Kafka (의존성만 포함)**                                      | 분산 메시징 시스템, 비동기 데이터 처리 |

## 주요 기능
**📌 주요 기능 (Detailed & Professional Ver.)**

📌 1️⃣ **대출 심사 요청 처리 (Loan Application Request Handling)**
- **LoanReceiveController**를 통해 외부에서 들어오는 대출 심사 요청을 RESTful API로 수신합니다.
- 클라이언트가 전송한 **JSON** 형식의 **LoanRequestDto.RequestInputDto** 데이터를 바탕으로 대출 심사 프로세스를 트리거합니다.   
- HTTP POST 메서드를 활용한 안전한 데이터 전송과, 요청 데이터 유효성 검증을 통해 안정적 API 운영을 보장합니다.

**[기술 요소]**

**@RestController, @PostMapping, @RequestBody**<br><br>

📌 2️⃣ **심사 기준에 따른 대출 한도 및 금리 산출 (Dynamic Loan Evaluation Logic)**
- LoanReviewService에서 연소득 금액(userIncomeAmount)을 기반으로 세분화된 심사 정책을 적용, 맞춤형 대출 한도 및 금리를 산출합니다.
- 부적절하거나 음수 등의 비정상 입력값에 대해선 커스텀 예외를 발생시켜 API 호출자에게 명확한 오류 메시지를 반환합니다.
- 비즈니스 로직은 확장 가능하도록 조건문 대신 전략 패턴(Strategy Pattern)이나 룰 엔진 도입 고려 가능.
 
  **예시 산출 기준**
  
  -> 1천만 원 미만: 0.0% 금리, 최대 1천만 원 대출 한도

  -> 1천만 원 이상 2천만 원 미만: 10.0% 금리, 최대 2천만 원 대출 한도
  
**주요 로직**

       -> if (loanRequestDto.userIncomeAmount < 10000000) 
        -> 0.0% 금리, 1,000만 원 한도
        
       -> else if (loanRequestDto.userIncomeAmount < 20000000)
        -> 10.0% 금리, 2,000만 원 한도
        
        ... 이하 생략 ...

**[기술 요소]**

**Kotlin, Service Layer, 런타임 예외 처리(RuntimeException)**<br><br>

📌 3️⃣ **도메인 중심의 계층형 설계 (Domain-Centric Layered Architecture)**
- 클린 아키텍처 원칙에 따라 Controller, Service, DTO 및 Domain 객체를 명확히 분리하여 유지보수성과 확장성 극대화
- 데이터베이스 연동, 메시징 등 인프라 관련 로직은 별도의 모듈(:domain, :kafka)로 분리하여 독립적으로 관리 및 테스트 가능
- 향후 마이크로서비스 아키텍처 확장 시에도 모듈화된 설계가 큰 장점으로 작용

**[기술 요소]**

**Domain-Driven Design(DDD) 구조, Spring MVC**<br><br>

📌 4️⃣ **Swagger 기반 API 문서 자동화 (API Documentation with Swagger)**
- springfox-boot-starter를 활용해 API 스펙을 자동 생성 및 문서화하여, 개발자 및 협업자가 엔드포인트, 요청 파라미터, 응답 구조를 직관적으로 이해할 수 있도록 지원
- API 문서 페이지를 통해 실제 API 테스트 및 응답 검증 가능
- 개발 및 QA 단계에서 신속한 피드백 루프 형성에 기여

**[기술 요소]**

**Springfox Swagger 3.0.0**<br><br>

📌 5️⃣ **테스트 데이터베이스 환경 구성 (In-Memory DB for Testing)**
- H2 인메모리 데이터베이스를 활용해 실제 운영 DB와 격리된 테스트 환경을 구성하여, 빠른 테스트 실행 및 데이터 초기화 가능
- MockK를 통해 외부 의존성(예: 외부 API 호출, 데이터베이스) 없이 단위 테스트 작성 가능
- 지속적인 통합(CI) 환경에서도 안정적으로 작동할 수 있도록 테스트 자동화 구현


**[기술 요소]**

**H2 Database, Mockk**<br><br>

📌 6️⃣ **AOP 기반 로깅 및 공통 처리 (Cross-Cutting Concern Management)**
- Spring AOP를 활용해 비즈니스 로직과 무관한 공통 관심사를 분리, 중복 코드 제거 및 일관된 처리 구현
- 서비스 호출 전후 주요 입력 파라미터, 실행 시간, 예외 발생 내역을 로깅하여 운영 단계에서 문제 추적 및 성능 모니터링 용이
- Kotlin-logging 라이브러리로 경량화된 로깅 구현

**[기술 요소]**

**Spring AOP, Kotlin Logging**<br><br>

## 프로젝트 구조
```
/src
│
├── /main
│   ├── /kotlin
│   │   └── com.zerobase.api
│   │       ├── /api
│   │       ├── /consumer
│   │       ├── /css
│   │       ├── /docker
│   │       ├── /domain
│   │       ├── /gradle
│   │       ├── /kafka
│   │       ├── /kafka-docker
│   │       ├── /nginx
│   │       └── /table
│   └── /resources
│       └── application.yml
└── /test
```

**✅ 프로젝트 전체 구현 기능**
   
   **1️⃣ 대출 심사 요청 API**
  - JSON 형태로 대출 심사 요청 수신
  
  - 연소득 데이터 유효성 검증 및 예외처리
  
  - 비정상 값 입력 시 커스텀 예외 발생
  
  **2️⃣ 대출 이자율 및 한도 산출**
  
  - 연소득 금액 기준 맞춤형 심사 정책 적용
  
   **산출 기준**
  
  - 1천만 원 미만: 0.0% / 1,000만 원 한도
  
  - 1천만 원 이상 2천만 원 미만: 10.0% / 2,000만 원 한도
  
     … (추후 확장 가능)
  
  **3️⃣ 도메인 중심 계층형 설계**
  
  - Controller / Service / DTO / Domain / Repository 분리
  
  - Clean Architecture 기반 모듈화
  
  **4️⃣ Swagger 기반 API 문서 자동화**
  
  - API 문서 자동 생성
  
  - 개발자 및 QA 실시간 API 테스트 가능
  
  **5️⃣ 테스트 환경 구성 및 Mocking**
  
  - H2 DB로 테스트 환경 격리
  
  - MockK로 외부 의존성 Mock 처리
  
  - 통합 테스트 자동화
  
  **6️⃣ AOP 기반 로깅 및 공통처리**
  
  - 서비스 호출 전후 파라미터, 실행 시간, 예외 내역 로깅
  
  - 중복 코드 제거 및 일관된 처리
   
**👨‍💻 개인 구현 기능**

- LoanReviewController, LoanReviewService 직접 구현

- 연소득 기반 대출 한도/이자율 산출 비즈니스 로직 설계 및 커스텀 예외 처리

- Spring AOP를 활용한 공통 로깅/예외처리 모듈화

- Swagger 설정 및 API 문서화 자동화

- 테스트 환경 구축(H2 + MockK + 통합 테스트) 및 테스트 코드 작성

- Clean Architecture 구조 설계 및 도메인 모듈화

- Gradle Kotlin DSL로 빌드 스크립트 구성


**🌱 프로젝트에서 발휘한 역량 & 성과**

✅ Kotlin + Spring Boot 기반 프로젝트 설계 및 구현 경험

✅ API 설계 및 데이터 처리, 비즈니스 로직 분리, 도메인 중심 설계 역량 향상

✅ AOP, 테스트 자동화, API 문서화 등 실무용 핵심 기술 경험

✅ Clean Architecture, 마이크로서비스 형태 설계로 시스템 유연성 확보

✅ 프로젝트 설계부터 구현, 테스트, 문서화까지 전 과정 개인 수행 → 개발자 역량 종합적으로 성장


## 데이터베이스
- **데이터베이스**는 MySQL 또는 개발/테스트 환경에서는 H2 Database를 사용하여 데이터 관리 및 검증을 수행합니다.
- 데이터 처리는 **JPA (Java Persistence API)** 기반의 ORM(Object-Relational Mapping)을 통해 이루어지며, 엔티티 클래스와 Repository 인터페이스를 활용해 데이터베이스와의 연동을 처리합니다.
- 향후 확장성을 고려해 데이터 접근 레이어를 별도의 모듈(:domain)로 구성하여 **유연한 데이터베이스 마이그레이션과 관리**가 가능하도록 설계했습니다.

## 프로필
👨‍💻 프로필
- 백엔드 개발자로서 **Spring Boot, Java, JPA, myBatis** 등을 기반으로 도메인 중심 설계와 RESTful API 개발 경험을 보유하고 있습니다.
- 실무 및 사이드 프로젝트를 통해 **API 설계, 데이터 처리, 시스템 구조 설계**를 직접 수행해왔으며, 효율적인 코드와 안정적인 서비스 운영을 목표로 개발합니다.
- 클린 코드와 테스트 자동화를 중시하며, 유지보수성과 확장성을 고려한 설계에 집중합니다.

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


