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

## 🧭 Quick Examples (cURL & Postman)

Below are minimal example requests to try the main features. Replace `localhost:8080` and tokens as needed.

1) Register (student):

```bash
curl -X POST http://localhost:8080/api/auth/register \
	-H 'Content-Type: application/json' \
	-d '{"username":"Alice","email":"alice@example.com","password":"secret","role":"STUDENT"}'
```

2) Login (returns JWT):

```bash
curl -X POST http://localhost:8080/api/auth/login \
	-H 'Content-Type: application/json' \
	-d '{"email":"alice@example.com","password":"secret"}'
```

3) List Posts (predefined pickup/drop points):

```bash
curl http://localhost:8080/api/posts
```

4) Create a Post (admin only):

```bash
curl -X POST http://localhost:8080/api/posts \
	-H 'Content-Type: application/json' \
	-H 'Authorization: Bearer <ADMIN_JWT>' \
	-d '{"name":"Gate A","latitude":25.0,"longitude":75.0}'
```

5) Request a booking (student):

```bash
curl -X POST http://localhost:8080/api/bookings/request \
	-H 'Content-Type: application/json' \
	-H 'Authorization: Bearer <STUDENT_JWT>' \
	-d '{"pickupPostId":"<PICKUP_ID>","destinationPostId":"<DEST_ID>"}'
```

6) Driver updates booking status:

```bash
curl -X PATCH http://localhost:8080/api/bookings/<BOOKING_ID>/status \
	-H 'Content-Type: application/json' \
	-H 'Authorization: Bearer <DRIVER_JWT>' \
	-d '{"status":"ACCEPTED"}'
```

7) Get my bookings (passenger):

```bash
curl -H 'Authorization: Bearer <STUDENT_JWT>' http://localhost:8080/api/bookings/me/passenger
```

WebSocket (demo):

- STOMP endpoint: `ws://localhost:8080/ws` (SockJS supported)
- Driver subscribes: `/topic/driver/{driverId}` to receive booking requests
- Passenger subscribes: `/topic/passenger/{passengerId}` to receive status updates

### Postman Collection

See `postman/erickshaw-booking-postman.json` for an importable Postman collection with these requests.

## ✅ Demo Checklist — Full Booking Lifecycle

Use these requests in order to demo a booking from student request through driver completion.

1. Register student (or use existing student) — `POST /api/auth/register`
2. Login student — `POST /api/auth/login` → save `student_jwt`
3. Register driver (role DRIVER) or create driver user — `POST /api/auth/register`
4. Login driver — `POST /api/auth/login` → save `driver_jwt`
5. (Admin) Create two Posts if none exist — `POST /api/posts` with `admin_jwt` (create pickup and destination)
6. Student: Request booking — `POST /api/bookings/request` with `student_jwt` (body: `pickupPostId`, `destinationPostId`) → note returned `bookingId`
7. Driver: Receive WebSocket on `/topic/driver/{driverId}` or poll `GET /api/bookings/me/driver` with `driver_jwt` to see assigned booking
8. Driver: Accept booking — `PATCH /api/bookings/{bookingId}/status` with `driver_jwt` and body `{"status":"ACCEPTED"}`
9. Driver: Update to `STARTED` when ride begins — same PATCH with `{"status":"STARTED"}`
10. Driver: Update to `ARRIVED` when at pickup — PATCH `{"status":"ARRIVED"}`
11. Driver: Update to `COMPLETED` when ride ends — PATCH `{"status":"COMPLETED"}`
12. Student: Confirm or view booking history — `GET /api/bookings/me/passenger` with `student_jwt`

Follow these in order for a simple end-to-end demonstration. The Postman collection includes corresponding requests and placeholders for `{{student_jwt}}`, `{{driver_jwt}}`, `{{admin_jwt}}`, `{{postId}}`, and `{{bookingId}}`.

## 📄 License

See [LICENSE](LICENSE) for details.
