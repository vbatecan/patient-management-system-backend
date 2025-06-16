# Patient Management System

## Table of Contents

- [About the Project](#about-the-project)

- [Features](#features)

- [Technologies Used](#technologies-used)

- [Prerequisites](#prerequisites)

- [Getting Started](#getting-started)

    - [1. Clone the Repository](#1-clone-the-repository)

    - [2. Database Setup](#2-database-setup)

    - [3. Building the Project](#3-building-the-project)

    - [4. Running the Application](#4-running-the-application)

- [Configuration](#configuration)

- [API Endpoints](#api-endpoints)

- [Indexing](#indexing)

## About the Project

The Patient Management System is a comprehensive Java-based application designed to manage various aspects of a healthcare facility's operations. It facilitates the handling of patient information, appointments, medical records, billing, and user accounts, all secured with a robust authentication mechanism. The system also incorporates an indexing service for efficient data searching and retrieval.

## Features

* **Patient Management:** CRUD operations for patient data (via `PatientDTO`, `PatientFilter`).

* **Appointment Scheduling:** Management of `Appointment` entities.

* **Medical Records Management:** Handling of `MedicalRecord` entities and `MedicalRecordDTO`, with filtering
  capabilities (`MedicalRecordFilter`).

* **Billing System:** Management of billing information (via `BillingDTO`, `BillingFilter`).

* **User Account Management:** Secure user registration, login, and management (via `UserAccountDTO`,
  `UserAccountInput`, `UserAccountFilter`, `UserAccount` entity).

* **Authentication & Authorization:** JWT-based security (via `JwtService`, `AuthenticationInput`,
  `SuccessfulLoginResponse`).

* **Data Filtering:** Advanced filtering capabilities for various entities (e.g., `DoctorFilter`, `PrescriptionFilter`).

* **Data Indexing:** Efficient search functionality powered by an `IndexingService`.
* **RBAC**: Role-based access control (RBAC) for user permissions and roles.

## Technologies Used

* **Java:** Core programming language.

* **Spring Boot:** Framework for building the application (likely including Spring MVC for REST APIs, Spring Data JPA
  for database interaction, Spring Security for authentication/authorization).

* **JPA (Hibernate):** For Object-Relational Mapping (ORM) to interact with the database.

* **JWT (JSON Web Tokens):** Used for securing APIs and managing user sessions (`JwtService`).

* **Indexing Engine:** An underlying indexing technology (e.g., Hibernate Search with Lucene, or Elasticsearch) used by
  `IndexingService`.

* **Build Tool:** Apache Maven or Gradle (commands below assume Maven).

* **Database:** A relational database (e.g., H2, PostgreSQL, MySQL, SQL Server). Configuration is externalized.

## Prerequisites

Before you begin, ensure you have the following installed on your system:

* Java Development Kit (JDK) (e.g., JDK 11, 17, or as specified by the project).

* Apache Maven (e.g., 3.6+) or Gradle.

* A relational database server (unless using an embedded one like H2 for development).

* Git for cloning the repository.

## Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing
purposes.

### 1. Clone the Repository
```bash
git clone https://github.com/vbatecan/patient-management-system-backend.git

cd patient-management-system-backend
```

### 2. Database Setup

This project requires a database. The configuration is typically found in `src/main/resources/application.properties` or
`src/main/resources/application.yml`.

**a. Configure Database Connection:**

Open the `application.properties` (or `.yml`) file and update the datasource properties to point to your database.

Example for **PostgreSQL**:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```
Example for **H2 (In-Memory Database for development)**:
```properties
spring.datasource.url=jdbc:h2:mem:pmsdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```
**b. Database Schema and Initialization:**

* **Schema Generation:** Spring Boot can automatically create/update the database schema based on your JPA entities if
  `spring.jpa.hibernate.ddl-auto` property is set appropriately (e.g., `update`, `create`, `create-drop`). For
  production, it's recommended to use database migration tools like Flyway  
  or Liquibase.

  ```properties                                                                                                                                                                                                                                                                                               
                                                                                                                                                                                                                                                                                                              
  spring.jpa.hibernate.ddl-auto=update # Use 'validate' or manage with migrations in production                                                                                                                                                                                                               
                                                                                                                                                                                                                                                                                                              
  ```                                                                                                                                                                                                                                                                                                         

* **Initial Data:** If the project uses a `data.sql` file in `src/main/resources/`, Spring Boot will execute it on
  startup to populate initial data (ensure `spring.jpa.defer-datasource-initialization=true` if `ddl-auto` is `create`
  or `create-drop` and you depend on schema generation before data      
  loading).

### 3. Building the Project

Navigate to the root directory of the project in your terminal and use Maven (or Gradle) to build the project. This will
compile the code, run tests, and package the application.

Using Maven:

`mvn clean install`

Using Gradle:

`./gradlew clean build`

### 4. Running the Application

Once the build is successful, you can run the Spring Boot application.

Using Maven:

`mvn spring-boot:run`

Or, run the packaged JAR file (typically found in the `target` directory):

`java -jar target/your-application-name.jar`

(Replace `your-application-name.jar` with the actual name of the JAR file generated by the build).

The application will start, usually on an embedded Tomcat server (default port 8080, unless configured otherwise). You
should see logs indicating the application has started successfully.

## Configuration

Key application configurations are typically managed in `src/main/resources/application.properties` or
`src/main/resources/application.yml`. This includes:

* Database connection details (`spring.datasource.*`).

* Server port (`server.port`).

* JWT secret key and expiration time (custom properties used by `JwtService`).

* Logging levels.

* Configuration for the `IndexingService`.

## API Endpoints

The application exposes RESTful APIs for its various functionalities. While specific endpoint details are found within
the controller classes (likely under a package like `com.vbatecan.patient_management_system.controller`), common base
paths might include:

* `/auth` for authentication (login, registration).

* `/api/patients` for patient management.

* `/api/appointments` for appointment scheduling.

* `/api/medical-records` for medical records.

* `/api/billing` for billing operations.

Consider using tools like Postman or Swagger UI (if integrated) to explore and test the APIs.

## Indexing

The `IndexingService` is responsible for creating and maintaining search indexes for entities like `Appointment`,
`MedicalRecord`, and `UserAccount`.

* **Initial Indexing:** This service may run on application startup or require a manual trigger to build the initial
  indexes from the database.

* **Real-time Updates:** Entities are likely indexed automatically upon creation, update, or deletion to keep the search
  results consistent.

* Consult the `IndexingService.java` file and any related configuration for more details on how indexing is managed.
                                                                                                                                                                                                                                                                                                                
---                                                                                                                                                                                                                                                                                                             

This README provides a general overview and setup guide. For more specific details, refer to the project's source code
and any additional documentation that may be available.