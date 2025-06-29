# Spring Boot JPA Hibernate Example

A comprehensive example project demonstrating three different approaches to data persistence in Spring Boot: **JPA Entity Manager**, **Hibernate Session**, and **Spring Data JPA**.

> 📝 **This project is created as a companion to a Medium article** explaining the differences and use cases of these three data access approaches in Spring Boot applications.

## 📁 Project Structure

```
spring-boot-jpa-hibernate-examples/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/trio/spring/boot/jpa/hibernate/example/
│   │   │       ├── SpringBootJpaHibernateExampleApplication.java
│   │   │       ├── controller/
│   │   │       │   ├── api/
│   │   │       │   │   ├── EntityManagerOperationsExampleApi.java
│   │   │       │   │   ├── HibernateSessionExampleApi.java
│   │   │       │   │   └── SpringDataJpaExampleApi.java
│   │   │       │   ├── EntityManagerOperationsExampleController.java
│   │   │       │   ├── HibernateSessionExampleController.java
│   │   │       │   └── SpringDataJpaExampleController.java
│   │   │       ├── data/
│   │   │       │   ├── dto/
│   │   │       │   │   ├── UserRequest.java
│   │   │       │   │   └── UserResponse.java
│   │   │       │   ├── entity/
│   │   │       │   │   └── User.java
│   │   │       │   └── repository/
│   │   │       │       └── UserRepository.java
│   │   │       └── service/
│   │   │           ├── EntityManagerOperationsExampleService.java
│   │   │           ├── HibernateSessionExampleService.java
│   │   │           └── SpringDataJpaExampleService.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/
└── pom.xml
```

## 🚀 Getting Started

### Prerequisites

#### For Local Development
- Java 17 or higher
- Maven 3.6+

#### For Docker
- Docker
- Docker Compose

### Running the Application

#### With Maven
```bash
mvn spring-boot:run
```

#### With Docker (Recommended)
```bash
docker-compose up
```

The application will start on `http://localhost:2703`

## 📚 API Documentation

### Swagger UI
Access the interactive API documentation at: [Swagger UI](http://localhost:2703/swagger-ui/index.html)


## 🔧 Three Data Access Approaches

This project demonstrates three different ways to interact with the database, each with its own advantages:

### 1. JPA Entity Manager
**Path:** `/api/v1/users/entity-manager`

Direct use of JPA EntityManager for fine-grained control over persistence operations.

**Example Endpoints:**
- `GET /api/v1/users/entity-manager` - Get all users
- `GET /api/v1/users/entity-manager/{userId}` - Get user by ID
- `POST /api/v1/users/entity-manager` - Create new user
- `PUT /api/v1/users/entity-manager/{userId}` - Update user
- `DELETE /api/v1/users/entity-manager/{userId}` - Delete user
- `GET /api/v1/users/entity-manager/search?searchParam=John` - Search users

### 2. Hibernate Session
**Path:** `/api/v1/users/hibernate-session`

Native Hibernate Session API for advanced Hibernate features and optimizations.

**Example Endpoints:**
- `GET /api/v1/users/hibernate-session` - Get all users
- `GET /api/v1/users/hibernate-session/{userId}` - Get user by ID
- `POST /api/v1/users/hibernate-session` - Create new user
- `PUT /api/v1/users/hibernate-session/{userId}` - Update user
- `DELETE /api/v1/users/hibernate-session/{userId}` - Delete user

### 3. Spring Data JPA
**Path:** `/api/v1/users/spring-data-jpa`

High-level Spring Data JPA repositories for rapid development with minimal boilerplate.

**Example Endpoints:**
- `GET /api/v1/users/spring-data-jpa` - Get all users
- `GET /api/v1/users/spring-data-jpa/{userId}` - Get user by ID
- `POST /api/v1/users/spring-data-jpa` - Create new user
- `PUT /api/v1/users/spring-data-jpa/{userId}` - Update user
- `DELETE /api/v1/users/spring-data-jpa/{userId}` - Delete user

## 🐳  Docker Commands
```bash
# Start the application
docker-compose up

# Start in background
docker-compose up -d

# Stop the application
docker-compose down

# Rebuild and start
docker-compose up --build

# View application logs
docker-compose logs app

# Follow logs in real-time
docker-compose logs -f app
```

## 🛠 Technologies Used

- **Spring Boot 3.5.3** - Application framework
- **Spring Data JPA** - Data access abstraction
- **Hibernate** - ORM implementation
- **H2 Database** - In-memory database for development
- **Lombok** - Boilerplate code reduction
- **SpringDoc OpenAPI** - API documentation
- **Maven** - Build tool
- **Docker** - Containerization

## 🎯 Key Features

- **Three Different Approaches**: Compare and contrast different data access patterns
- **Comprehensive API Documentation**: Swagger UI with detailed endpoint descriptions
- **RESTful Design**: Following REST principles with proper HTTP status codes
- **In-Memory Database**: H2 database for easy setup and testing

## 🔍 When to Use Each Approach

### Entity Manager
- When you need fine-grained control over persistence operations
- Complex queries requiring native SQL or JPQL
- Batch processing operations
- Custom transaction management

### Hibernate Session
- When you need Hibernate-specific features
- Advanced caching strategies
- Complex object-relational mappings
- Performance-critical applications requiring optimization

### Spring Data JPA
- Rapid application development
- Standard CRUD operations
- Simple to moderate query complexity
- When you want to minimize boilerplate code
