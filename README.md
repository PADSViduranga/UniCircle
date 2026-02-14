UniCricle
Project Description
UniCricle is a Spring Boot application designed to manage university clubs, events, charities,
and a leaderboard system. It provides functionalities for user authentication, role-based
access control (Admin, User, Club Leader roles), and a points system to encourage
participation in charity events.
Features
• User Management: Registration, login, and role-based access control.
• Club Management: Create, approve, and manage university clubs. Club leaders can
manage their respective clubs.
• Event Management: Create and manage events associated with clubs.
• Charity Module: Users can participate in charity events and earn points.
• Leaderboard: Displays users ranked by their accumulated points.
• Security: Implements Spring Security for authentication and authorization, including
custom success/failure handlers and password encoding.
• Database Integration: Uses MySQL for data persistence with Spring Data JPA.
• Templating: Utilizes Thymeleaf for server-side rendering of web pages.

Technologies Used
• Backend: Java 25, Spring Boot 3.5.10
• Web Framework: Spring Web
• Database: MySQL
• ORM: Spring Data JPA, Hibernate
• Security: Spring Security
• Templating Engine: Thymeleaf
• Build Tool: Maven
• Utility: Lombok
Setup Instructions
Prerequisites
• Java Development Kit (JDK) 25
• Maven
• MySQL Server

Database Configuration
1 Ensure your MySQL server is running.
2 The application is configured to connect to a database named UniClub on
localhost:3306 with username root and an empty password. You can modify these
settings in src/main/resources/application.properties:
spring.datasource.url=jdbc:mysql://localhost:3306/UniClub\?createDatabase
IfNotExist=true
spring.datasource.username=root
spring.datasource.password=
3 The createDatabaseIfNotExist=true parameter will automatically create the UniClub
database if it doesn't exist.
Running the Application
4 Navigate to the UniCricle project root directory (where pom.xml is located).
5 Build the project using Maven:
mvn clean install
6 Run the Spring Boot application:
mvn spring-boot:run
7 The application will start on http://localhost:8080

Usage
Default Credentials
The DataInitializer.java class creates a default club leader user and a test club upon
application startup if they don't already exist.
• Username: leader1
• Email: leader@example.nsbm.ac.lk
• Password: Pw1234
This user has the USER role and is assigned as PRESIDENT of the "Test Club".

Roles and Access
UniCricle implements a role-based access control system:
• ADMIN: Full access to /admin/** endpoints.
• PRESIDENT/VICE_PRESIDENT/CLUB_LEADER: Access to /leader/**
endpoints and can create announcements.
• USER: General user access to /user/**, /dashboard/**, /charity/**, and /clubs/**.
Upon successful login, users are redirected based on their highest authority:
• ADMIN: /admin/dashboard
• PRESIDENT/VICE_PRESIDENT/CLUB_LEADER: /leader/dashboard
• USER: /dashboard
Charity and Leaderboard
Users can participate in charity events. Participating in a charity event awards points to the
user, which are then reflected on the leaderboard.

Charity and Leaderboard
Users can participate in charity events. Participating in a charity event awards points to the
user, which are then reflected on the leaderboard.
Project Structure Overview
UniCricle/
├── src/
│ ├── main/
│ │ ├── java/com/example/UniCricle/
│ │ │ ├── Auth/ # Custom Spring Security handlers
│ │ │ ├── config/ # Spring configuration (Security,
DataInitializer)
│ │ │ ├── controller/ # MVC Controllers
│ │ │ ├── DTO/ # Data Transfer Objects
│ │ │ ├── model/ # JPA Entities and Enums
│ │ │ ├── Repository/ # Spring Data JPA Repositories
│ │ │ └── Service/ # Business Logic Services
│ │ └── resources/
│ │ ├── static/ # Static assets (CSS, JS, Images)
│ │ └── templates/ # Thymeleaf HTML templates
│ └── test/ # Test classes
├── pom.xml # Maven Project Object Model
└── README.md # This file
