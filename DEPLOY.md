# Deployment and Build Notes

This project is a Spring Boot application. The repository has been updated to make deployment easier:

- `Dockerfile` — multi-stage image which builds with Maven and produces a small runtime image.
- `application.yml` (plus `application-local.yml` / `application-prod.yml`) — configuration is now profile-based; MongoDB URI and server port are read from environment variables for production and local defaults are provided in `application-local.yml`.

Required environment variables (production):
- `MONGODB_URI` — MongoDB connection string (no default; must be provided in production).
- `MONGODB_DATABASE` — (optional) database name, defaults to `banasthali_buddy`.
- `PORT` — (optional) server port, defaults to `8080`.

Notes on profiles:
- The app defaults to the `local` profile if `SPRING_PROFILES_ACTIVE` is not set. Use `SPRING_PROFILES_ACTIVE=prod` in production.
- Local developers can run the app without providing `MONGODB_URI` (it will use the `mongodb://localhost:27017/banasthali_buddy` default in the `local` profile).

Build locally with Maven:

```bash
mvn -DskipTests package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

Build and run with Docker:

```bash
docker build -t banasthali-backend:latest .
docker run -e MONGODB_URI="your_uri_here" -p 8080:8080 banasthali-backend:latest
```

Notes:
- Do not commit production secrets into `application.properties`. Provide them via environment variables or your CI/CD platform.
- The project now includes Spring Boot Actuator endpoints (`/actuator/health`, `/actuator/info`, `/actuator/metrics`) — configure your platform to allow access as needed.
