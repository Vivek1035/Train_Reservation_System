# 📡 Enhanced REST API Documentation

## Overview
This document provides comprehensive examples of the enhanced Train Reservation System REST APIs with DTOs, validation, pagination, and proper HTTP status codes.

---

## 🎯 Base URL
```
http://localhost:8080/api
```

---

## 📋 Table of Contents
1. [Train Search API](#train-search-api)
2. [Train Management API](#train-management-api)
3. [Booking API](#booking-api)
4. [Reservation History API](#reservation-history-api)

---

## 🚂 Train Search API

### 1. Search Trains Between Stations

**Endpoint:** `POST /api/trains/search`

**Description:** Search for trains between source and destination stations with optional availability filter.

**Request Body:**
```json
{
  "sourceStationId": 1,
  "destinationStationId": 2,
  "journeyDate": "2025-01-15",
  "onlyAvailable": true
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Found 3 trains",
  "data": [
    {
      "id": 1,
      "trainNumber": "12301",
      "trainName": "Rajdhani Express",
      "sourceStationCode": "NDLS",
      "sourceStationName": "New Delhi Railway Station",
      "destinationStationCode": "BCT",
      "destinationStationName": "Mumbai Central",
      "departureTime": "16:55:00",
      "arrivalTime": "09:25:00",
      "duration": "16 hrs 30 mins",
      "availableSeats": 450,
      "startingFare": 1500.00,
      "hasAvailability": true
    },
    {
      "id": 2,
      "trainNumber": "12951",
      "trainName": "Mumbai Rajdhani",
      "sourceStationCode": "NDLS",
      "sourceStationName": "New Delhi Railway Station",
      "destinationStationCode": "BCT",
      "destinationStationName": "Mumbai Central",
      "departureTime": "16:35:00",
      "arrivalTime": "08:35:00",
      "duration": "16 hrs 0 mins",
      "availableSeats": 320,
      "startingFare": 1650.00,
      "hasAvailability": true
    }
  ],
  "timestamp": "2025-12-21T10:30:00"
}
```

**Validation Errors (400 Bad Request):**
```json
{
  "timestamp": "2025-12-21T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation error. Please check the request body.",
  "path": "/api/trains/search",
  "details": [
    "sourceStationId: must not be null",
    "destinationStationId: must not be null"
  ]
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/trains/search \
  -H "Content-Type: application/json" \
  -d '{
    "sourceStationId": 1,
    "destinationStationId": 2,
    "onlyAvailable": true
  }'
```

---

### 2. Get All Trains (Paginated & Sorted)

**Endpoint:** `GET /api/trains?page=0&size=10&sortBy=trainNumber&sortDir=ASC`

**Query Parameters:**
- `active` (optional): Filter by active status (true/false)
- `page` (default: 0): Page number (0-indexed)
- `size` (default: 10): Items per page
- `sortBy` (default: trainNumber): Field to sort by
- `sortDir` (default: ASC): Sort direction (ASC/DESC)

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Request processed successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "trainNumber": "12301",
        "trainName": "Rajdhani Express",
        "sourceStation": {
          "id": 1,
          "stationCode": "NDLS",
          "stationName": "New Delhi Railway Station",
          "city": "New Delhi",
          "state": "Delhi"
        },
        "destinationStation": {
          "id": 2,
          "stationCode": "BCT",
          "stationName": "Mumbai Central",
          "city": "Mumbai",
          "state": "Maharashtra"
        },
        "departureTime": "16:55:00",
        "arrivalTime": "09:25:00",
        "totalSeats": 1000,
        "availableSeats": 450,
        "baseFare": 1500.00,
        "active": true,
        "operatingDays": "MON,TUE,WED,THU,FRI,SAT,SUN",
        "coaches": [
          {
            "id": 1,
            "coachNumber": "A1",
            "coachType": "AC_1A",
            "coachTypeDescription": "1A - First AC",
            "totalSeats": 20,
            "availableSeats": 15,
            "fareMultiplier": 3.0,
            "calculatedFare": 4500.00
          },
          {
            "id": 2,
            "coachNumber": "A2",
            "coachType": "AC_2A",
            "coachTypeDescription": "2A - Second AC",
            "totalSeats": 50,
            "availableSeats": 35,
            "fareMultiplier": 2.0,
            "calculatedFare": 3000.00
          }
        ]
      }
    ],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 25,
    "totalPages": 3,
    "first": true,
    "last": false,
    "empty": false
  },
  "timestamp": "2025-12-21T10:35:00"
}
```

**cURL Example:**
```bash
curl "http://localhost:8080/api/trains?page=0&size=10&sortBy=trainName&sortDir=ASC&active=true"
```

---

### 3. Get Train by ID

**Endpoint:** `GET /api/trains/{id}`

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Request processed successfully",
  "data": {
    "id": 1,
    "trainNumber": "12301",
    "trainName": "Rajdhani Express",
    "sourceStation": {
      "id": 1,
      "stationCode": "NDLS",
      "stationName": "New Delhi Railway Station",
      "city": "New Delhi",
      "state": "Delhi"
    },
    "destinationStation": {
      "id": 2,
      "stationCode": "BCT",
      "stationName": "Mumbai Central",
      "city": "Mumbai",
      "state": "Maharashtra"
    },
    "departureTime": "16:55:00",
    "arrivalTime": "09:25:00",
    "totalSeats": 1000,
    "availableSeats": 450,
    "baseFare": 1500.00,
    "active": true,
    "operatingDays": "MON,TUE,WED,THU,FRI,SAT,SUN"
  },
  "timestamp": "2025-12-21T10:40:00"
}
```

**Error Response (404 Not Found):**
```json
{
  "timestamp": "2025-12-21T10:40:00",
  "status": 404,
  "error": "Resource Not Found",
  "message": "Train not found with id: '999'",
  "path": "/api/trains/999"
}
```

**cURL Example:**
```bash
curl http://localhost:8080/api/trains/1
```

---

### 4. Get Train by Number

**Endpoint:** `GET /api/trains/number/{trainNumber}`

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Request processed successfully",
  "data": {
    "id": 1,
    "trainNumber": "12301",
    "trainName": "Rajdhani Express",
    "sourceStation": {...},
    "destinationStation": {...},
    "departureTime": "16:55:00",
    "arrivalTime": "09:25:00",
    "totalSeats": 1000,
    "availableSeats": 450,
    "baseFare": 1500.00,
    "active": true
  },
  "timestamp": "2025-12-21T10:45:00"
}
```

**cURL Example:**
```bash
curl http://localhost:8080/api/trains/number/12301
```

---

## 🎫 Booking API

### 1. Create New Booking

**Endpoint:** `POST /api/bookings`

**Description:** Create a new train booking with passenger details.

**Request Body:**
```json
{
  "userId": 1,
  "trainId": 1,
  "journeyDate": "2025-01-20",
  "passengers": [
    {
      "passengerName": "John Doe",
      "passengerAge": 35,
      "passengerGender": "MALE",
      "quotaType": "GENERAL",
      "coachId": 1,
      "seatPreference": "WINDOW"
    },
    {
      "passengerName": "Jane Doe",
      "passengerAge": 32,
      "passengerGender": "FEMALE",
      "quotaType": "LADIES",
      "coachId": 1,
      "seatPreference": "WINDOW"
    }
  ],
  "specialRequests": "Prefer lower berth for both passengers"
}
```

**Validation Rules:**
- `userId`: Required, must be positive number
- `trainId`: Required, must be positive number
- `journeyDate`: Required, must be a future date
- `passengers`: Required, 1-6 passengers allowed
- `passengerName`: 2-100 characters
- `passengerAge`: 1-120 years
- `passengerGender`: Must be MALE, FEMALE, or OTHER
- `quotaType`: Must be valid quota type (GENERAL, TATKAL, etc.)

**Success Response (201 Created):**
```json
{
  "success": true,
  "message": "Booking created successfully",
  "data": {
    "id": 100,
    "pnrNumber": "1234567890",
    "status": "CONFIRMED",
    "journeyDate": "2025-01-20",
    "numberOfPassengers": 2,
    "totalFare": 9000.00,
    "remarks": null,
    "bookedAt": "2025-12-21T10:50:00",
    "train": {
      "id": 1,
      "trainNumber": "12301",
      "trainName": "Rajdhani Express",
      "sourceStation": "New Delhi Railway Station",
      "destinationStation": "Mumbai Central"
    },
    "reservations": [
      {
        "id": 150,
        "passengerName": "John Doe",
        "passengerAge": 35,
        "passengerGender": "MALE",
        "quotaType": "GENERAL",
        "coachNumber": "A1",
        "seatNumber": "5",
        "fare": 4500.00
      },
      {
        "id": 151,
        "passengerName": "Jane Doe",
        "passengerAge": 32,
        "passengerGender": "FEMALE",
        "quotaType": "LADIES",
        "coachNumber": "A1",
        "seatNumber": "6",
        "fare": 4500.00
      }
    ],
    "payment": {
      "id": 75,
      "transactionId": "TXN123456789",
      "amount": 9000.00,
      "paymentMethod": "CREDIT_CARD",
      "status": "SUCCESS",
      "paymentDate": "2025-12-21T10:50:30"
    }
  },
  "timestamp": "2025-12-21T10:50:00"
}
```

**Validation Error (400 Bad Request):**
```json
{
  "timestamp": "2025-12-21T10:50:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation error. Please check the request body.",
  "path": "/api/bookings",
  "details": [
    "passengers[0].passengerName: Passenger name is required",
    "passengers[0].passengerAge: Age must be at least 1",
    "journeyDate: Journey date must be in the future"
  ]
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "trainId": 1,
    "journeyDate": "2025-01-20",
    "passengers": [
      {
        "passengerName": "John Doe",
        "passengerAge": 35,
        "passengerGender": "MALE",
        "quotaType": "GENERAL",
        "coachId": 1,
        "seatPreference": "WINDOW"
      }
    ]
  }'
```

---

### 2. Get Booking by PNR

**Endpoint:** `GET /api/bookings/pnr/{pnr}`

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Request processed successfully",
  "data": {
    "id": 100,
    "pnrNumber": "1234567890",
    "status": "CONFIRMED",
    "journeyDate": "2025-01-20",
    "numberOfPassengers": 2,
    "totalFare": 9000.00,
    "remarks": null,
    "bookedAt": "2025-12-21T10:50:00",
    "train": {
      "id": 1,
      "trainNumber": "12301",
      "trainName": "Rajdhani Express",
      "sourceStation": "New Delhi Railway Station",
      "destinationStation": "Mumbai Central"
    },
    "reservations": [
      {
        "id": 150,
        "passengerName": "John Doe",
        "passengerAge": 35,
        "passengerGender": "MALE",
        "quotaType": "GENERAL",
        "coachNumber": "A1",
        "seatNumber": "5",
        "fare": 4500.00
      }
    ],
    "payment": {
      "id": 75,
      "transactionId": "TXN123456789",
      "amount": 9000.00,
      "paymentMethod": "CREDIT_CARD",
      "status": "SUCCESS",
      "paymentDate": "2025-12-21T10:50:30"
    }
  },
  "timestamp": "2025-12-21T11:00:00"
}
```

**cURL Example:**
```bash
curl http://localhost:8080/api/bookings/pnr/1234567890
```

---

## 📚 Reservation History API

### 1. Get User Booking History (Paginated)

**Endpoint:** `GET /api/bookings/user/{userId}/history?page=0&size=10`

**Query Parameters:**
- `page` (default: 0): Page number
- `size` (default: 10): Items per page

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Request processed successfully",
  "data": {
    "content": [
      {
        "id": 102,
        "pnrNumber": "9876543210",
        "status": "CONFIRMED",
        "journeyDate": "2025-01-25",
        "numberOfPassengers": 1,
        "totalFare": 3000.00,
        "bookedAt": "2025-12-20T15:30:00",
        "trainNumber": "12302",
        "trainName": "Shatabdi Express",
        "route": "New Delhi Railway Station -> Jaipur Junction",
        "canCancel": true,
        "isPastJourney": false
      },
      {
        "id": 100,
        "pnrNumber": "1234567890",
        "status": "CONFIRMED",
        "journeyDate": "2025-01-20",
        "numberOfPassengers": 2,
        "totalFare": 9000.00,
        "bookedAt": "2025-12-21T10:50:00",
        "trainNumber": "12301",
        "trainName": "Rajdhani Express",
        "route": "New Delhi Railway Station -> Mumbai Central",
        "canCancel": true,
        "isPastJourney": false
      },
      {
        "id": 85,
        "pnrNumber": "5555555555",
        "status": "COMPLETED",
        "journeyDate": "2025-12-15",
        "numberOfPassengers": 3,
        "totalFare": 7500.00,
        "bookedAt": "2025-12-10T09:20:00",
        "trainNumber": "12303",
        "trainName": "Duronto Express",
        "route": "Mumbai Central -> Chennai Central",
        "canCancel": false,
        "isPastJourney": true
      }
    ],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 15,
    "totalPages": 2,
    "first": true,
    "last": false,
    "empty": false
  },
  "timestamp": "2025-12-21T11:05:00"
}
```

**cURL Example:**
```bash
curl "http://localhost:8080/api/bookings/user/1/history?page=0&size=10"
```

---

### 2. Get User Active Bookings

**Endpoint:** `GET /api/bookings/user/{userId}/active`

**Description:** Get all upcoming/active bookings for a user (future journeys with CONFIRMED or PENDING status).

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Found 2 active bookings",
  "data": [
    {
      "id": 100,
      "pnrNumber": "1234567890",
      "status": "CONFIRMED",
      "journeyDate": "2025-01-20",
      "numberOfPassengers": 2,
      "totalFare": 9000.00,
      "bookedAt": "2025-12-21T10:50:00",
      "trainNumber": "12301",
      "trainName": "Rajdhani Express",
      "route": "New Delhi Railway Station -> Mumbai Central",
      "canCancel": true,
      "isPastJourney": false
    },
    {
      "id": 102,
      "pnrNumber": "9876543210",
      "status": "CONFIRMED",
      "journeyDate": "2025-01-25",
      "numberOfPassengers": 1,
      "totalFare": 3000.00,
      "bookedAt": "2025-12-20T15:30:00",
      "trainNumber": "12302",
      "trainName": "Shatabdi Express",
      "route": "New Delhi Railway Station -> Jaipur Junction",
      "canCancel": true,
      "isPastJourney": false
    }
  ],
  "timestamp": "2025-12-21T11:10:00"
}
```

**cURL Example:**
```bash
curl http://localhost:8080/api/bookings/user/1/active
```

---

### 3. Cancel Booking

**Endpoint:** `PATCH /api/bookings/{id}/cancel`

**Description:** Cancel an existing booking (only if journey date is in future).

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Booking cancelled successfully",
  "data": {
    "id": 100,
    "pnrNumber": "1234567890",
    "status": "CANCELLED",
    "journeyDate": "2025-01-20",
    "numberOfPassengers": 2,
    "totalFare": 9000.00,
    "remarks": "Cancelled by user",
    "bookedAt": "2025-12-21T10:50:00",
    "train": {...},
    "reservations": [...],
    "payment": {...}
  },
  "timestamp": "2025-12-21T11:15:00"
}
```

**Error Response (400 Bad Request):**
```json
{
  "timestamp": "2025-12-21T11:15:00",
  "status": 400,
  "error": "Invalid Operation",
  "message": "Booking is already cancelled",
  "path": "/api/bookings/100/cancel"
}
```

**cURL Example:**
```bash
curl -X PATCH http://localhost:8080/api/bookings/100/cancel
```

---

## 🔧 Admin Train Management API

### 1. Update Seat Availability

**Endpoint:** `PATCH /api/trains/{id}/seats?change=-5`

**Description:** Update train seat availability (admin operation). Use positive number to increase, negative to decrease.

**Query Parameters:**
- `change`: Number of seats to add/subtract (can be negative)

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Seat availability updated",
  "data": {
    "id": 1,
    "trainNumber": "12301",
    "trainName": "Rajdhani Express",
    "totalSeats": 1000,
    "availableSeats": 445,
    "baseFare": 1500.00,
    "active": true
  },
  "timestamp": "2025-12-21T11:20:00"
}
```

**cURL Example:**
```bash
curl -X PATCH "http://localhost:8080/api/trains/1/seats?change=-5"
```

---

### 2. Delete Train

**Endpoint:** `DELETE /api/trains/{id}`

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Train deleted successfully",
  "data": null,
  "timestamp": "2025-12-21T11:25:00"
}
```

**cURL Example:**
```bash
curl -X DELETE http://localhost:8080/api/trains/10
```

---

## 📊 Common Error Responses

### Resource Not Found (404)
```json
{
  "timestamp": "2025-12-21T11:30:00",
  "status": 404,
  "error": "Resource Not Found",
  "message": "Train not found with id: '999'",
  "path": "/api/trains/999"
}
```

### Validation Error (400)
```json
{
  "timestamp": "2025-12-21T11:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation error. Please check the request body.",
  "path": "/api/bookings",
  "details": [
    "userId: must not be null",
    "journeyDate: must be a future date",
    "passengers: size must be between 1 and 6"
  ]
}
```

### Duplicate Resource (409)
```json
{
  "timestamp": "2025-12-21T11:30:00",
  "status": 409,
  "error": "Duplicate Resource",
  "message": "Train already exists with trainNumber: '12301'",
  "path": "/api/trains"
}
```

### Internal Server Error (500)
```json
{
  "timestamp": "2025-12-21T11:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred. Please try again later.",
  "path": "/api/trains/search"
}
```

---

## 🎯 HTTP Status Codes Used

| Code | Description | Usage |
|------|-------------|-------|
| 200 | OK | Successful GET, PATCH requests |
| 201 | Created | Successful POST (resource creation) |
| 400 | Bad Request | Validation errors, invalid input |
| 404 | Not Found | Resource doesn't exist |
| 409 | Conflict | Duplicate resource |
| 500 | Internal Server Error | Unexpected server errors |

---

## 🔍 Query Parameter Guidelines

### Pagination
- `page`: Page number (0-indexed, default: 0)
- `size`: Items per page (default: 10, max: 100)

### Sorting
- `sortBy`: Field name to sort by (default varies by endpoint)
- `sortDir`: Sort direction (`ASC` or `DESC`, default: ASC)

### Filtering
- `active`: Boolean filter for active/inactive resources
- `status`: Filter by status enum values

---

## ✅ Best Practices Implemented

1. **Consistent Response Format**: All responses wrapped in `ApiResponse`
2. **Proper HTTP Status Codes**: Semantic status codes for all operations
3. **Input Validation**: Bean validation with detailed error messages
4. **Pagination Support**: Pageable endpoints for large datasets
5. **Error Handling**: Global exception handler with structured error responses
6. **DTOs**: Separation between entities and API contracts
7. **Documentation**: Clear, detailed API documentation with examples

---

## 📚 Additional Notes

- All timestamps are in ISO-8601 format
- Dates are in `YYYY-MM-DD` format
- Times are in `HH:mm:ss` format
- All monetary values are in decimal format (e.g., 1500.00)
- Pagination is 0-indexed (first page = 0)
- Sort direction is case-insensitive (ASC/asc, DESC/desc)

---

**Generated:** December 21, 2025  
**API Version:** 1.0  
**Backend:** Spring Boot 3.2.1
