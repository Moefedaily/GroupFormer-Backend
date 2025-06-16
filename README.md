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

## 🎯 **Final Result**

We ended up with a working system that creates balanced groups reliably. The approach is straightforward to understand and modify, which makes maintenance much easier.

---

*A practical solution for a common classroom challenge* 📚