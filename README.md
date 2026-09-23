# Basic Java Web Server REST API

<img src="docs/img/java-img.jpg" width="100%">

This is a basic web server project originally built with native Java, now refactored into a **professional RESTful JSON API**. It demonstrates fundamental backend concepts such as handling HTTP methods, thread pools for concurrency, routing, and processing JSON payloads. The goal of this project is to serve as an educational reference for building a modern web service in Java from scratch.

## Features

- **REST API (JSON)**: Fully responds and consumes `application/json` using Google's Gson library.
- **Concurrency**: Built-in `ThreadPoolExecutor` for handling multiple HTTP requests simultaneously.
- **Maven Architecture**: Standardized project structure and dependency management.
- **Dev Tooling**: Code automatically formatted via **Spotless** (Google Java Format) and linted via **Checkstyle**.
- **CI/CD Ready**: Includes GitHub Actions for PR checks (Format/Lint/Test) and multi-stage manual deployment pipelines using SCP/SSH.

## Getting Started

### Prerequisites

Ensure you have **Java Development Kit (JDK) 17+** and **Apache Maven** installed on your machine.

### Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/FarrelAD/Basic-Web-Server-Java.git
    ```
2. Navigate into the project directory:
    ```bash
    cd Basic-Web-Server-Java
    ```

### Running the Server

Using Maven, you can compile and start the server immediately:
```bash
mvn clean compile exec:java -Dexec.mainClass="com.server.Main"
```
The server will be running at `http://localhost:8000`

### Building the Executable JAR
If you want to build a deployment-ready Fat JAR:
```bash
mvn clean package -DskipTests
java -jar target/basic-web-server-1.0-SNAPSHOT.jar
```

## Tooling & Testing

Run the automated tests:
```bash
mvn test
```

Format the codebase and check for linting errors:
```bash
mvn spotless:apply checkstyle:check
```

### Pre-commit Hooks
This project strictly enforces code quality using Git pre-commit hooks (Spotless and Checkstyle).
After cloning the repository, you **must** configure Git to use the local hooks directory:
```bash
git config core.hooksPath .githooks
```
Every time you commit, it will automatically verify formatting and linting. If it fails, the commit will be aborted.

## Endpoints
Here are the key REST endpoints you can interact with. All endpoints strictly consume and return `application/json`.

### 1. API Health Check
- **`GET /`**
  - **Response (200 OK):**
    ```json
    {
      "status": "UP",
      "message": "Welcome to Basic Web Server API",
      "totalUsers": 8
    }
    ```

### 2. Fetch All Users
- **`GET /users`**
  - **Response (200 OK):**
    ```json
    [
      { "name": "Alex", "job": "Data Analyst" },
      { "name": "Budi", "job": "Frontend Web Developer" }
    ]
    ```

### 3. Fetch User by ID
- **`GET /users/:id`** *(e.g. `/users/1`)*
  - **Response (200 OK):**
    ```json
    {
      "name": "Alex",
      "job": "Data Analyst"
    }
    ```

### 4. Create New User
- **`POST /users`**
  - **Request Body:**
    ```json
    {
      "name": "John Doe",
      "job": "Backend Engineer"
    }
    ```
  - **Response (201 Created):**
    ```json
    {
      "status": "success",
      "message": "User added successfully"
    }
    ```

### 5. Update User Information
- **`PATCH /users/:id`** *(e.g. `/users/1`)*
  - **Request Body:**
    ```json
    {
      "job": "Senior Data Analyst"
    }
    ```
  - **Response (200 OK):**
    ```json
    {
      "status": "success",
      "message": "Data updated successfully"
    }
    ```

### 6. Delete a User
- **`DELETE /users/:id`** *(e.g. `/users/1`)*
  - **Response (200 OK):**
    ```json
    {
      "status": "success",
      "message": "Data deleted successfully"
    }
    ```

### 7. Search Users
- **`GET /search?name=&job=`** *(e.g. `/search?job=Data Analyst`)*
  - **Response (200 OK):**
    ```json
    [
      { "name": "Alex", "job": "Data Analyst" },
      { "name": "Dono", "job": "Data Analyst" }
    ]
    ```

## Code Structure
The project uses the standard Maven structure:

```bash
/root
├───.github/workflows   # CI/CD Pipelines
├───pom.xml             # Build config and dependencies
└───src
    ├───main/java/com/server
    │   ├─── /controller    # HTTP route handlers
    │   ├─── /model         # Data structures and tracking
    │   ├─── /utils         # Parsers and HTTP helpers
    │   └─── Main.java      # Server initialization and Thread Pool
    └───test/java/com/server
        └─── /model         # JUnit 5 test cases
```

## License
This project is licensed under the MIT License.

## Acknowledgments
This project is intended to help others learn the basics of building a robust REST API with Java ☕.