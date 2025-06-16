# GroupFormer - Group Generation

Hey there! Welcome to our group generation project.

## What This Thing Does

Imagine you're a teacher with a bunch of students, and you need to create balanced groups. You don't want all the guys in one group, all the beginners in another, and definitely don't want to recreate the exact same groups you made last week. That's exactly what this does - but automatically.

##  **Database Design (MCD)**

Here's how we organized our data to make everything work together:

![Database Schema](/Screenshots/Group-generator.png)

The database is straight-forward - users have lists, lists contain people, and when we generate groups, we save everything so we can learn from it next time.

## **Backend Structure**

Looking at our project structure, we kept things organized the Spring Boot way:

```
src/main/java/com/groupformer/   
├── controller/     # Handle web requests  
├── dto/            # Data transfer objects
├── mapper/         # Convert between DTOs and models
├── model/          # Our database entities
├── repository/     # Database access layer
├── service/        # Business logic interfaces
└── serviceImpl/    # Actual business logic
```

## **Testing Architecture**

```
src/test/java/com/groupformer/
└── controller/     # Integration tests for REST endpoints
    ├── UserControllerTest.java
    ├── StudentListControllerTest.java
    ├── PersonControllerTest.java
    ├── GroupControllerTest.java
    ├── GroupDrawControllerTest.java
    └── GroupGenerationControllerTest.java
```

### Key Testing Insights

1. **Mock Services, Not Repositories**: Each controller test mocks its service layer
2. **Validation Testing**: Test only success scenarios

## **API Endpoints**

### User Management
```http
POST   /api/users                    # Create user
GET    /api/users/{id}               # Get user by ID
GET    /api/users/email/{email}      # Get user by email
PUT    /api/users/{id}               # Update user
DELETE /api/users/{id}               # Delete user
PUT    /api/users/{id}/accept-cgu    # Accept terms
```

### Student Lists
```http
POST   /api/studentlists/user/{userId}        # Create list for user
GET    /api/studentlists/{id}                 # Get list by ID
GET    /api/studentlists/user/{userId}        # Get user's lists
PUT    /api/studentlists/{id}                 # Update list
DELETE /api/studentlists/{id}                 # Delete list
```

### People Management
```http
POST   /api/persons/studentlist/{listId}      # Add person to list
GET    /api/persons/{id}                      # Get person by ID
GET    /api/persons/studentlist/{listId}      # Get people in list
PUT    /api/persons/{id}                      # Update person
DELETE /api/persons/{id}                      # Delete person
```

### Group Generation
```http
POST   /api/generate-groups          # Generate balanced groups
```

## **Development Setup**

### Prerequisites
- Java 21+
- Maven 3.8+
- PostgreSQL 13+

### Running the Application
```bash
# Clone the repository
git clone <repository-url>
cd groupformer-backend

# Configure database in application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/groupformer
spring.datasource.username=your_username
spring.datasource.password=your_password

# Run tests
mvn test

# Start the application
mvn spring-boot:run
```

## **The Algorithm Challenge**

### **The Problem We Needed to Solve**

Creating balanced groups turned out to be trickier than expected. We needed to handle multiple criteria at once:
- Gender balance across groups
- Skill level distribution
- Personality type mixing
- Age diversity
- Experience level balance

The challenge was making sure all these factors worked together without creating conflicts.

### **Our Solution**

We built an algorithm that works step by step, handling one criterion at a time. It starts with a random distribution, then applies each selected balancing criterion sequentially.

The core idea is simple: take everyone out, organize by one factor, then redistribute evenly. Repeat for each factor the user wants to balance.

## **How It Performs**

We tested it with real scenarios and it handles various situations well - from simple 2-group splits to complex multi-criteria balancing with uneven numbers.

## **Final Result**

We ended up with a working system that creates balanced groups reliably. The approach is straightforward to understand and modify, which makes maintenance much easier.

---

Built with Spring Boot, curiosity, and lots of coffee ☕☕☕☕☕☕☕☕☕☕☕☕
