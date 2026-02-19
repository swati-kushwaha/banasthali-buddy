# Driver API

Base path: `/api/driver`

All endpoints that require authentication expect header:

- `Authorization: Bearer <JWT>`

---

## POST /api/driver/register

- Description: Register a new driver and receive JWT token.
- Request body (application/json):

```json
{
  "name": "John Driver",
  "phone": "9999999999",
  "email": "driver@example.com",
  "password": "Secret123",
  "vehicleNumber": "ABC1234",
  "licenseNumber": "LIC987654"
}
```

- Response (200):

```json
{
  "token": "<JWT>",
  "userId": "<driverId>",
  "username": "John Driver",
  "email": "driver@example.com"
}
```

---

## POST /api/driver/login

- Description: Login and receive JWT token.
- Request body:

```json
{ "email": "driver@example.com", "password": "Secret123" }
```

- Response (200): same `AuthResponse` as register.

---

## PATCH /api/driver/status

- Description: Update driver's online/offline status. Requires driver token.
- Headers: `Authorization: Bearer <JWT>`
- Request body:

```json
{ "online": true }
```

- Response (200): `DriverResponseDTO` (example):

```json
{
  "id": "driverId",
  "name": "John Driver",
  "email": "driver@example.com",
  "phone": "9999999999",
  "vehicleNumber": "ABC1234",
  "location": null,
  "isOnline": true,
  "rating": 0.0,
  "createdAt": "2026-02-19T12:34:56.789"
}
```

---

## PUT /api/driver/location

- Description: Update driver's location (longitude/latitude). Broadcasts `DriverResponseDTO` to `/topic/driver-location/{driverId}`.
- Headers: `Authorization: Bearer <JWT>`
- Request body:

```json
{ "latitude": 12.34567, "longitude": 76.54321 }
```

- Response (200): `DriverResponseDTO` (same shape as above) and a WebSocket broadcast on `/topic/driver-location/{driverId}`.

---

## GET /api/driver/profile

- Description: Get authenticated driver's profile.
- Headers: `Authorization: Bearer <JWT>`
- Response (200): `DriverResponseDTO`.

---

## GET /api/driver/rides

- Description: Get ride history (bookings assigned to the driver).
- Headers: `Authorization: Bearer <JWT>`
- Response (200): Array of `Booking` objects.

---

### Notes
- No API response contains the raw `password` field. `DriverResponseDTO` intentionally excludes `password` and `licenseNumber` to avoid leaking sensitive information.
- WebSocket topic for location updates: `/topic/driver-location/{driverId}` (uses STOMP over `/ws`).
