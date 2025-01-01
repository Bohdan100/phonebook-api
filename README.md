# Phonebook Application
The **Phonebook Application** is a demonstration project that provides a simple implementation of a digital phonebook for managing contacts. It showcases modern development practices using Spring Boot and integrates robust database management and testing tools.

## Features
- Add, update, view, and delete contacts.
- Persist contact information using PostgreSQL.
- Handle database migrations with Flyway.
- Simplify setup and build processes with Gradle.
- Streamline code using Lombok to reduce boilerplate.
- Ensure code quality and correctness with JUnit 5 tests.

## Requirements
The application is built using the following technologies:
- **Spring Boot**: 3.3.5
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
       CREATE USER admin WITH PASSWORD 'secret1234';
       ```

    2. Create a new database:
       ```sql
       CREATE DATABASE phonebook;
       ```

    3. Assign ownership of the database to the new user:
       ```sql
       ALTER DATABASE phonebook OWNER TO admin;
       ```

    4. Connect to the newly created database:
       ```sql
       \c phonebook
       ```

    5. Grant schema usage and creation privileges to the user:
       ```sql
       GRANT USAGE ON SCHEMA public TO admin;
       GRANT CREATE ON SCHEMA public TO admin;
       ```

    6. Grant all privileges on tables and sequences in the schema:
       ```sql
       GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO admin;
       GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO admin;
       ```

    7. Configure default privileges for new tables and sequences:
       ```sql
       ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO admin;
       ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO admin;
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