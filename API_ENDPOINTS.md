# Student Exchange Hub - API Endpoints Documentation

> **Base URL:** `http://localhost:8080` (local) or your deployed URL  
> **API Version:** v1  
> **Last Updated:** January 2026

---

## 📋 Table of Contents

1. [Authentication](#-authentication)
   - [Register](#post-apiauthregister)
   - [Login](#post-apiauthlogin)
2. [Items (Marketplace)](#-items-marketplace)
   - [Create Item](#post-apiitems)
   - [Get All Items](#get-apiitems)
   - [Get Item by ID](#get-apiitemsid)
   - [Search Items](#get-apiitemssearch)
   - [Get Items by Category](#get-apiitemscategorycategory)
   - [Get Items by Seller](#get-apiitemssellersellerid)
   - [Update Item](#put-apiitemsid)
   - [Delete Item](#delete-apiitemsid)
   - [Mark Item as Sold](#patch-apiitemsidsold)
3. [Users](#-users)
   - [Get Seller Contact](#get-apiuserssellerid)
4. [Health & Documentation](#-health--documentation)
5. [Authentication Guide](#-authentication-guide)
6. [Error Responses](#-error-responses)

---

## 🔐 Authentication

### POST `/api/auth/register`

Register a new student account.

**Auth Required:** ❌ No

**Request Body:**
```json
{
  "username": "string (required, 2-50 chars)",
  "email": "string (required, valid email)",
  "password": "string (required, min 6 chars)"
}
```

**Example Request:**
```json
{
  "username": "john_doe",
  "email": "john@banasthali.in",
  "password": "securePassword123"
}
```

**Success Response (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "userId": "6789abc123def456",
  "username": "john_doe",
  "email": "john@banasthali.in"
}
```

**Error Response (400 Bad Request):**
```json
{
  "message": "Email already exists"
}
```

---

### POST `/api/auth/login`

Authenticate user and get JWT token.

**Auth Required:** ❌ No

**Request Body:**
```json
{
  "email": "string (required, valid email)",
  "password": "string (required)"
}
```

**Example Request:**
```json
{
  "email": "john@banasthali.in",
  "password": "securePassword123"
}
```

**Success Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "userId": "6789abc123def456",
  "username": "john_doe",
  "email": "john@banasthali.in"
}
```

**Error Response (401 Unauthorized):**
```json
{
  "message": "Invalid email or password"
}
```

---

## 🛒 Items (Marketplace)

### POST `/api/items`

Create a new item listing with image upload.

**Auth Required:** ✅ Yes (Bearer Token)

**Content-Type:** `multipart/form-data`

**Form Data:**
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `title` | string | ✅ | Item title |
| `description` | string | ✅ | Item description |
| `price` | number | ✅ | Item price (must be > 0) |
| `category` | string | ❌ | Item category (e.g., "Books", "Electronics") |
| `image` | file | ✅ | Item image (JPEG, PNG) |

**Example cURL:**
```bash
curl -X POST http://localhost:8080/api/items \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
  -F "title=Data Structures Textbook" \
  -F "description=Brand new, never used. 2nd edition." \
  -F "price=250" \
  -F "category=Books" \
  -F "image=@/path/to/image.jpg"
```

**Success Response (201 Created):**
```json
{
  "id": "abc123def456",
  "title": "Data Structures Textbook",
  "description": "Brand new, never used. 2nd edition.",
  "price": 250.00,
  "imageUrl": "/uploads/abc123_image.jpg",
  "category": "Books",
  "sellerId": "6789abc123def456",
  "sellerName": "john_doe",
  "available": true,
  "createdAt": "2026-01-03T10:30:00"
}
```

---

### GET `/api/items`

Get all available item listings.

**Auth Required:** ❌ No

**Query Parameters:** None

**Success Response (200 OK):**
```json
[
  {
    "id": "abc123def456",
    "title": "Data Structures Textbook",
    "description": "Brand new, never used. 2nd edition.",
    "price": 250.00,
    "imageUrl": "/uploads/abc123_image.jpg",
    "category": "Books",
    "sellerId": "6789abc123def456",
    "sellerName": "john_doe",
    "available": true,
    "createdAt": "2026-01-03T10:30:00"
  },
  {
    "id": "def789ghi012",
    "title": "Scientific Calculator",
    "description": "Casio FX-991EX, barely used.",
    "price": 800.00,
    "imageUrl": "/uploads/def789_calc.jpg",
    "category": "Electronics",
    "sellerId": "xyz456abc789",
    "sellerName": "jane_smith",
    "available": true,
    "createdAt": "2026-01-02T15:45:00"
  }
]
```

---

### GET `/api/items/{id}`

Get a specific item by its ID.

**Auth Required:** ❌ No

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | Item ID |

**Success Response (200 OK):**
```json
{
  "id": "abc123def456",
  "title": "Data Structures Textbook",
  "description": "Brand new, never used. 2nd edition.",
  "price": 250.00,
  "imageUrl": "/uploads/abc123_image.jpg",
  "category": "Books",
  "sellerId": "6789abc123def456",
  "sellerName": "john_doe",
  "available": true,
  "createdAt": "2026-01-03T10:30:00"
}
```

**Error Response (404 Not Found):**
```json
{
  "error": "Item not found"
}
```

---

### GET `/api/items/search`

Search items by keyword (searches in title, description, and category).

**Auth Required:** ❌ No

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `query` | string | ✅ | Search keyword |

**Example Request:**
```
GET /api/items/search?query=textbook
```

**Success Response (200 OK):**
```json
[
  {
    "id": "abc123def456",
    "title": "Data Structures Textbook",
    "description": "Brand new, never used. 2nd edition.",
    "price": 250.00,
    "imageUrl": "/uploads/abc123_image.jpg",
    "category": "Books",
    "sellerId": "6789abc123def456",
    "sellerName": "john_doe",
    "available": true,
    "createdAt": "2026-01-03T10:30:00"
  }
]
```

---

### GET `/api/items/category/{category}`

Get items filtered by category.

**Auth Required:** ❌ No

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `category` | string | Category name |

**Example Request:**
```
GET /api/items/category/Books
```

**Success Response (200 OK):**
```json
[
  {
    "id": "abc123def456",
    "title": "Data Structures Textbook",
    "description": "Brand new, never used.",
    "price": 250.00,
    "imageUrl": "/uploads/abc123_image.jpg",
    "category": "Books",
    "sellerId": "6789abc123def456",
    "sellerName": "john_doe",
    "available": true,
    "createdAt": "2026-01-03T10:30:00"
  }
]
```

---

### GET `/api/items/seller/{sellerId}`

Get all items listed by a specific seller.

**Auth Required:** ❌ No

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `sellerId` | string | Seller's user ID |

**Example Request:**
```
GET /api/items/seller/6789abc123def456
```

**Success Response (200 OK):**
```json
[
  {
    "id": "abc123def456",
    "title": "Data Structures Textbook",
    "description": "Brand new, never used.",
    "price": 250.00,
    "imageUrl": "/uploads/abc123_image.jpg",
    "category": "Books",
    "sellerId": "6789abc123def456",
    "sellerName": "john_doe",
    "available": true,
    "createdAt": "2026-01-03T10:30:00"
  }
]
```

---

### PUT `/api/items/{id}`

Update an existing item. Only the seller can update their own items.

**Auth Required:** ✅ Yes (Bearer Token)

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | Item ID |

**Request Body:**
```json
{
  "title": "string (required)",
  "description": "string (required)",
  "price": "number (required, > 0)",
  "category": "string (optional)"
}
```

**Example Request:**
```json
{
  "title": "Data Structures Textbook - Updated",
  "description": "Brand new, never used. 2nd edition. Slight discount!",
  "price": 200.00,
  "category": "Books"
}
```

**Success Response (200 OK):**
```json
{
  "id": "abc123def456",
  "title": "Data Structures Textbook - Updated",
  "description": "Brand new, never used. 2nd edition. Slight discount!",
  "price": 200.00,
  "imageUrl": "/uploads/abc123_image.jpg",
  "category": "Books",
  "sellerId": "6789abc123def456",
  "sellerName": "john_doe",
  "available": true,
  "createdAt": "2026-01-03T10:30:00"
}
```

**Error Response (400 Bad Request):**
```json
{
  "error": "You can only update your own items"
}
```

---

### DELETE `/api/items/{id}`

Delete an item. Only the seller can delete their own items.

**Auth Required:** ✅ Yes (Bearer Token)

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | Item ID |

**Success Response (200 OK):**
```json
{
  "message": "Item deleted successfully"
}
```

**Error Response (400 Bad Request):**
```json
{
  "error": "You can only delete your own items"
}
```

---

### PATCH `/api/items/{id}/sold`

Mark an item as sold (no longer available).

**Auth Required:** ✅ Yes (Bearer Token)

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | Item ID |

**Success Response (200 OK):**
```json
{
  "id": "abc123def456",
  "title": "Data Structures Textbook",
  "description": "Brand new, never used.",
  "price": 250.00,
  "imageUrl": "/uploads/abc123_image.jpg",
  "category": "Books",
  "sellerId": "6789abc123def456",
  "sellerName": "john_doe",
  "available": false,
  "createdAt": "2026-01-03T10:30:00"
}
```

---

## 👤 Users

### GET `/api/users/{sellerId}`

Get seller contact information for initiating communication.

**Auth Required:** ❌ No

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `sellerId` | string | Seller's user ID |

**Example Request:**
```
GET /api/users/6789abc123def456
```

**Success Response (200 OK):**
```json
{
  "id": "6789abc123def456",
  "displayName": "john_doe",
  "email": "john@banasthali.in"
}
```

**Error Response (404 Not Found):**
```json
{
  "error": "User not found"
}
```

---

## 🏥 Health & Documentation

### GET `/actuator/health`

Health check endpoint for monitoring.

**Auth Required:** ❌ No

**Response:**
```json
{
  "status": "UP"
}
```

---

### GET `/actuator/info`

Application information endpoint.

**Auth Required:** ❌ No

---

### GET `/swagger-ui.html`

Swagger UI - Interactive API documentation.

**Auth Required:** ❌ No

---

### GET `/v3/api-docs`

OpenAPI 3.0 specification in JSON format.

**Auth Required:** ❌ No

---

## 🔑 Authentication Guide

### How to Use JWT Tokens

1. **Register or Login** to get your JWT token from the response.

2. **Include the token** in the `Authorization` header for protected endpoints:
   ```
   Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNzA0MjQwMDAwLCJleHAiOjE3MDQzMjY0MDB9.abc123...
   ```

3. **Token expiration:** Tokens expire after 24 hours (configurable via `JWT_EXPIRATION` env var).

4. **Example with cURL:**
   ```bash
   curl -X GET http://localhost:8080/api/items \
     -H "Authorization: Bearer YOUR_JWT_TOKEN"
   ```

5. **Example with JavaScript (fetch):**
   ```javascript
   fetch('/api/items', {
     method: 'POST',
     headers: {
       'Authorization': `Bearer ${token}`,
       'Content-Type': 'application/json'
     },
     body: JSON.stringify(itemData)
   });
   ```

---

## ❌ Error Responses

### Common Error Formats

**Validation Error (400 Bad Request):**
```json
{
  "title": "Title is required",
  "price": "Price must be positive"
}
```

**Authentication Error (401 Unauthorized):**
```json
{
  "error": "Invalid or expired token"
}
```

**Authorization Error (403 Forbidden):**
```json
{
  "error": "You can only update your own items"
}
```

**Not Found (404):**
```json
{
  "error": "Item not found"
}
```

**Server Error (500):**
```json
{
  "error": "Internal server error",
  "message": "Something went wrong"
}
```

---

## 📦 Suggested Categories

When posting items, consider using these standard categories for better organization:

- `Books`
- `Electronics`
- `Clothing`
- `Furniture`
- `Stationery`
- `Sports`
- `Accessories`
- `Others`

---

## 🚀 Quick Start Examples

### Register a New User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@banasthali.in",
    "password": "password123"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@banasthali.in",
    "password": "password123"
  }'
```

### Post an Item
```bash
curl -X POST http://localhost:8080/api/items \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -F "title=Laptop Stand" \
  -F "description=Ergonomic laptop stand, adjustable height" \
  -F "price=500" \
  -F "category=Electronics" \
  -F "image=@./laptop_stand.jpg"
```

### Browse All Items
```bash
curl -X GET http://localhost:8080/api/items
```

### Search Items
```bash
curl -X GET "http://localhost:8080/api/items/search?query=laptop"
```

---

## 📊 Endpoint Summary Table

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/auth/register` | ❌ | Register new user |
| POST | `/api/auth/login` | ❌ | Login user |
| POST | `/api/items` | ✅ | Create item |
| GET | `/api/items` | ❌ | List all items |
| GET | `/api/items/{id}` | ❌ | Get item by ID |
| GET | `/api/items/search` | ❌ | Search items |
| GET | `/api/items/category/{category}` | ❌ | Filter by category |
| GET | `/api/items/seller/{sellerId}` | ❌ | Items by seller |
| PUT | `/api/items/{id}` | ✅ | Update item |
| DELETE | `/api/items/{id}` | ✅ | Delete item |
| PATCH | `/api/items/{id}/sold` | ✅ | Mark as sold |
| GET | `/api/users/{sellerId}` | ❌ | Get seller contact |
| GET | `/swagger-ui.html` | ❌ | API documentation |
| GET | `/actuator/health` | ❌ | Health check |
