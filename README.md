# CertVerify — Digital Certificate Issuance and Public Verification System

## 1. Project Title
**CertVerify** — Digital Certificate Issuance and Public Verification System

---

## 2. Objective
Institutions and academic organizations issue course or event completion certificates as PDFs. However, digital credentials can be easily forged or tampered with. **CertVerify** solves this real-world security challenge by generating tamper-proof certificates with a globally unique verification code. Anyone (students, employers, or institutions) can publicly verify the validity and authenticity of any certificate in real-time without needing an account or login. Additionally, administrators can revoke fraudulent or canceled certificates, ensuring revoked certificates immediately fail future verification attempts.

---

## 3. Technologies Used
- **Backend Framework:** Spring Boot 4.1.1x (Java 17/21)
- **Data Persistence:** Spring Data JPA / Hibernate
- **Database:** MySQL 8.x
- **Bean Validation:** Jakarta Validation (`@Valid`, `@NotNull`, `@NotBlank`, `@Email`)
- **Web Layer:** Spring Web MVC (RESTful API architecture)
- **Build Tool:** Maven Wrapper (`mvnw` / `mvnw.cmd`)

---

## 4. Database Name
- **Database Name:** `certverify`
- **Host / Port:** `localhost:3306`
- **Tables Generated Automatically by JPA / Hibernate:**
  1. `participants`
  2. `courses`
  3. `certificates`
  4. `verification_records`

---

## 5. Entity Relationship Explanation
- **Participant (1) ---- (*) Certificate:** One participant can be issued multiple certificates across different courses. (One-to-Many / Many-to-One)
- **Course (1) ---- (*) Certificate:** One course can have many certificates issued to different participants. (One-to-Many / Many-to-One)
- **Certificate (1) ---- (*) VerificationRecord:** Each certificate can have multiple verification log entries tracking public verification attempts. (One-to-Many / Many-to-One)
- Note: If a verification attempt uses an invalid or non-existent code, a `VerificationRecord` is still logged with `certificate_id = NULL` and result `INVALID` for audit purposes.

---

## 6. Folder Structure
```
CertVerify/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           ├── CertVerify/
│   │   │           │   └── CertVerifyApplication.java
│   │   │           ├── controller/
│   │   │           │   ├── CertificateController.java
│   │   │           │   ├── CourseController.java
│   │   │           │   └── ParticipantController.java
│   │   │           ├── dto/
│   │   │           │   └── IssueCertificateRequest.java
│   │   │           ├── exception/
│   │   │           │   ├── DuplicateResourceException.java
│   │   │           │   ├── GlobalExceptionHandler.java
│   │   │           │   └── ResourceNotFoundException.java
│   │   │           ├── models/
│   │   │           │   ├── Certificate.java
│   │   │           │   ├── Course.java
│   │   │           │   ├── Participant.java
│   │   │           │   └── VerificationRecord.java
│   │   │           ├── repository/
│   │   │           │   ├── CertificateRepository.java
│   │   │           │   ├── CourseRepository.java
│   │   │           │   ├── ParticipantRepository.java
│   │   │           │   └── VerificationRecordRepository.java
│   │   │           └── services/
│   │   │               ├── CertificateService.java
│   │   │               ├── CourseService.java
│   │   │               └── ParticipantService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── CertVerify/
│                       └── CertVerifyApplicationTests.java
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

## 7. REST API List

### A. Participant Endpoints (`/api/participants`)
| Method | Endpoint | Description | Status Code |
|---|---|---|---|
| `POST` | `/api/participants` | Create a new participant | `201 Created` |
| `GET` | `/api/participants` | Get all participants | `200 OK` |
| `GET` | `/api/participants/{id}` | Get participant by ID | `200 OK` / `404 Not Found` |
| `PUT` | `/api/participants/{id}` | Update participant details | `200 OK` / `404 Not Found` |
| `DELETE` | `/api/participants/{id}` | Delete participant | `200 OK` / `404 Not Found` |

### B. Course Endpoints (`/api/courses`)
| Method | Endpoint | Description | Status Code |
|---|---|---|---|
| `POST` | `/api/courses` | Create a new course | `201 Created` |
| `GET` | `/api/courses` | Get all courses | `200 OK` |
| `GET` | `/api/courses/{id}` | Get course by ID | `200 OK` / `404 Not Found` |
| `PUT` | `/api/courses/{id}` | Update course details | `200 OK` / `404 Not Found` |
| `DELETE` | `/api/courses/{id}` | Delete course | `200 OK` / `404 Not Found` |

### C. Certificate Endpoints (`/api/certificates`)
| Method | Endpoint | Description | Status Code |
|---|---|---|---|
| `POST` | `/api/certificates` | Issue a certificate to a participant for a course | `201 Created` |
| `GET` | `/api/certificates` | Get all issued certificates | `200 OK` |
| `GET` | `/api/certificates/{id}` | Get certificate by ID | `200 OK` / `404 Not Found` |
| `GET` | `/api/certificates/verify/{code}` | **Public verification** using unique verification code (No login required) | `200 OK` |
| `PUT` | `/api/certificates/{id}/revoke` | Revoke a certificate by ID | `200 OK` / `404 Not Found` |
| `GET` | `/api/certificates/statistics/course/{courseId}` | View certificate count per course | `200 OK` / `404 Not Found` |

---

## 8. MySQL Database Setup
Open MySQL Command Line Client or MySQL Workbench and run:
```sql
CREATE DATABASE IF NOT EXISTS certverify;
USE certverify;
```

In `src/main/resources/application.properties`, configure credentials:
```properties
spring.application.name=CertVerify

spring.datasource.url=jdbc:mysql://localhost:3306/certverify?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8080
```
> **Note:** Hibernate automatically creates all tables (`participants`, `courses`, `certificates`, `verification_records`). No manual `CREATE TABLE` scripts needed!

---

## 9. How to Run the Application
In your project terminal:
```powershell
# Using Maven Wrapper (Windows)
.\mvnw.cmd clean compile
.\mvnw.cmd spring-boot:run

# Or package into a JAR:
.\mvnw.cmd clean package
java -jar target/CertVerify-0.0.1-SNAPSHOT.jar
```
The server starts on `http://localhost:8080`.

---

## 10. Postman Testing Steps & Order

Follow this exact test execution sequence in Postman or cURL:

### Step 1: Create Participant
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/participants`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
  "name": "Arun Kumar",
  "email": "arun@gmail.com",
  "phone": "9876543210"
}
```
- **Expected Status:** `201 Created`

### Step 2: Create Course
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/courses`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
  "courseName": "Java Full Stack Development",
  "description": "Spring Boot and REST API Development"
}
```
- **Expected Status:** `201 Created`

### Step 3: Issue Certificate
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/certificates`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
  "participantId": 1,
  "courseId": 1
}
```
- **Expected Status:** `201 Created`
- Copy the `verificationCode` from the response (e.g., `80D8C70D1C77`).

### Step 4: Verify Active Certificate (Public)
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/certificates/verify/<COPIED_CODE>`
- **Expected Status:** `200 OK`
- **Response:**
```json
{
  "valid": true,
  "message": "Certificate is valid",
  "certificateId": 1,
  "holderName": "Arun Kumar",
  "course": "Java Full Stack Development",
  "issueDate": "2026-09-28",
  "status": "ACTIVE"
}
```

### Step 5: Verify Invalid Code
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/certificates/verify/INVALID123`
- **Expected Status:** `200 OK`
- **Response:**
```json
{
  "valid": false,
  "message": "Certificate is invalid"
}
```

### Step 6: Revoke Certificate
- **Method:** `PUT`
- **URL:** `http://localhost:8080/api/certificates/1/revoke`
- **Expected Status:** `200 OK`
- **Response:** Certificate with `status: "REVOKED"`

### Step 7: Verify Revoked Certificate Again (Critical Rule)
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/certificates/verify/<COPIED_CODE>`
- **Expected Status:** `200 OK`
- **Response:**
```json
{
  "valid": false,
  "message": "Certificate has been revoked"
}
```

### Step 8: View Course Statistics
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/certificates/statistics/course/1`
- **Expected Status:** `200 OK`
- **Response:**
```json
{
  "courseId": 1,
  "courseName": "Java Full Stack Development",
  "totalCertificates": 1
}
```

### Step 9: Test Not Found Error Handling (404)
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/certificates`
- **Body:**
```json
{
  "participantId": 9999,
  "courseId": 1
}
```
- **Expected Status:** `404 Not Found`
- **Response:** `{"error": "Participant not found with id: 9999"}`

### Step 10: Test Validation Error Handling (400)
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/certificates`
- **Body:**
```json
{
  "participantId": null,
  "courseId": null
}
```
- **Expected Status:** `400 Bad Request`
- **Response:**
```json
{
  "courseId": "Course ID is required",
  "participantId": "Participant ID is required"
}
```

---

## 11. Business Rules
1. **Rule 1 (Unique Verification Code):** Every certificate is automatically assigned a 12-character uppercase alphanumeric verification code (derived from UUID) that is globally unique.
2. **Rule 2 (Revocation Enforcement):** A revoked certificate (`status = REVOKED`) must ALWAYS return `valid: false` on all future verification calls.
3. **Rule 3 (Invalid Codes):** Non-existent verification codes return `valid: false` and a clean explanation message.
4. **Rule 4 (Audit Logging):** All verification attempts (VALID, INVALID, and INVALID - REVOKED) are persisted into `verification_records` with timestamps.
5. **Rule 5 (Resource Not Found Handling):** Looking up or referencing invalid IDs returns HTTP 404 with clean JSON error messages.
6. **Rule 6 (Validation Rules):** Request payloads are validated with Jakarta Validation (`@Valid`) returning HTTP 400 with field-specific errors.
7. **Rule 7 (Duplicate Prevention):** Prevents issuing duplicate certificates for the same participant and same course.

---

## 12. Sample JSON Requests and Responses

### Create Participant Request
```json
POST /api/participants
{
  "name": "Priya Sharma",
  "email": "priya@gmail.com",
  "phone": "9123456780"
}
```
**Response (`201 Created`):**
```json
{
  "id": 2,
  "name": "Priya Sharma",
  "email": "priya@gmail.com",
  "phone": "9123456780"
}
```

### Issue Certificate Request
```json
POST /api/certificates
{
  "participantId": 2,
  "courseId": 1
}
```
**Response (`201 Created`):**
```json
{
  "id": 2,
  "verificationCode": "62EB30642304",
  "issueDate": "2026-09-28",
  "status": "ACTIVE",
  "participant": {
    "id": 2,
    "name": "Priya Sharma",
    "email": "priya@gmail.com",
    "phone": "9123456780"
  },
  "course": {
    "id": 1,
    "courseName": "Java Full Stack Development",
    "description": "Spring Boot and REST API Development"
  }
}
```

### Verification Response (Active)
```json
GET /api/certificates/verify/62EB30642304
{
  "valid": true,
  "message": "Certificate is valid",
  "certificateId": 2,
  "holderName": "Priya Sharma",
  "course": "Java Full Stack Development",
  "issueDate": "2026-09-28",
  "status": "ACTIVE"
}
```

---

## 13. Thymeleaf Web UI Portal

CertVerify includes an institutional web portal with Bootstrap 5 and Thymeleaf.

### Frontend Web Routes:
| Web Route | Template | Description |
|---|---|---|
| `GET /` or `GET /dashboard` | `dashboard.html` | Real-time statistics, quick action triggers, public search, and recent certificates |
| `GET /participants` | `participants/list.html` | Interactive participant directory with search filter and quick management |
| `GET /participants/new` | `participants/form.html` | Register a new participant with validation feedback |
| `GET /participants/edit/{id}` | `participants/form.html` | Update participant details |
| `GET /courses` | `courses/list.html` | Curriculum directory showing certificate issuance count per course |
| `GET /courses/new` | `courses/form.html` | Create a new academic course / event |
| `GET /courses/edit/{id}` | `courses/form.html` | Edit course description |
| `GET /certificates` | `certificates/list.html` | Certificate ledger with verification code badges, status pills, and safe revoke modals |
| `GET /certificates/issue` | `certificates/issue.html` | Interactive certificate generation form with dynamic database dropdowns |
| `GET /certificates/{id}` | `certificates/details.html` | Academic certificate document view with formal border, official seal, and status stamps |
| `GET /verify` | `verification/verify.html` | Public certificate verification portal (open to anyone without login) |
| `GET /verify/result?code={code}` | `verification/result.html` | Verification result showing VALID, REVOKED, or NOT FOUND states |

