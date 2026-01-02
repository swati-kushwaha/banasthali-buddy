# Banasthali Buddy - Backend

A smart campus app for transport tracking and **Student Exchange Hub** - a marketplace for students to buy and sell items.

## 🚀 Features

- **Student Exchange Hub** - Post, browse, and search items for sale
- JWT-based authentication
- Image upload support
- Category-based filtering
- RESTful API with Swagger documentation

## 📖 Documentation

- **[API Endpoints](API_ENDPOINTS.md)** - Complete API documentation with request/response examples
- **[Deployment Guide](DEPLOY.md)** - Deployment instructions for Render and Docker

## 🛠️ Tech Stack

- **Framework:** Spring Boot 3.5.9
- **Language:** Java 21
- **Database:** MongoDB
- **Security:** Spring Security + JWT
- **Documentation:** Swagger/OpenAPI 3.0

## 📋 Quick Start

### Prerequisites
- Java 21
- MongoDB (local or Atlas)
- Maven 3.9+

### Run Locally

```bash
# Clone and navigate to backend
cd backend

# Run with Maven
./mvnw spring-boot:run

# Or build and run JAR
./mvnw package -DskipTests
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Environment Variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `MONGODB_URI` | ✅ (prod) | localhost:27017 | MongoDB connection string |
| `JWT_SECRET` | ✅ (prod) | dev-default | JWT signing secret |
| `PORT` | ❌ | 8080 | Server port |

## 📡 API Endpoints Overview

| Endpoint | Method | Auth | Description |
|----------|--------|------|-------------|
| `/api/auth/register` | POST | ❌ | Register user |
| `/api/auth/login` | POST | ❌ | Login user |
| `/api/items` | POST | ✅ | Create item |
| `/api/items` | GET | ❌ | List all items |
| `/api/items/search` | GET | ❌ | Search items |
| `/api/items/{id}` | PUT/DELETE | ✅ | Update/Delete item |
| `/api/users/{sellerId}` | GET | ❌ | Get seller contact |

👉 **[See full API documentation](API_ENDPOINTS.md)**

## 🔗 Links

- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **Health Check:** `http://localhost:8080/actuator/health`

## 📄 License

See [LICENSE](LICENSE) for details.
