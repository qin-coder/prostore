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

## ⚙️ Setup Instructions

1. Clone the repository and open in **VS Code** (frontend) and **IDEA** (backend).  
2. **Backend Setup**:
   - Create database and tables first
   - Update database credentials in `application.yml`  
3. **Frontend Setup**:
   - Create Vue 3 project scaffold  
   - Import frontend code  
   - Install dependencies: `npm install`  
   - Start dev server: `npm run dev`  

---


## 📁 Frontend Structure

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





