# Phoenix Technologies Code Exercise

A RESTful Product Inventory API developed with **Spring Boot 3.4.4**, **Java 21**, and **Maven**.  

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.4-green)
![Java](https://img.shields.io/badge/Java-21-blue)
![Maven](https://img.shields.io/badge/Maven-3.9.8-orange)

## ✨ Features:
- **CRUD Operations** for Products and Categories
- **Embedded H2 Database** for development
- **Database Migrations** with Flyway
- **Unit tests** with JUnit
- **Pagination & Sorting** for all list endpoints
- **Filtering by name and price range** for product list endpoint
- **Auto-generated API Docs** with Swagger
---
## 🚀 Technologies Used
- **Framework**: Spring Boot 3.4.4
- **Language**: Java 21
- **Database**: H2 (Embedded)
- **ORM**: Spring Data JPA
- **Migrations**: Flyway
- **API Docs**: Springdoc OpenAPI
- **Build**: Maven
- **Testing**: JUnit 
---
## ⚙️ Requirements
- Java 21+ ([Installation Guide](https://docs.oracle.com/en/java/javase/21/install/index.html))
- Maven 3.9+ ([Installation Guide](https://maven.apache.org/install.html))
---
## ▶️ Running the Application
For running the application, execute the following Maven command:
```bash 
mvn spring-boot:run
```
The API will be available at http://localhost:8090

---
## 💾 H2 Database Console
Access the H2 database:
```bash 
http://localhost:8090/h2-console
```
Connection details:
- JDBC URL: jdbc:h2:file:./target/product_inventory_db
- Username: sa
- Password: Leave empty
---
## 📂 Flyway Database Migrations
Flyway runs automatically at startup.
Migration scripts are located in: 
```bash
src/main/resources/db/migration/
```
---
## ✅ Running Tests
Execute all unit tests:
```bash 
mvn test
```
---
## 📖 Swagger API Documentation
Explore and test the API endpoints via Swagger UI:
```bash 
http://localhost:8090/swagger-ui/index.html
```