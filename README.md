# Shopping Cart Backend Project

🍔A Spring Boot and Spring Security backend for a shopping cart application. This project demonstrates building a complete e-commerce backend from scratch, including user management, product and category CRUD, cart functionality, order management, and JWT-based authentication.

---

## 🌟 Technology Stack

### Backend
- **Framework**: SpringBoot 3.5.6
- **ORM**: Spring Data JPA / Hibernate
- **Database**: MySQL  
- **JDK Version**: 21 
- **Build Tool**: Maven 3.9.6
- **Security**: JWT for authentication

---

## 📌 Features

### User Management
- Register, retrieve, update, and delete users
- JWT authentication and login
- Password hashing and secure storage

### Product & Category Management
- Full CRUD for products and categories
- Product image management
- DTO-based API design

### Cart & CartItem Management
- Create and initialize carts per user
- Add, remove, and update cart items
- Calculate total price of a cart

### Order Management
- Place orders from cart
- Track order history per user

### Security
- Spring Security integration
- JWT authentication
- Role-based access control (can be extended)

### Database
- MySQL database integration
- JPA/Hibernate for ORM

---

## 📝 API Documentation
- Managed with **Apifox** (Postman + Swagger) for simplified configuration and powerful testing.

---




## 📁 project Structure

```bash
src/main/java/com/xuwei/prostore
│
├── controller       # REST API controllers
├── dto              # Data Transfer Objects
├── exception        # Custom exceptions
├── mapper           # MapStruct mappers
├── model            # Entity classes
├── repository       # Spring Data JPA repositories
├── response         # Standard API response class
├── service          # Business logic services
└── security         # Spring Security & JWT configuration

```

## 🚀 Getting Started

### Prerequisites
- Java 21+
- Maven 3.9+
- MySQL 8+
- Postman or any API testing tool

### Installation
1. **Clone the repository**
```bash
https://github.com/qin-coder/prostore.git
cd prostore
```
2. **Configure database in application.properties**
```
spring.datasource.url=jdbc:mysql://localhost:3306/prostore
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

### Example Endpoints
```
User

POST /users - Create a user

GET /users/{id} - Fetch user by ID

GET /users - Fetch all users

DELETE /users/{id} - Delete user

Cart

POST /carts/initialize?userId={userId} - Initialize cart for a user

GET /carts/{cartId} - Get cart details

DELETE /carts/delete/{cartId} - Clear cart

GET /carts/total/{cartId} - Get total price

CartItem

POST /cartItems/add - Add item to cart

PUT /cartItems/update/cart/{cartId}/item/{itemId} - Update item quantity

DELETE /cartItems/remove/cart/{cartId}/item/{itemId} - Remove item

GET /cartItems/item/{itemId} - Get cart item by ID

Order

POST /orders/order?userId={userId} - Place order

GET /orders/order/{orderId} - Get order by ID
```

## 💾 Database

MySQL used as the primary database

Tables are auto-generated using JPA/Hibernate

Relationships:

User ↔ Cart (1:1)

User ↔ Order (1:N)

Cart ↔ CartItem (1:N)

Product ↔ Category (N:1)
