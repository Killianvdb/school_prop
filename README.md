# School Property Reservation System

## 📌 Overview

School Property Reservation System is a web application for managing and reserving school-owned items (like lamps, equipment, etc.). Built with Spring Boot, it helps teachers and students easily browse, reserve, and track items, streamlining school inventory management.

## 🎯 Features

- Secure user registration and login (Spring Security)
- Browse available items
- Add items to a cart and checkout
- Make and manage reservations
- Admin dashboard for inventory management
- Planned: Notifications (email/system alerts), Swagger API docs

## 🛠️ Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- Hibernate / JPA
- Maven
- H2 / MySQL (configurable in `application.properties`)

## 📂 Project Structure

```
school_prop/
├── src/main/java/com/example/demo/
│   ├── Controller/        # REST controllers (Cart, Item, User, Reservation)
│   ├── DTO/               # Data Transfer Objects
│   ├── Entities/          # JPA entities (User, Cart, Item, Reservation, etc.)
│   ├── Repository/        # JPA repositories
│   ├── Security/          # Security configuration
│   ├── Service/           # Business logic
│   └── DemoApplication.java
├── src/main/resources/
│   └── application.properties
├── pom.xml                # Maven dependencies
```

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/Killianvdb/school_prop.git
cd school_prop
```

### 2. Configure the database

Edit `src/main/resources/application.properties` for your database settings. Example (H2):

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.hibernate.ddl-auto=update
```

### 3. Build and run

```bash
./mvnw spring-boot:run
```

### 4. Access the application

- API: [http://localhost:8080](http://localhost:8080)
- Swagger/API docs: _Coming soon_

## 📖 API Endpoints (Work In Progress)

- `POST /api/users/register` : Register a new user
- `POST /api/users/login` : Authenticate user
- `GET /api/items` : List available items
- `POST /api/cart` : Add item to cart
- `POST /api/reservations` : Make a reservation

_More endpoints will be documented soon._

## Contributing

Contributions are welcome! Fork the repository and submit a pull request.

## 📜 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---
helping links for this project  
for spring boot basics  
https://www.youtube.com/watch?v=9SGDpanrc8U 
https://spring.io/projects/spring-boot 
https://github.com/David-VS/Blog3TI   

for API 
https://www.baeldung.com/building-a-restful-web-service-with-spr
