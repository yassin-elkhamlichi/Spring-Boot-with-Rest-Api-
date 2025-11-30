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
* **Dual Token Architecture:** Implemented **Access Tokens** (short-lived) and **Refresh Tokens** (long-lived) to balance security and user experience.
* **Stateless Authentication:** Using Spring Security & JWT.
* **Role-Based Access Control (RBAC):** Distinct access levels for `ADMIN` and `USER`.

**Authentication Flow:**

![Authentication Flow](https://github.com/yassin-elkhamlichi/Spring-Boot-with-Rest-Api-/blob/main/JwtWrokFlow.svg)

### 🏗️ Architecture & Design

* **Feature-Based Packaging:** Code is organized by domain features (e.g., `user`, `product`, `order`) rather than technical layers, improving scalability and maintainability.

<!-- end list -->

```text
📦 src/main/java/com/yassine/ecommerce
 ┣ 📂 auth           <-- Feature: Authentication
 ┣ 📂 product        <-- Feature: Product Management
 ┣ 📂 order          <-- Feature: Order Processing
 ┣ 📂 shared         <-- Shared Utilities
 ┗ 📜 EcommerceApplication.java
```

* **DTO Pattern:** Strict separation between Database Entities and API responses using **MapStruct** for high-performance, type-safe mapping.
* **Global Exception Handling:** Centralized `@ControllerAdvice` to capture runtime errors and return standardized, user-friendly JSON responses.

### 💾 Data & Persistence & Performance

* **Database Version Control:** Integrated **Flyway** to manage database schema changes and versioning, ensuring consistency across environments.
* **Performance Optimization:** solved the **N+1 Select Problem** using **Spring Data JPA @EntityGraph** to optimize fetching strategies (Eager vs Lazy) dynamically.
* **Complex Relationships:** Optimized handling of `@ManyToMany` and `@OneToMany` associations.
* **Data Integrity:** Custom **JPQL Queries** and **Specifications** for advanced filtering.

**Database Schema:**

![Database Schema](https://github.com/yassin-elkhamlichi/Spring-Boot-with-Rest-Api-/blob/main/DatabaseSchema.svg)


### 💰 Transactions

* **Payment Integration:** Integrated Payment Gateway simulation to handle secure financial transactions.
* **Validation:** Strict input validation using `@Valid` and **Custom Annotations** to ensure business logic integrity.

### ✅ Validation & Error Handling
* **Robust Validation:** utilized **Jakarta Validation (`@Valid`)** alongside **Custom Annotations** to enforce strict business rules (e.g., password complexity, unique emails).
* **Global Exception Handler:** A centralized `@ControllerAdvice` component that intercepts exceptions (like `MethodArgumentNotValidException` or custom `ResourceNotFoundException`) and returns standardized, user-friendly JSON error responses.

-----

## 🛠️ Tech Stack

* **Core:** Java 17, Spring Boot 3.x
* **Database:** MySQL, Spring Data JPA (Hibernate)
* **Migrations:** Flyway
* **Security:** Spring Security, JWT
* **Mapping:** MapStruct
* **Documentation:** Swagger UI (OpenAPI 3.0)
* **Testing:** Postman (Automation & Collections)
* **Build Tool:** Maven

-----

## 🧪 Testing & QA (Postman)

Reliability is verified through an automated **Postman Collection**.

* **Collections:** API endpoints are grouped by flow (Auth, Product, Order).
* **Environments:** Configured for `DEV` and `PROD` switching.
* **Automation:** Pre-request scripts handle Token generation, and Test scripts validate:
    * Status Codes (200, 201, 403).
    * Response Body Structure.
    * Business Logic constraints.

-----

## 📚 API Documentation

The API is fully documented using **Swagger/OpenAPI**.
Once the application is running, access the interactive UI at:

```
http://localhost:8080/swagger-ui.html
```

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
----
## 🎮 Live Demo & How to Test

The API is deployed and accessible via Swagger UI. You can test endpoints directly without installing anything.

**🔗 Live URL:** [https://store-api-production-fc3d.up.railway.app/swagger-ui/index.html](https://store-api-production-fc3d.up.railway.app/swagger-ui/index.html)
<<<<<<< HEAD
> **⚠️ Note regarding Live Demo:** > The application is hosted on a free cloud tier (Railway).
=======
> **⚠️ Note regarding Live Demo:** > The application is hosted on a free cloud tier (Railway). 
> 1. The initial request might take **30-60 seconds** to wake up the server (Cold Start).
> 2. If the link is inaccessible, the free trial credits may have expired.
### 🔐 How to Authenticate (Step-by-Step)

Most endpoints (like creating products or placing orders) are secured. Follow these steps to access them:

1.  **Login:**
    * Go to the `Auth-Controller` section.
    * Open `POST /api/v1/auth/login`.
    * Click **Try it out**.
    * Use these demo credentials (or register a new user):
        ```json
        {
          "email": "yassine@example.com",  // Or your created user
          "password": "45454545"      // Or your password
        }
        ```
    * Click **Execute**.

2.  **Copy Token:**
    * In the response body, copy the `access_token` string (without quotes).

3.  **Authorize:**
    * Scroll to the top of the page and click the **Authorize 🔓** button.
    * Paste the token in the value box.
    * Click **Authorize** then **Close**.

4.  **Test Secure Endpoints:**
    * Now the lock icon 🔒 is closed. You can test any secured endpoint (e.g., `POST /products` or `GET /orders`).

### 🧪 Test Credentials
| Role | Email | Password | Access |
| :--- | :--- | :--- | :--- |
| **Admin** | `yassine45@example.com` | `45454545` | Full Access (Manage Products, Users) |
| **User** | `user@example.com` | `45454545` | Shop, Cart, Place Orders,show Products |
-----

## 📬 Contact

**Yassine El Khamlichi**

* **Email:** yassinelkhamlichi98@gmail.com
* **LinkedIn:** [Yassine El Khamlichi](https://www.linkedin.com/in/yassinelkhamlichi)
