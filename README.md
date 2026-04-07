# Phonebook Application

The **Phonebook Application** is a **RESTful API** service developed with **Kotlin** and the **Spring Boot** framework,
designed for efficient contact management in a digital phonebook. Containerized using **Docker** for
consistent deployment across development, testing, and production environments, the platform provides secure user
authentication, comprehensive contact management, and advanced data analytics.

The application leverages **PostgreSQL** as its primary relational database for structured, ACID-compliant data storage,
while implementing **session-based authentication** via secure HTTP-only cookies. Through its comprehensive RESTful API,
users can manage personal contacts, search by name or phone number, and access detailed analytics. The API features *
*pagination** across all collection endpoints for efficient data loading, **aggregate functions** for advanced
statistics, and **database indexing** for optimal query performance.

## Features

- **Authentication API** powered by **Spring Security** with a custom **session-based filter**, featuring secure user
  registration, login, and logout using HTTP-only cookies.
- **Secure Session-Based Authentication** implemented with UUID session identifiers stored in `SESSION_ID` cookie,
  including automatic session expiration (30 minutes) and cleanup mechanisms.
- **Pagination Support:** All collection endpoints support pagination with customizable page size, page number, and
  sorting parameters for optimal data loading (`getAllContactsOfUser`, `getContactByNumber`, `getAllOwnersOfPhonebooks`,
  `getUserByName`).
- **Aggregate Functions & Analytics:** Advanced SQL aggregation queries for business intelligence:
    - Total contacts count per user (`COUNT(*)`);
    - Unique phone numbers statistics (`COUNT(DISTINCT number)`);
    - Name and number length analytics (`MIN`, `MAX`, `AVG`);
    - User statistics by role (`GROUP BY`);
    - Active session tracking (`COUNT` with `WHERE` conditions);
- **Database Performance Optimization:** Strategic indexing on frequently queried columns:
    - `idx_users_session_id` - fast session lookup;
    - `idx_contacts_phonebook_id` - optimized JOIN operations;
    - `idx_contacts_name` - quick contact search by name;
    - `idx_contacts_number` - efficient phone number lookup;
- **Audit Trail System:** Automated tracking of user registration and deletion operations stored in dedicated
  `user_audit` table for compliance and monitoring purposes.
- **Containerized Deployment with Docker:** Seamlessly build and run the entire ecosystem — including the Spring Boot
  application and PostgreSQL database — using Docker and Docker Compose for consistent, "one-command" environment setup.
- **CRUD operations** available through RESTful API endpoints for the following collections:
    - Contacts
    - Users
- **Database Versioning:** Automated schema migrations and version control using **Flyway**.
- **Modern Development with Kotlin:** Leverage Kotlin's concise syntax, null safety, and data classes to enhance code
  readability, reduce boilerplate, and improve maintainability.
- Streamlined setup and build processes using **Gradle**.

## Requirements

The following configurations are required to launch the project:

- **Java Platform (JDK)**: 25
- **Gradle**: 9.2.0
- **Spring Boot**: 4.0.2
- **Docker**: 29.1.3
- **Kotlin**: 2.3.0

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/Bohdan100/phonebook-api
   cd phonebook-api

2. Build and Run the Application Using Docker in Terminal:
   ```bash
   docker-compose up -d --build app

3. Make your HTTPS requests using the following endpoints (e.g., via **Postman**), for authentification:

### 1. Authentication Endpoints

- **Register**: [http://localhost:8080/api/v1/auth/register](http://localhost:8080/api/v1/auth/register)
    - `POST`: Registers a new user. Returns a `SESSION_ID` cookie.
   Admin Registration Request Body:
```json lines
    {
      "name": "Admin", 
      "email": "admin@example.com",
      "password": "secret12345678",
      "role": "ADMIN"
     }
  ```

  User Registration Request Body:
  ```json lines
    {
     "name": "User",
     "email": "user@example.com",
     "password": "secret123456"
     }
  ```

- **Login**: [http://localhost:8080/api/v1/auth/login](http://localhost:8080/api/v1/auth/login)
    - `POST`: Logs in an existing user. Returns a `SESSION_ID` cookie.
  ```json lines
    {
      "email": "user@example.com",
      "password": "secret123456"
     }
  ```

- **Logout**: [http://localhost:8080/api/v1/auth/logout](http://localhost:8080/api/v1/auth/logout)
    - `POST`: Logs out the current user. Requires valid `SESSION_ID` cookie.

---

#### Important:

#### After successful authentication, you receive a valid `SESSION_ID` cookie (HTTP-only, expires after 30 minutes). Include this cookie automatically for all subsequent requests.

---

### 2. Contacts Endpoints

- **Get All User Contacts with Pagination**: [http://localhost:8080/api/v1/phonebooks/contacts/user/{id}?page=0&size=15&sort=name,asc](http://localhost:8080/api/v1/phonebooks/contacts/user/{id}?page=0&size=15&sort=name,asc)
    - `GET`: Retrieves all contacts belonging to the authenticated user with pagination support.
    - Parameters:
      - `id` - User ID (must match authenticated user), for example "id": "1"
      - `page` - Page number (default: 0)
      - `size` - Items per page (default: 15)
      - `sort` - Sort field and direction, e.g., name,asc or number,desc

- **Get Contact by ID**: [http://localhost:8080/api/v1/phonebooks/contacts/{id}](http://localhost:8080/api/v1/phonebooks/contacts/{id})
    - `GET`: Retrieves a single contact by its ID, for example "id": "4".
    - **Authorization**: User must own the contact.

- **Search Contacts by Number with Pagination**: [http://localhost:8080/api/v1/phonebooks/contacts/number/{number}?page=0&size=15&sort=name,asc](http://localhost:8080/api/v1/phonebooks/contacts/number/{number}?page=0&size=15&sort=name,asc)
    - `GET`: Retrieves contacts containing the specified sequence of digits or characters in their phone number (e.g., "+7" or "345").
    - Parameters:
      - `number` - Phone number to search (partial match supported)
      - `page` - Page number (default: 0)
      - `size` - Items per page (default: 15)
      - `sort` - Sort field and direction

  - **Create Contact**: [http://localhost:8080/api/v1/phonebooks/contacts](http://localhost:8080/api/v1/phonebooks/contacts)
      - `POST`: Adds a new contact to the user's phonebook. Requires a JSON body with the following structure:
    ```json lines
      {
        "name": "Steve Smith",
        "number": "+74656543210"
      }
    ```

- **Update Contact**: [http://localhost:8080/api/v1/phonebooks/contacts/{id}](http://localhost:8080/api/v1/phonebooks/contacts/{id})
    - `PUT`: Updates an existing contact by ID, for example "id": "4". Requires a JSON body with the following structure:
    ```json lines
      {
        "name": "Steve Smith job",
        "number": "+79876543210"
      }
    ```

- **Delete Contact**: [http://localhost:8080/api/v1/phonebooks/contacts/{id}](http://localhost:8080/api/v1/phonebooks/contacts/{id})
    - `DELETE`: Removes a contact from the phonebook by ID, for example "id": "10".

- **Contact Statistics Endpoints (Aggregate Functions)**
- **Get Total Contacts Count**: [http://localhost:8080/api/v1/phonebooks/contacts/statistics/total?userId=3](http://localhost:8080/api/v1/phonebooks/contacts/statistics/total?userId=3)
    - `GET`: Returns total number of contacts for a specific user, for example with "id": "3".
    - **Response**: `{ "totalContacts": 45 }`.

- **Get Unique Numbers Count**: [http://localhost:8080/api/v1/phonebooks/contacts/statistics/unique?userId=3](http://localhost:8080/api/v1/phonebooks/contacts/statistics/unique?userId=3)
    - `GET`: Returns count of unique phone numbers (without duplicates) for a specific user, for example with "id": "3".
    - **Response**: `{ "uniqueNumbers": 42 }`.

- **Get Full Contact Statistics**: [http://localhost:8080/api/v1/phonebooks/contacts/statistics/full?userId=4](http://localhost:8080/api/v1/phonebooks/contacts/statistics/full?userId=4)
    - `GET`: Returns comprehensive analytics including MIN/MAX/AVG name and number lengths for a specific user, for example with "id": "4".
    - **Response**:
    ```json lines
      {
        "totalContacts": 45,
        "uniqueNumbers": 42,
        "minNameLength": 3.0,
        "maxNameLength": 25.0,
        "avgNameLength": 12.5,
        "minNumberLength": 10.0,
        "maxNumberLength": 15.0,
        "avgNumberLength": 12.8
      }
    ```
---

### 3. Users Endpoints

- **Get All Phonebook Owners with Pagination**: [http://localhost:8080/api/v1/phonebooks/users/owners?page=0&size=10&sort=name,asc](http://localhost:8080/api/v1/phonebooks/users/owners?page=0&size=10&sort=name,asc)
    - `GET`: Retrieves all users who own a phonebook **(Admin only)**.
    - Parameters:
        - `page` - Page number (default: 0)
        - `size` - Items per page (default: 10)
        - `sort` - Sort field and direction, e.g., name,asc or email,desc

- **Search Users by Name with Pagination**: [http://localhost:8080/api/v1/phonebooks/users/names/{name}?page=0&size=10&sort=name,asc](http://localhost:8080/api/v1/phonebooks/users/names/{name}?page=0&size=10&sort=name,asc)
    - `GET`: Searches for users whose name starts with the specified string **(Admin only)**.
    - Parameters:
        - `name` - Name prefix to search (e.g., "a" or "Bob")
        - `page` - Page number (default: 0)
        - `size` - Items per page (default: 10)
        - `sort` - Sort field and direction

- **Get User by ID**: [http://localhost:8080/api/v1/phonebooks/users/{id}](http://localhost:8080/api/v1/phonebooks/users/{id})
    - `GET`: Retrieves a single user by ID, for example "id": "4".
    - **Authorization**: User can view their own profile; Admin can view any user.

- **Update User**: [http://localhost:8080/api/v1/phonebooks/users/{id}](http://localhost:8080/api/v1/phonebooks/users/{id})
    - `PUT`: Updates user information (name and/or email) by ID. Requires a JSON body with updated details:
    ```json lines
      {
        "name": "Jimmy",
        "email": "jimmy@example.com"
      }
    ```

- **Delete User**: [http://localhost:8080/api/v1/phonebooks/users/{id}](http://localhost:8080/api/v1/phonebooks/users/{id})
    - `DELETE`: Removes a user and all associated data **(Admin only)**.
    - **Note**: User deletion is logged in the audit table.

- **User Statistics Endpoints (Aggregate Functions - Admin Only)**
- **Get Total Users Count**: [http://localhost:8080/api/v1/phonebooks/users/statistics/total](http://localhost:8080/api/v1/phonebooks/users/statistics/total)
    - `GET`: Returns total number of registered users.
    - **Response**: `{ "totalUsers": 7 }`.

- **Get Active Sessions Count**: [http://localhost:8080/api/v1/phonebooks/users/statistics/active-sessions](http://localhost:8080/api/v1/phonebooks/users/statistics/active-sessions)
    - `GET`: Returns number of users with active (non-expired) sessions.
    - **Response**: `{ "activeSessions": 3 }`.

- **Get Users Count by Role**: [http://localhost:8080/api/v1/phonebooks/users/statistics/by-role](http://localhost:8080/api/v1/phonebooks/users/statistics/by-role)
    - `GET`: Returns user distribution by role (ADMIN vs USER).
    - **Response**:
    ```json lines
      [
        { "role": "ADMIN", "count": 2 },
        { "role": "USER", "count": 5 }
      ]
    ```
- **Get Full User Statistics**: [http://localhost:8080/api/v1/phonebooks/users/statistics/full](http://localhost:8080/api/v1/phonebooks/users/statistics/full)
    - `GET`: Returns comprehensive user analytics.
    - **Response**:
    ```json lines
      {
        "totalUsers": 7,
        "uniqueEmails": 7,
        "minNameLength": 3.0,
        "maxNameLength": 20.0,
        "avgNameLength": 8.5,
         "adminCount": 2,
        "userCount": 5,
         "activeSessions": 3
      }
    ```
---
- **Audit Trail**:
    - The user_audit table automatically tracks:
      - User registration events (**REGISTER** operation).
      - User deletion events (**DELETE** operation).
      - Timestamps and user identification for compliance.
---