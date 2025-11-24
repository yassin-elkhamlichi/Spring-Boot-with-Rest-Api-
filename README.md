# 🛒 E-Commerce REST API Backend

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0-green)
![Security](https://img.shields.io/badge/Spring_Security-JWT-red)
![Status](https://img.shields.io/badge/Build-Passing-brightgreen)

## 📖 Overview
A robust, production-ready RESTful API designed for a scalable E-Commerce platform. This project goes beyond basic CRUD operations by implementing a **Feature-Based Architecture**, advanced security with **JWT**, and automated testing workflows.

It manages the complete shopping lifecycle: **User Registration → Product Discovery → Cart Management → Secure Checkout → Order Processing.**

## 🚀 Key Features

### 🛡️ Security & Authentication
* **Stateless Authentication:** Implemented using **Spring Security** and **JWT (JSON Web Tokens)**.
* **Custom Filters:** `OncePerRequestFilter` utilized to intercept requests and validate tokens before hitting the security context.
* **Role-Based Access Control (RBAC):** Distinct access levels for `ADMIN` (Product management) and `USER` (Shopping).

sequenceDiagram
    participant User
    participant Filter as JwtAuthFilter
    participant Controller as AuthController
    participant Service as UserService
    participant DB as MySQL Database

    User->>Controller: POST /api/auth/login (username, password)
    Controller->>Service: authenticate(username, password)
    Service->>DB: findByUsername()
    DB-->>Service: UserDetails
    Service-->>Controller: Return User & Generate JWT
    Controller-->>User: HTTP 200 OK (Access Token)

    Note over User, Filter: Subsequent Requests
    User->>Filter: GET /api/orders (Header: Bearer Token)
    Filter->>Filter: validateToken()
    Filter->>Controller: Forward Request (SecurityContext Set)
    Controller->>DB: Fetch Data
    DB-->>Controller: Order Data
    Controller-->>User: HTTP 200 OK (JSON)
    
### 🏗️ Architecture & Design
* **Feature-Based Packaging:** Code is organized by domain features (e.g., `user`, `product`, `order`) rather than technical layers, improving scalability and maintainability.
* **DTO Pattern:** Strict separation between Database Entities and API responses using **MapStruct** for high-performance, type-safe mapping.
* **Global Exception Handling:** Centralized `@ControllerAdvice` to capture runtime errors and return standardized, user-friendly JSON responses.

### 💾 Data & Persistence
* **Complex Relationships:** Optimized handling of `@ManyToMany` (Products ↔ Orders) and `@OneToMany` relationships.
* **Performance:** Implemented **Pagination and Sorting** for product catalogs to handle large datasets efficiently.
* **Data Integrity:** Custom **JPQL Queries** and **Specifications** for advanced filtering.

### 💰 Transactions
* **Payment Integration:** Integrated Payment Gateway simulation to handle secure financial transactions.
* **Validation:** Strict input validation using `@Valid` and **Custom Annotations** to ensure business logic integrity.

---

## 🛠️ Tech Stack

* **Core:** Java 17, Spring Boot 3.x
* **Database:** MySQL, Spring Data JPA (Hibernate)
* **Security:** Spring Security, JWT
* **Mapping:** MapStruct
* **Documentation:** Swagger UI (OpenAPI 3.0)
* **Testing:** Postman (Automation & Collections), JUnit 5
* **Build Tool:** Maven

---

## 🧪 Testing & QA (Postman)

Reliability is verified through an automated **Postman Collection**.

* **Collections:** API endpoints are grouped by flow (Auth, Product, Order).
* **Environments:** Configured for `DEV` and `PROD` switching.
* **Automation:** Pre-request scripts handle Token generation, and Test scripts validate:
    * Status Codes (200, 201, 403).
    * Response Body Structure.
    * Business Logic constraints.

---

## 📚 API Documentation

The API is fully documented using **Swagger/OpenAPI**.
Once the application is running, access the interactive UI at:

```

http://localhost:8080/swagger-ui.html

````

## ⚙️ Installation & Setup

1.  **Clone the repository**
    ```bash
    git clone [https://github.com/yassin-elkhamlichi/Spring-Boot-with-Rest-Api-.git](https://github.com/yassin-elkhamlichi/Spring-Boot-with-Rest-Api-.git)
    ```
2.  **Configure Database**
    * Update `src/main/resources/application.properties` with your MySQL credentials.
3.  **Run the Application**
    ```bash
    mvn spring-boot:run
    ```

---

## 📬 Contact
**Yassine El Khamlichi**
* **Email:** yassinelkhamlichi98@gmail.com
* **LinkedIn:** yassinelkhamlichi
````
