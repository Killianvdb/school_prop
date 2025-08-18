School Property Reservation System

📌 Overview

This project is a School Property Reservation System built with Spring Boot.
It allows users to:

Register and log in securely.

Browse available items (e.g., lamps, equipment, etc.).

Add items to a cart.

Make reservations and manage checkout.

The system is designed to streamline the process of borrowing and managing school property.


⚠️ Note: This project is still under development.


🛠️ Tech Stack
Java 17+

Spring Boot

Spring Security

Maven

Hibernate / JPA

H2 / MySQL (configure in application.properties)

📂 Project Structure

bash
Copier
Modifier
school_prop-main/

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

🚀 Getting Started

1. Clone the repository
bash
Copier
Modifier
git clone https://github.com/your-username/school_prop.git
cd school_prop
2. Configure database
Edit src/main/resources/application.properties to match your database settings.

For example (H2 in-memory DB):

properties
Copier
Modifier
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.hibernate.ddl-auto=update

3. Build and run
bash
Copier
Modifier
./mvnw spring-boot:run
4. Access the app
API runs on: http://localhost:8080

Swagger / API docs: (to be added)


📖 API Endpoints (WIP)


POST /api/users/register → Register a new user

POST /api/users/login → Authenticate user

GET /api/items → List available items

POST /api/cart → Add item to cart

POST /api/reservations → Make a reservation

(More endpoints coming soon)

✅ Features (Planned & Completed)

 User authentication (Spring Security)

 Item browsing & cart system

 Reservation management UI

 Admin dashboard for inventory

 Notifications (email / system alerts)

🤝 Contributing
Contributions are welcome! Feel free to fork the repo and submit a pull request.

📜 License
This project is licensed under the MIT License – see the LICENSE file for details.

