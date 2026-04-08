# Banasthali Buddy - Backend

A smart campus application backend that provides:

• Student Exchange Hub (Marketplace for buying and selling items)
• E-rickshaw booking system using predefined pickup and destination posts
• Real-time bus tracking system with live location updates

The backend provides secure REST APIs for authentication, marketplace operations, booking requests, and transport tracking.

##  Features

### Student Exchange Hub (Marketplace)

* Students can post items for sale
* Students can browse available items
* Search items by category
* View seller contact details
* Image upload support

### E-rickshaw Booking System

* Predefined pickup and destination posts
* Students can request ride
* Drivers can accept booking requests
* Driver updates ride status (ACCEPTED, STARTED, ARRIVED, COMPLETED)
* Students can view booking progress

### Bus Tracking System

* Driver shares live GPS location
* Backend stores latest coordinates
* Students can track bus on map
* Real-time location updates
* ETA updates for students

### Security and Access Control

* JWT-based authentication
* Role-based access control (Admin, Student, Driver)

### System Features

* RESTful API design
* MongoDB database integration
* Health monitoring using Spring Actuator

## Documentation

* API Endpoints documentation available in project
* Deployment instructions available in project files

##  Tech Stack

* Framework: Spring Boot
* Language: Java
* Database: MongoDB
* Security: Spring Security + JWT
* Build Tool: Maven
* Documentation: Swagger / OpenAPI

##  Quick Start

### Prerequisites

Make sure the following software is installed:

* Java 17
* Maven 3.9+
* MongoDB Atlas account or local MongoDB
* Internet connection

### Run Locally

Open terminal inside backend folder:

cd backend

Run backend server:

mvn spring-boot:run

OR

mvn clean install

Backend will start at:

http://localhost:8080

### Configuration

Open file:

src/main/resources/application.properties

Set MongoDB connection string:

spring.data.mongodb.uri=<your-mongodb-connection-string>

Example:

spring.data.mongodb.uri=mongodb+srv://banasthali_buddy:banasthali_buddy2027@cluster0.mz5eyzf.mongodb.net/?appName=Cluster0

Set server port:

server.port=8080

##  API Endpoints Overview

### Authentication

POST /api/auth/register
Register user

POST /api/auth/login
Login user

### Marketplace APIs

POST /api/items
Create item listing

GET /api/items
Get all items

GET /api/items/search
Search items

PUT /api/items/{id}
Update item

DELETE /api/items/{id}
Delete item

GET /api/users/{sellerId}
Get seller contact

### E-rickshaw Booking APIs

GET /api/posts
Get predefined pickup and destination posts

POST /api/bookings/request
Create ride request

PATCH /api/bookings/{id}/status
Update booking status

GET /api/bookings/me/passenger
Get passenger booking details

GET /api/bookings/me/driver
Get driver booking details

### Bus Tracking APIs

POST /api/bus/location
Driver sends bus coordinates

GET /api/bus/location
Fetch latest bus location

GET /api/bus/route
Fetch route details

##  Links

Swagger UI:

http://localhost:8080/swagger-ui.html

Health Check:

http://localhost:8080/actuator/health

##  Example Workflow

### Marketplace Flow

1. Student registers or logs in
2. Student creates item listing
3. Other students browse items
4. Buyer contacts seller

### E-rickshaw Booking Flow

1. Student selects pickup and destination post
2. Student sends ride request
3. Driver receives booking request
4. Driver accepts booking
5. Driver updates ride status
6. Student tracks booking status

### Bus Tracking Flow

1. Driver turns ON location
2. Driver application sends GPS coordinates
3. Backend stores latest coordinates
4. Student application fetches latest location
5. Bus location displayed on map

##  License

Project developed for academic purpose.
