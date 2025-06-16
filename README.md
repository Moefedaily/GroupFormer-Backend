# GroupFormer - Group Generation

Hey there! Welcome to our group generation project.

## What This Thing Does

Imagine you're a teacher with a bunch of students, and you need to create balanced groups. You don't want all the guys in one group, all the beginners in another, and definitely don't want to recreate the exact same groups you made last week. That's exactly what this does - but automatically.

## **Database Design (MCD)**

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
├── security/       # JWT authentication & security
├── service/        # Business logic interfaces
├── serviceImpl/    # Actual business logic
└── util/           # JWT utilities & password encoding
```

## **Authentication & Security**

### JWT-Based Authentication
We implemented a complete JWT authentication system with three user roles:

- **STUDENT**: Read-only access to shared lists (no authentication required)
- **TRAINER**: Full CRUD on their own data, read access to everything
- **ADMIN**: Full access to everything

### Security Features
- **Argon2 password hashing** (most secure algorithm available)
- **JWT tokens** for stateless authentication
- **Role-based access control** with Spring Security
- **Ownership validation** on all write operations

### Authentication Endpoints
```http
POST   /api/auth/login       # Login with email/password
POST   /api/auth/register    # Register new trainer account
POST   /api/auth/logout      # Logout
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

### User Management (Admin Only)
```http
POST   /api/users                    # Create user
GET    /api/users/{id}               # Get user by ID
GET    /api/users/email/{email}      # Get user by email
PUT    /api/users/{id}               # Update user
DELETE /api/users/{id}               # Delete user
PUT    /api/users/{id}/accept-cgu    # Accept terms
```

### Student Lists (Authenticated)
```http
GET    /api/studentlists             # See all lists (universal access)
POST   /api/studentlists             # Create your own list
GET    /api/studentlists/{id}        # See any list details
PUT    /api/studentlists/{id}        # Edit only your lists
DELETE /api/studentlists/{id}        # Delete only your lists
GET    /api/studentlists/my          # Convenience: your lists only
```

### People Management (Authenticated)
```http
GET    /api/persons                         # See all people (global pool)
POST   /api/persons/studentlist/{listId}    # Add person to your list only
GET    /api/persons/{id}                    # See any person details
GET    /api/persons/studentlist/{listId}    # See people in any list
PUT    /api/persons/{id}                    # Edit people in your lists only
DELETE /api/persons/{id}                    # Delete people from your lists only
```

### Group Draws (Authenticated)
```http
GET    /api/groupdraws                                # See all group draws
POST   /api/groupdraws/studentlist/{studentListId}    # Create draw for your list
GET    /api/groupdraws/{id}                           # See any draw details
GET    /api/groupdraws/studentlist/{studentListId}    # See draws for any list
DELETE /api/groupdraws/{id}                           # Delete only your draws
```

### Groups (Authenticated)
```http
GET    /api/groups                                    # See all groups
POST   /api/groups/groupdraw/{groupDrawId}            # Create group in your draw
GET    /api/groups/{id}                               # See any group details
GET    /api/groups/groupdraw/{groupDrawId}            # See groups in any draw
PUT    /api/groups/{id}                               # Edit groups in your draws only
DELETE /api/groups/{id}                               # Delete groups from your draws only
POST   /api/groups/{groupId}/persons/{personId}       # Add person to your group
DELETE /api/groups/{groupId}/persons/{personId}       # Remove person from your group
```

### Group Generation (Authenticated)
```http
POST   /api/generate-groups          # Generate groups for your lists only
```

### Authentication Example:
```bash
# Register
POST /api/auth/register
{"name":"Johnny Depp","email":"johnny@depp.com","password":"Amber12356"}

# Login  
POST /api/auth/login
{"email":"johnny@depp.com","password":"Amber12356"}

# Response includes JWT token:
{"token":"eyJhbGciOiJIUzI1NiJ9...","type":"Bearer","id":1,"name":"Johnny Depp","email":"johnny@depp.com","role":"TRAINER"}

# Use token in requests
GET /api/generate-groups
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Group Generation Example:
```json
{
    "studentListId": 1,
    "numberOfGroups": 3,
    "groupNames": ["CDA24", "CDA25J", "CDA25P"],
    "mixGender": true,
    "mixAge": false,
    "mixFrenchLevel": true,
    "mixTechnicalLevel": true,
    "mixFormerDwwm": false,
    "mixPersonalityProfile": true
}
```

## **Development Setup**

### Prerequisites
- Java 21+
- Maven 3.8+
- PostgreSQL 13+

### Configuration
Add to `application.properties`:
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/(db name)
spring.datasource.username= (username)
spring.datasource.password= (password)

# JWT Configuration
jwt.secret=a (base64_encoded) secret key
jwt.expiration=86400000 (1 day)
```

### Running the Application
```bash
# Clone and build
git clone <repository-url>
cd groupformer-backend
mvn clean install

# Run tests
mvn test

# Start the application
mvn spring-boot:run
```

## **The Algorithm Challenge**

### **Group Balancing Problem**
Creating balanced groups with multiple criteria (gender, skills, personality, age) without conflicts.

### **Solution**
Step-by-step algorithm: random distribution first, then sequential balancing per criterion. Simple concept: extract everyone, organize by one factor, redistribute evenly, repeat.

### **JWT Authentication Challenge**
Integrating Spring Security with JWT was complex. The flow: **UserDetailsService** finds users → **CustomUserDetails** adapts to Spring Security format → **JWT Filter** validates tokens → **Security Config** ties everything together.

## **How It Performs**

We tested it with real scenarios and it handles various situations well - from simple 2-group splits to complex multi-criteria balancing with uneven numbers.

## **Final Result**

We ended up with a working system that creates balanced groups reliably with proper authentication and ownership controls. The approach is straightforward to understand and modify, which makes maintenance much easier.

---

Built with Spring Boot, JWT, Argon2, curiosity, and lots of coffee ☕☕☕☕☕☕☕☕☕☕☕☕
