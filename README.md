## :closed_book: **Book Store Project**

This is **Book Store API**, a spring-boot backend built for ordering books online 

## :mag_right: **Project Overview**

Book Store API provides different operations:
- Role-based access (Admin, User)
- Book management: Allows to add/remove/edit data about avaliable books and grouping them by categories
- Shopping cart: Provides user ability to select multiple books with different quantity before proceeding to order 
- Order management: Gives administrator tools for saving orders and tracking their status

## :hammer_and_wrench: **Technology Stack**
**Core:**
| Tool    | Description                                         |
|---------|-----------------------------------------------------|
| Java 17 | Core programming language of the backend            |
| Maven   | Project management and build tool                   |

**Spring:**
| Tool                     | Description                                                |
|--------------------------|------------------------------------------------------------|
| Spring Boot 3.4.2     | Advanced architecture framework for building applications |
| Spring Boot Web      | Enables embedded web server and REST API development       |
| Spring Data JPA     | Simplifies database access operations using JPA and ORM    |
| Spring Boot Security   | Provides authentication and authorization capabilities    |
| Spring Boot Validation | Ready-to-use collection of data constraints/checks         |

**Data storage and access:**
| Tool        | Description                                                |
|-------------|------------------------------------------------------------|
| MySQL 8.0.33 | Database management system                                 |
| Hibernate   | Bidirectional mapping tool between Java code and SQL database |
| Liquibase   | Tool for database creation and version control             |


**Additional libs and tools:**
 | Tool      | Description                                               |
|-----------|-----------------------------------------------------------|
| Lombok    | Library for Java code simplification                      |
| MapStruct | Tool for simple data mapping                              |
| JWT       | Authorization standard                                     |
| Swagger   | Tools to create API documentation                         |
| Docker    | Platform for project packaging and deployment             |

## :computer: **How to run the project**
1. Download [Java](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) and [Docker Desktop](https://www.docker.com/).
2. Clone repository: Open your terminal (cmd) and use `git clone https://github.com/MishaHMK/Spring-Boot-Book.git`.
3. Add .env file in root folder of cloned project and write down a configuration:

  ![image](https://github.com/user-attachments/assets/20acf401-8424-4db2-b83d-87be85208afd)

4. Build the project: Open your termianal (cmd) and use `mvn clean package`.
5. Docker Compose: Open your terminal and use `docker compose build` and `docker compose up`.
6. Project is ready to run

🔸 The project is also testable through in [Documentation](http://ec2-51-20-55-12.eu-north-1.compute.amazonaws.com/api/swagger-ui/index.html#)

## :movie_camera: **Video Preview**
[Video](https://drive.google.com/drive/u/0/folders/1jerCQ2yfZB28p9DfHGnyk_9NJYN_NqeH)

## :page_facing_up: **Endpoints explanation**

Some endpoints require a [role] for access, use JWT token (Bearer) or Basic authentication.

**UserController:** Handles registration and login requests.
- POST: `/api/auth/registration` - register new user.
- POST: `/api/auth/login` - login user and receive JWT token.

**BookController:** Handles requests for book operations (Authorization is required). 
- GET: `/api/books` - Receive all books with optional pagination.
- GET: `/api/books/{id}` - Search a specific book by ID.
- GET: `/api/search` - Fiter books by symbols contained in title or author name with optional pagination.
- POST: `/api/books` - Create new book. [Admin]
- PUT: `/api/books/{id}` - Update book data. [Admin]
- DELETE: `/api/books/{id}` - Soft delete book. [Admin]

**CategoryController:** Handles requests for category operations and getting all books by category (Authorization is required).
- GET: `/api/categories` - Receive all categories.
- GET: `/api/categories/{id}` - Receive a specific category by its ID.
- GET: `/api/categories/{id}/books` - Receive all books of category by a category ID.
- POST: `/api/categories` - Create a new category. [Admin]
- PUT: `/api/categories/{id}` - Update book data. [Admin]
- DELETE: `/api/categories/{id}` - Soft delete category. [Admin]

**ShoppingCartController:** Handles requests for shopping cart operations (Authorization is required).
- GET: `/api/cart` - Receive all items from a shopping cart.
- POST: `/api/cart` - Add an item to a shopping cart.
- PUT: `/api/cart/cart-items/{cartItemId}` - Update quantity of a specific item in shopping cart.
- DELETE: `/api/cart/cart-items/{cartItemId}` - Delete items from a shopping cart.

**OrderController:** Handles requests for order operations.
- GET: `/api/orders` - Receive all user orders.
- GET: `/api/orders/{order-id}/items` - Receive all items from a specific order.
- GET: `/api/orders/{order-id}/items/{item-id}` - Receive a specific item from a specific order.
- POST: `/api/orders` - Create an order.
- PATCH: `/api/orders/{id}` - Updating an order status. [Admin]
🔸 Allowed order status values are: COMPLETED, DELIVERED, PENDING
