# Phonebook Application
The **Phonebook Application** is a Spring Boot application written in Kotlin that provides RESTful API endpoints for managing contacts in a digital phonebook. The application features its own user 
authentication mechanism utilizing tokens and session identifiers. The AuthAPI includes endpoints for user registration, login, and logout. Users can create, edit, or delete contacts and search for them based on various criteria. All data is securely stored in a PostgreSQL database.

## Features
- Authentication API powered by **Spring Security**, featuring a custom **JWT filter**.
- Secure, token-based authentication implemented using **JWT (JSON Web Tokens)**.
- Session management with session expiration checks, integrated via **cookies**.
- CRUD operations for contacts (add, update, view, and delete) available through RESTful API endpoints.
- Structured and organized data storage backed by **PostgreSQL**.
- DEfficient database versioning and migrations with **Flyway**.
- Reduced boilerplate code through the use of **Lombok**.
- Streamlined setup and build processes using **Gradle**.
- Ensure code quality and correctness with JUnit 5 tests.

## Requirements
The application is built using the following technologies:
- **Spring Boot**: 3.3.5
- **Kotlin**: 1.9.25
- **Java Platform (JDK)**: 21
- **PostgreSQL**: 16.6
- **Flyway**: 11.0.1
- **Lombok**: 1.18.36
- **JUnit**: 5
- **Gradle**: 8.8

## Database Setup
Before running the application, follow these steps to set up the database:

1. **Create a PostgreSQL 16 Database**  
   Set up a PostgreSQL database to store the application’s data.

2. **Configure Database and User**  
   Perform the following steps in your PostgreSQL instance to create a user and database for the application:

    1. Create a new user with a password:
       ```sql
       CREATE USER IF NOT EXISTS admin WITH PASSWORD 'secret1234';
       ```

    2. Create a new database and 
       assign ownership of the database to the new user:
       ```sql
       CREATE DATABASE phonebook ENCODING 'UTF8' OWNER admin;
       ```

3. **Connect to the Database**  
   To connect to the `phonebook` database as the `admin` user, use the following command in the terminal:
   ```bash
   psql -U admin -d phonebook

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/Bohdan100/phonebook-api
   cd phonebook

2. Build and run the application in terminal using Gradle:
   ```bash
   .\gradlew bootRun     (Windows)
   ./gradlew bootRun     (Linux)

3. Build and run the application in terminal using JAR file:
   ```bash
   .\gradlew bootJar     (Windows)
   ./gradlew bootJar     (Linux)
   
   java -jar phonebook-api.jar

4. Make your HTTPS requests using the following endpoints (e.g., via **Postman**):
    - **Register**: [http://localhost:8080/api/v1/auth/register](http://localhost:8080/api/v1/auth/register)
    - **Login**: [http://localhost:8080/api/v1/auth/login](http://localhost:8080/api/v1/auth/login)
    - **Logout**: [http://localhost:8080/api/v1/auth/logout](http://localhost:8080/api/v1/auth/logout)
    - **Contacts** (Requires a Bearer Token in the Authorization header and a valid "SESSION_ID" cookie):
        - Get contact list by user id: [http://localhost:8080/api/v1/phonebooks/contacts/user/{id}](http://localhost:8080/api/v1/phonebooks/contacts/user/{id})
        - Get by id: [http://localhost:8080/api/v1/phonebooks/contacts/{id}](http://localhost:8080/api/v1/phonebooks/contacts/{id})
        - Get by number: [http://localhost:8080/api/v1/phonebooks/contacts/number/{fullNumber}](http://localhost:8080/api/v1/phonebooks/contacts/number/{fullNumber})
        - Create: [http://localhost:8080/api/v1/phonebooks/contacts](http://localhost:8080/api/v1/phonebooks/contacts)
        - Update: [http://localhost:8080/api/v1/phonebooks/contacts/{id}](http://localhost:8080/api/v1/phonebooks/contacts/{id})
        - Delete: [http://localhost:8080/api/v1/phonebooks/contacts/{id}](http://localhost:8080/api/v1/phonebooks/contacts/{id})
    - **Users**:
        - Get by id: [http://localhost:8080/api/v1/phonebooks/users/{id}](http://localhost:8080/api/v1/phonebooks/users/{id})
        - Get by name: [http://localhost:8080/api/v1/phonebooks/users/names/{name}](http://localhost:8080/api/v1/phonebooks/users/names/{name})
        - Get list: [http://localhost:8080/api/v1/phonebooks/users/owners](http://localhost:8080/api/v1/phonebooks/users/owners)
        - Update by id: [http://localhost:8080/api/v1/phonebooks/users/{id}](http://localhost:8080/api/v1/phonebooks/users/{id})
        - Delete by id: [http://localhost:8080/api/v1/phonebooks/users/{id}](http://localhost:8080/api/v1/phonebooks/users/{id})