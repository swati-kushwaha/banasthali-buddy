# Deployment and Build Notes

This project is a Spring Boot application implementing the **Student Exchange Hub** REST API. The repository has been updated to make deployment easier:

- `Dockerfile` — multi-stage image which builds with Maven and produces a small runtime image.
- `application.yml` (plus `application-local.yml` / `application-prod.yml`) — configuration is now profile-based; MongoDB URI and server port are read from environment variables for production and local defaults are provided in `application-local.yml`.

## Required Environment Variables (Production)

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `MONGODB_URI` | ✅ Yes | - | MongoDB connection string |
| `MONGODB_DATABASE` | No | `banasthali_buddy` | Database name |
| `JWT_SECRET` | ✅ Yes | - | Secret key for JWT signing (min 64 chars recommended) |
| `JWT_EXPIRATION` | No | `86400000` | JWT token expiration in milliseconds (default: 24 hours) |
| `PORT` | No | `8080` | Server port |
| `FILE_UPLOAD_DIR` | No | `/app/uploads` | Directory for uploaded images |
| `SPRING_PROFILES_ACTIVE` | No | `local` | Active profile (`local`, `prod`, `test`) |

## API Endpoints

### Authentication
- `POST /api/auth/register` — Register a new user
- `POST /api/auth/login` — Login and get JWT token

### Items (Marketplace)
- `POST /api/items` — Create new item (🔒 requires auth)
- `GET /api/items` — List all available items
- `GET /api/items/{id}` — Get item by ID
- `GET /api/items/search?keyword=...` — Search items
- `GET /api/items/category/{category}` — Filter by category
- `PUT /api/items/{id}` — Update item (🔒 requires auth, owner only)
- `DELETE /api/items/{id}` — Delete item (🔒 requires auth, owner only)
- `PATCH /api/items/{id}/sold` — Mark as sold (🔒 requires auth, owner only)

### User Profile
- `GET /api/users/{sellerId}` — Get seller contact info (🔒 requires auth)

### Documentation
- `GET /swagger-ui.html` — Swagger UI
- `GET /v3/api-docs` — OpenAPI specification

### Health Check
- `GET /actuator/health` — Health endpoint
- `GET /actuator/info` — Info endpoint
- `GET /actuator/metrics` — Metrics endpoint

## Notes on Profiles

- The app defaults to the `local` profile if `SPRING_PROFILES_ACTIVE` is not set. Use `SPRING_PROFILES_ACTIVE=prod` in production.
- Local developers can run the app without providing `MONGODB_URI` (it will use the `mongodb://localhost:27017/banasthali_buddy` default in the `local` profile).

## Build Locally with Maven

```bash
mvn -DskipTests package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## Build and Run with Docker

```bash
docker build -t banasthali-backend:latest .
docker run -e MONGODB_URI="your_uri_here" \
           -e JWT_SECRET="your-super-secret-key-at-least-64-characters-long-for-security" \
           -p 8080:8080 banasthali-backend:latest
```

## Render Deployment

1. Push to GitHub
2. Create a new Web Service on Render
3. Connect your repository
4. Add environment variables:
   - `MONGODB_URI` — your MongoDB Atlas connection string
   - `JWT_SECRET` — generate a secure random string (64+ chars)
   - `SPRING_PROFILES_ACTIVE` — set to `prod`
5. Deploy!

## Security Notes

- **Never commit production secrets** to version control
- Use a strong, random JWT secret in production
- JWT tokens expire after 24 hours by default
- Passwords are hashed using BCrypt
- CORS is configured to allow all origins (customize for production)
