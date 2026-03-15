# Authify Backend – Authentication System

Authify is a secure authentication backend built using **Spring Boot** and **Spring Security**. It provides APIs for user authentication, authorization, and secure access control using **JWT (JSON Web Tokens)**.

This project demonstrates how to implement a production-style authentication system with a layered architecture including controllers, services, repositories, DTOs, filters, and exception handling.

---

## Features

- User registration and login
- JWT-based authentication
- Secure API endpoints using Spring Security
- Email integration using SMTP
- Environment-based configuration using profiles
- DTO and mapper layer for clean architecture
- Global exception handling
- Password encryption using BCrypt

---

## Tech Stack

- **Backend:** Spring Boot, Spring Security, Spring Data JPA, SMTP Email Service
- **Language:** Java
- **Security:** JWT Authentication, BCrypt Password Encoder
- **Database:** MySQL
- **Build Tool:** Maven
- **API Style:** RESTful APIs

---

## Project Structure

```text
src
└── main
    ├── java/com/project/authentication_system
    │   ├── config        → Security and application configuration
    │   ├── controller    → REST API endpoints
    │   ├── dto           → Data transfer objects
    │   ├── entity        → Database entities
    │   ├── exception     → Global exception handling
    │   ├── filter        → JWT authentication filters
    │   ├── mapper        → DTO ↔ Entity mapping
    │   ├── repository    → Database repositories
    │   ├── service       → Business logic
    │   └── AuthenticationSystemApplication.java
    │
    └── resources
        ├── application.properties
        └── application-dev.properties
```

---

## Authentication Flow

1. User registers an account
2. Password is encrypted using **BCrypt**
3. User logs in with credentials
4. Server generates a **JWT token**
5. Client sends JWT in request headers (typically `Authorization: Bearer <token>`)
6. Spring Security filter validates the token
7. Access is granted to protected APIs

---

## Getting Started

### 1️. Clone the repository

```bash
git clone https://github.com/rarestpreet/Authify_Backend.git
```

### 2️. Set environment variables

Configure the required environment variables:

- `DB_URL`
- `DB_USER`
- `DB_PASS`
- `SECRET_KEY`
- `MAIL_USER`
- `MAIL_PASS`

### 3️. Run the application

Run the main class:

- `AuthenticationSystemApplication.java`

Server will start at:

- `http://localhost:8080`

---

## Example API Endpoints

| Method | Endpoint         | Description           |
|-------:|------------------|-----------------------|
| POST   | `/auth/register` | Register new user     |
| POST   | `/auth/login`    | User login            |
| GET    | `/user/profile`  | Get user profile      |
| POST   | `/auth/logout`   | Logout user           |

> and more.

---

## Learning Objectives

This project demonstrates:

- Spring Security authentication
- JWT token generation and validation
- Secure REST API development
- Layered backend architecture (Controller → Service → Repository + DTO/Mapper)
- Exception handling and validation

---

## Future Improvements

- Refresh token support
- OAuth2 / Google login
- Role-based authorization (Admin/User)
- Docker containerization
- API documentation using Swagger / OpenAPI
