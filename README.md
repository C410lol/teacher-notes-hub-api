# Teacher Notes Hub — API

The REST API for **Teacher Notes Hub**, an application designed to organize school information and routines. The backend centralizes business rules and data persistence for the web interface, providing features to manage users, institutions, teachers, students, school notebooks, lessons, attendance, grades, and assignments.

The project was built with Java and Spring Boot, using a layered architecture to separate controllers, services, repositories, entities, and Data Transfer Objects (DTOs).

## ✨ Features

The API implements features including:

- Management of users, teachers, administrators, and institutions.
- Creation, retrieval, editing, and finalization of school notebooks.
- Student registration and organization.
- Management of lessons and related content.
- Recording and retrieval of student attendance.
- Management of grades and assignments.
- Student performance queries and identification of pending tasks.
- Features related to BNCC curriculum codes.
- Utilities for processing and generating spreadsheet files.
- Authentication and authorization with Spring Security and JWT.

The availability of each operation depends on the access rules implemented for the different user roles.

## 🧰 Technologies

- **Java 20**
- **Spring Boot 3**
- Spring Web
- Spring Data JPA
- Spring Security
- Bean Validation
- **JWT (JSON Web Tokens)**
- **PostgreSQL**
- Hibernate
- Apache POI
- Maven
- Lombok

## 🏗️ Application structure

The code is organized by responsibility:

- `controllers`: REST endpoints and HTTP request handling.
- `services`: business rules and operation orchestration.
- `repositories`: data access and persistence with Spring Data JPA.
- `models/entities`: entities persisted in the database.
- `models/dtos`: objects used for communication between the API and its clients.
- `configs`: general and security configurations.
- `utils`: helper utilities, including operations for handling school notebooks and spreadsheets.

## ▶️ How to run locally

### Prerequisites

- JDK 20.
- PostgreSQL.
- Git.

### 1. Clone the repository

```bash
git clone https://github.com/C410lol/teacher-notes-hub-api.git
cd teacher-notes-hub-api
```

### 2. Configure the database

Create a PostgreSQL database and configure the application's connection in:

```
src/main/resources/application.properties
```

Also check the security settings and the parameters required to generate and validate JWTs. Use your own values for your local environment and do not publish credentials or secret keys in the repository.

### 3. Run the application

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
mvnw.cmd spring-boot:run
```

### 4. Run the tests

```bash
./mvnw test
```

## 🔗 Related project

- [Teacher Notes Hub — Angular Frontend](https://github.com/C410lol/teacher-notes-hub-front)

---

This repository contains the backend of Teacher Notes Hub. To use the complete solution, also check out the frontend project.
