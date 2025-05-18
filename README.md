# 📄 Document Management System (DMS)

A microservices-based Document Management System (DMS) built using **Spring Boot 3.3.x**, **Spring Cloud**, **MongoDB**, and **Eureka**. The system is modular and supports uploading documents, user authentication, and service discovery.

---

## 🧩 Microservices

| Service Name        | Description                             | Port (Default) |
|---------------------|-----------------------------------------|----------------|
| `document-service`  | Handles document upload & management    | `8081`         |
| `user-service`      | Handles user authentication and details | `8082`         |
| `eureka-server`     | Service discovery and registry          | `8761`         |
| `api-gateway`       | Central entry point (planned)           | `8080`         |

---

## ✅ Features

- REST APIs for document upload and retrieval
- JWT-based authentication (in progress)
- Centralized service discovery with Eureka
- MongoDB as the database for both services
- Environment-specific config handling

---

## 🐞 Bug Fixes and Compatibility Improvements

### ⚙️ Compatibility Fixes
- ❌ Fixed Spring Boot & Spring Cloud version mismatch:
  - Switched from Spring Boot `3.4.5`  to `3.3.0` 
  - Set `spring-cloud.version` to `2023.0.1`
  - Ensured compatibility with Netflix Eureka

### 📦 Servlet Dependency Fix
- ❌ Error: `jakarta.servlet.http does not exist`
  - ➤ Fixed by explicitly adding:
    ```xml
    <dependency>
      <groupId>jakarta.servlet</groupId>
      <artifactId>jakarta.servlet-api</artifactId>
      <scope>provided</scope>
    </dependency>
    ```

### 🔁 Multi-Microservice Run
- Manually running each microservice was inefficient
  - ➤ Created a root-level `docker-compose.yml` (optional future improvement)
  - ➤ Currently using `Run Configurations` in IntelliJ for each module

---

## 🚀 How to Run

### 🔧 Prerequisites
- Java 17
- Maven 3.8+
- MongoDB running locally or Atlas

### 📦 Running Each Service
1. **Start Eureka Server**
   ```bash
   cd eureka-server
   mvn spring-boot:run


#### ✅ Start Document Service
```bash
cd document-service
mvn spring-boot:run
