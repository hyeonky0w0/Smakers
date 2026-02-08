# 🚀 Smakers-BE
> Smakers 서비스의 백엔드 API 서버입니다.
---

### 🛠 Tech Stack
| Category | Technology      | Badge |
| :--- |:----------------| :--- |
| **Language** | Java 17         | ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white) |
| **Framework** | Spring Boot 4.x | ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white) |
| **Persistence** | JPA / Hibernate | ![JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white) |
| **Build / Test** | Gradle / JUnit5 | ![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white) ![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white) |
| **API Docs** | Swagger         | ![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black) |


### 🌐 Infrastructure & Third Party

| Category | Technology | Badge |
| :--- | :--- | :--- |
| **Cloud Storage** | AWS S3 | ![AWS S3](https://img.shields.io/badge/Amazon_S3-FF9900?style=for-the-badge&logo=amazons3&logoColor=white) |
| **PDF Generation** | iText7 / Thymeleaf | ![iText7](https://img.shields.io/badge/iText7-003865?style=for-the-badge&logo=adobeacrobatreader&logoColor=white) ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white) |
| **Communication** | WebClient | ![Spring WebFlux](https://img.shields.io/badge/Spring_WebFlux-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![SSE](https://img.shields.io/badge/SSE_Streaming-000000?style=for-the-badge&logo=lighthouse&logoColor=white) |
| **JSON Parsing** | Jackson / org.json | ![Jackson](https://img.shields.io/badge/Jackson-000000?style=for-the-badge&logo=json&logoColor=white) ![org.json](https://img.shields.io/badge/org.json-000000?style=for-the-badge&logo=json&logoColor=white) |
### 📑 API Documentation & Testing

| Category | Technology | Badge |
| :--- | :--- | :--- |
| **API Docs** | Swagger UI | ![Swagger](https://img.shields.io/badge/Swagger_OpenAPI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black) |
| **Testing** | JUnit 5 | ![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white) |
| **Test Starter** | Spring Test | ![Spring](https://img.shields.io/badge/Spring_Boot_Starter_Test-6DB33F?style=for-the-badge&logo=spring&logoColor=white) |

---

### 📋 Requirements

개발 및 실행을 위해 아래 환경이 필요합니다.

- [x] **Java**: 17 이상
- [x] **IDE**: IntelliJ IDEA (Lombok Plugin 설치 필수)
- [x] **Database**: MySQL 8.0 이상
- [x] **Cloud**: AWS S3 Bucket Access (Access Key / Secret Key 필요)
---

## ⚙️ 깃 브랜치 전략 <a id="깃-브랜치-전략"></a>

### **깃 플로우(Git-flow)**

<img src="https://github.com/user-attachments/assets/ff9fe80b-c684-496c-88fb-b2d0378adc78" height="200" />

- **Main**: 배포 가능한 안정된 코드 저장.
- **Develop**: 개발 중인 코드 통합 및 테스트.
- **Feature Branches**: 개별 기능 개발 후 Develop에 병합.

### **브랜치 전략**
- **명확한 구조**: 개발, 테스트, 배포 단계 구분.
- **안정성**: Main은 항상 검증된 코드만 유지.
- **동시 개발**: 각 기능을 독립적으로 작업해 충돌 최소화.
---


## 📝 프로젝트 소개 (Introduction)
### 3D 분해로 원리를 보고, AI 튜터로 공학을 마스터하다
>텍스트 위주의 평면적인 학습을 넘어, 7종의 정밀 기계 에셋(Drone, V4 Engine, Robot Arm 등)을 3D로 직접 조립·분해하며 시각적 이해도를 극대화하고, AI를 통해 개인화된 학습 경험을 제공하는  에듀테크 플랫폼입니다.

### 🌟Service Concept
> 복잡한 기계 장치의 내부 구조와 부품 간의 유기적인 역학 관계를 3D 인터랙션으로 학습합니다.  
> 사용자가 학습 중 남긴 메모와 취약점을 AI가 분석하여, 기능·구조·오개념 중심의 맞춤형 퀴즈를 생성하고 학습 결과를 데이터화합니다.

------
## 🔑 주요 기능

| **서비스 도메인** | **기능 설명** |
| :--- | :--- |
| **에셋 및 부품 관리** | 58종의 정밀 기계 부품 및 에셋에 대한 메타데이터를 관리하며, 부품 간의 계층 구조와 재질 정보를 체계적으로 시각화하여 제공합니다. |
| **워크플로우 시스템** | 노드와 엣지 기반의 인터랙티브 캔버스를 통해 학습 흐름을 설계하며, **Autosave 시스템**을 통해 편집 중인 데이터를 실시간으로 동기화합니다. |
| **AI 튜터 채팅** | **SSE(Server-Sent Events)** 스트리밍 기술을 적용하여 AI 답변의 지연 시간을 최소화하고, 학습 맥락을 유지하는 실시간 질의응답 서비스를 제공합니다. |
| **맞춤형 퀴즈 생성** | 사용자의 학습 에셋을 AI가 실시간 분석하여 최적화된 퀴즈를 자동 생성하며, **비동기 스레드 처리**를 통해 생성 대기 시간을 최적화합니다. |
| **학습 리포트 생성** | 작성된 메모, 채팅 내역, 퀴즈 결과를 통합하여 **iText7 기반의 고해상도 PDF** 리포트를 자동 렌더링하고 다운로드 기능을 지원합니다. |
| **리소스 서빙 최적화** | **AWS S3와 CloudFront(OAC)**를 결합하여 대용량 3D 에셋 및 이미지 파일을 보안성 있게 관리하고, CDN 캐싱을 통해 전송 속도를 극대화합니다. |
| **인프라 및 배포** | **GitHub Actions와 Docker**를 활용한 CI/CD 파이프라인을 구축하여 운영 효율성을 높이고 안정적인 서비스 환경을 제공합니다. |
---

#  📐 데이터 모델링
### 📌 ERD
![ERD 모델링](https://github.com/user-attachments/assets/93e3944b-39c9-49a2-9870-ca82bed4c7a8)

------


# 📑 프로젝트 기획

### 📌 기능 명세서
[![Google Sheets](https://img.shields.io/badge/Google_Sheets-34A853?style=for-the-badge&logo=google-sheets&logoColor=white)](https://docs.google.com/spreadsheets/d/12Q8J0j1wNkx3RLtu9HwmBpJenPvjJPnH8-zdxqeId0U/edit?usp=sharing)
### 📌 API 상세 명세서
[![Google Sheets](https://img.shields.io/badge/Google_Sheets-34A853?style=for-the-badge&logo=google-sheets&logoColor=white)](https://docs.google.com/spreadsheets/d/1y46EP1XeVnB-MjXWoIwj2417uBBYYWNEdNhrxL9efbI/edit?usp=sharing)

------


## 👥 Roles and Responsibilities
### 현경
> **도메인 설계 및 인프라 아키텍처 구축**
> * **Domain Design**: 에셋/부품, 메모, 워크플로우 도메인 모델링 및 CRUD 구현
> * **Workflow**: 실시간 노드/엣지 저장 시스템(Autosave) 및 데이터 정합성 관리
>* **Infra & DevOps**: AWS(S3, CloudFront) 인프라 설계 및 GitHub Actions CI/CD 구축

### 채연
> **AI 기반 학습 콘텐츠 및 리포트 시스템 구축**  
> - **AI Interaction**: WebClient 기반 AI API 연동 및 SSE 스트리밍 답변 구현  
> - **Quiz Service**: 학습 컨텍스트(메모/채팅) 분석을 통한 맞춤형 퀴즈 생성 로직 구현  
> - **Reporting**: Thymeleaf & iText7을 이용한 실시간 학습 리포트 PDF 자동 생성



----




## 🛠️ Technical Troubleshooting

### 현경 (Infrastructure & Database)

| 주제 | 요약 | 상세 내용 (Click to expand) |
| :--- | :--- | :--- |
| **JPA** | **Autosave 유니크 제약 오류** | <details><summary>상태 관리 로직 최적화</summary><br/>**[문제]** 수정 로직에서 `new Node()` 생성으로 인한 Duplicate Entry 발생<br/>**[원인]** 준영속 객체를 JPA가 `Insert`로 판단<br/>**[해결]** 기존 엔티티 존재 여부 선조회 후 변경 감지(Dirty Checking) 유도<br/>**[교훈]** 엔티티 생명주기 관리의 중요성 체감</details> |
| **Infra** | **S3 vs CloudFront 설계** | <details><summary>서빙 아키텍처 의사결정</summary><br/>**[문제]** Private S3 리소스의 보안과 성능 간의 균형 고민<br/>**[선택]** CloudFront + OAC 구조 채택<br/>**[이유]** UUID 기반 비노출 식별자로 보안 유지 + CDN 캐싱으로 Latency 단축<br/>**[교훈]** 시스템 패턴에 맞는 적절한 복잡도 선택</details> |



### 채연 (AI & UX Optimization)

| 주제 | 요약 | 상세 내용 (Click to expand) |
| :--- | :--- | :--- |
| **Async** | **퀴즈 생성 대기 시간 단축** | <details><summary>비동기 처리(Async) 도입</summary><br/>**[문제]** AI 분석 시간(15초+)으로 인한 브라우저 타임아웃<br/>**[해결]** Spring `@Async` 도입으로 백그라운드 처리 및 즉시 응답 반환<br/>**[효과]** 사용자 대기 차단 방지 및 서버 자원 점유 효율화</details> |
| **SSE** | **AI 채팅 응답 지연 해소** | <details><summary>실시간 스트리밍 답변</summary><br/>**[문제]** 답변 완료 전까지 빈 화면 노출로 인한 UX 저하<br/>**[해결]** Server-Sent Events(SSE) 기반 토큰 단위 실시간 전송<br/>**[효과]** TTFB(첫 응답 시간) 단축으로 체감 응답 속도 대폭 개선</details> |


