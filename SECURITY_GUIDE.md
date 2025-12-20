# 🔐 Authentication & Authorization Guide

## Overview
The Train Reservation System uses **JWT-based authentication** with **OAuth2 (Google)** support and **role-based authorization** (USER, ADMIN). Passwords are securely stored using **BCrypt** with 12 rounds of hashing.

---

## 📋 Table of Contents
1. [Security Architecture](#security-architecture)
2. [JWT Authentication Flow](#jwt-authentication-flow)
3. [OAuth2 Google Login Flow](#oauth2-google-login-flow)
4. [Token Refresh Flow](#token-refresh-flow)
5. [Role-Based Authorization](#role-based-authorization)
6. [API Endpoints](#api-endpoints)
7. [Frontend Integration](#frontend-integration)
8. [Configuration](#configuration)

---

## 🏗️ Security Architecture

### Components

```
┌─────────────────────────────────────────────────────────────┐
│                    Security Architecture                     │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  ┌────────────┐      ┌──────────────────┐                   │
│  │  Frontend  │─────►│ SecurityConfig    │                   │
│  │  (React)   │      │ - CORS            │                   │
│  └────────────┘      │ - JWT Filter      │                   │
│        │             │ - OAuth2          │                   │
│        │             └──────────────────┘                   │
│        │                      │                              │
│        ▼                      ▼                              │
│  ┌────────────────────────────────────┐                     │
│  │  JwtAuthenticationFilter           │                     │
│  │  - Extract JWT from header         │                     │
│  │  - Validate token                  │                     │
│  │  - Set SecurityContext             │                     │
│  └────────────────────────────────────┘                     │
│                      │                                        │
│        ┌─────────────┴─────────────┐                        │
│        ▼                           ▼                        │
│  ┌──────────┐              ┌────────────────┐              │
│  │  JwtUtil │              │ UserDetailsService│             │
│  │  - Generate │             │ - Load user      │             │
│  │  - Validate │             │ - Check roles    │             │
│  │  - Parse   │             └────────────────┘              │
│  └──────────┘                      │                         │
│                                    ▼                         │
│                            ┌──────────────┐                 │
│                            │  Controllers  │                 │
│                            │  @PreAuthorize│                 │
│                            └──────────────┘                 │
└─────────────────────────────────────────────────────────────┘
```

### Key Classes

| Class | Purpose |
|-------|---------|
| **SecurityConfig** | Configures security filter chain, CORS, OAuth2, BCrypt |
| **JwtUtil** | JWT token generation, validation, parsing |
| **JwtAuthenticationFilter** | Intercepts requests, validates JWT, sets authentication |
| **UserDetailsService** | Loads user details for authentication |
| **OAuth2AuthenticationSuccessHandler** | Handles successful OAuth2 login |
| **AuthService** | Business logic for login, register, token refresh |
| **AuthController** | REST endpoints for authentication |

---

## 🔑 JWT Authentication Flow

### 1. Registration Flow

```
┌────────┐                 ┌────────────┐                 ┌──────────┐
│ Client │                 │  Backend   │                 │ Database │
└───┬────┘                 └─────┬──────┘                 └────┬─────┘
    │                            │                             │
    │ POST /api/auth/register    │                             │
    │ {email, password, ...}     │                             │
    ├───────────────────────────►│                             │
    │                            │                             │
    │                            │ 1. Validate input           │
    │                            │ 2. Check email exists       │
    │                            ├────────────────────────────►│
    │                            │◄────────────────────────────┤
    │                            │                             │
    │                            │ 3. Hash password (BCrypt)   │
    │                            │ 4. Save user                │
    │                            ├────────────────────────────►│
    │                            │◄────────────────────────────┤
    │                            │                             │
    │                            │ 5. Generate JWT tokens      │
    │                            │    - Access Token (24h)     │
    │                            │    - Refresh Token (7d)     │
    │                            │                             │
    │ 201 Created                │                             │
    │ {accessToken, refreshToken}│                             │
    │◄───────────────────────────┤                             │
    │                            │                             │
```

**Request Example:**
```bash
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "fullName": "John Doe",
  "email": "john@example.com",
  "phoneNumber": "9876543210",
  "password": "SecurePass123!",
  "role": "USER"
}
```

**Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400,
    "user": {
      "id": 1,
      "fullName": "John Doe",
      "email": "john@example.com",
      "phoneNumber": "9876543210",
      "role": "USER",
      "active": true,
      "createdAt": "2025-12-21T10:00:00"
    }
  },
  "timestamp": "2025-12-21T10:00:00"
}
```

### 2. Login Flow

```
┌────────┐                 ┌────────────┐                 ┌──────────┐
│ Client │                 │  Backend   │                 │ Database │
└───┬────┘                 └─────┬──────┘                 └────┬─────┘
    │                            │                             │
    │ POST /api/auth/login       │                             │
    │ {email, password}          │                             │
    ├───────────────────────────►│                             │
    │                            │                             │
    │                            │ 1. Load user by email       │
    │                            ├────────────────────────────►│
    │                            │◄────────────────────────────┤
    │                            │                             │
    │                            │ 2. Verify password (BCrypt) │
    │                            │ 3. Authenticate             │
    │                            │                             │
    │                            │ 4. Generate JWT tokens      │
    │                            │    - Access Token (24h)     │
    │                            │    - Refresh Token (7d)     │
    │                            │                             │
    │ 200 OK                     │                             │
    │ {accessToken, refreshToken}│                             │
    │◄───────────────────────────┤                             │
    │                            │                             │
```

**Request:**
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "SecurePass123!"
}
```

### 3. Authenticated Request Flow

```
┌────────┐                 ┌──────────────────┐         ┌────────────┐
│ Client │                 │ JWT Auth Filter   │         │ Controller │
└───┬────┘                 └────────┬──────────┘         └─────┬──────┘
    │                               │                          │
    │ GET /api/bookings             │                          │
    │ Authorization: Bearer <JWT>   │                          │
    ├──────────────────────────────►│                          │
    │                               │                          │
    │                               │ 1. Extract JWT token     │
    │                               │ 2. Validate signature    │
    │                               │ 3. Check expiration      │
    │                               │ 4. Extract username      │
    │                               │ 5. Load UserDetails      │
    │                               │ 6. Set SecurityContext   │
    │                               │                          │
    │                               │ Request with Auth        │
    │                               ├─────────────────────────►│
    │                               │                          │
    │                               │                          │ 7. Check @PreAuthorize
    │                               │                          │ 8. Execute method
    │                               │                          │
    │                               │         Response         │
    │                               │◄─────────────────────────┤
    │         200 OK                │                          │
    │◄──────────────────────────────┤                          │
    │                               │                          │
```

**Request:**
```bash
GET http://localhost:8080/api/bookings/user/1/history
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 🔐 OAuth2 Google Login Flow

### Configuration Steps

1. **Google Cloud Console Setup**
   - Go to [Google Cloud Console](https://console.cloud.google.com)
   - Create a new project
   - Enable Google+ API
   - Create OAuth2 credentials
   - Add authorized redirect URI: `http://localhost:8080/login/oauth2/code/google`

2. **Update application.properties**
   ```properties
   spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
   spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
   ```

### OAuth2 Flow Diagram

```
┌────────┐         ┌────────┐         ┌────────────┐         ┌──────────┐
│Frontend│         │Backend │         │   Google   │         │ Database │
└───┬────┘         └────┬───┘         └─────┬──────┘         └────┬─────┘
    │                   │                   │                     │
    │ 1. Click "Login   │                   │                     │
    │    with Google"   │                   │                     │
    ├──────────────────►│                   │                     │
    │                   │                   │                     │
    │                   │ 2. Redirect to    │                     │
    │                   │    Google OAuth   │                     │
    │◄──────────────────┤──────────────────►│                     │
    │                   │                   │                     │
    │ 3. User logs in   │                   │                     │
    │    and grants     │                   │                     │
    │    permission     │                   │                     │
    ├───────────────────────────────────────►│                     │
    │                   │                   │                     │
    │                   │ 4. Auth code      │                     │
    │◄──────────────────┤◄──────────────────┤                     │
    │                   │                   │                     │
    │                   │ 5. Exchange code  │                     │
    │                   │    for token      │                     │
    │                   ├──────────────────►│                     │
    │                   │                   │                     │
    │                   │ 6. User info      │                     │
    │                   │◄──────────────────┤                     │
    │                   │                   │                     │
    │                   │ 7. Find or create user                  │
    │                   ├─────────────────────────────────────────►│
    │                   │◄─────────────────────────────────────────┤
    │                   │                   │                     │
    │                   │ 8. Generate JWT   │                     │
    │                   │                   │                     │
    │ 9. Redirect to    │                   │                     │
    │    frontend       │                   │                     │
    │    with JWT token │                   │                     │
    │◄──────────────────┤                   │                     │
    │                   │                   │                     │
```

### Frontend Initiation

```javascript
// React component
const handleGoogleLogin = () => {
  window.location.href = 'http://localhost:8080/oauth2/authorization/google';
};
```

### Backend Callback Handling

After successful OAuth2 authentication, user is redirected to:
```
http://localhost:5173/oauth2/redirect?token=<JWT>&email=<EMAIL>&name=<NAME>
```

**Frontend handling:**
```javascript
// OAuth2 redirect handler
useEffect(() => {
  const params = new URLSearchParams(window.location.search);
  const token = params.get('token');
  
  if (token) {
    localStorage.setItem('accessToken', token);
    // Redirect to dashboard
    navigate('/dashboard');
  }
}, []);
```

---

## 🔄 Token Refresh Flow

```
┌────────┐                 ┌────────────┐
│ Client │                 │  Backend   │
└───┬────┘                 └─────┬──────┘
    │                            │
    │ API Request with expired   │
    │ access token               │
    ├───────────────────────────►│
    │                            │
    │ 401 Unauthorized           │
    │ (Token expired)            │
    │◄───────────────────────────┤
    │                            │
    │ POST /api/auth/refresh-token│
    │ {refreshToken}             │
    ├───────────────────────────►│
    │                            │
    │                            │ 1. Validate refresh token
    │                            │ 2. Check if it's refresh type
    │                            │ 3. Extract username
    │                            │ 4. Generate new access token
    │                            │
    │ 200 OK                     │
    │ {accessToken, ...}         │
    │◄───────────────────────────┤
    │                            │
    │ Retry original request     │
    │ with new token             │
    ├───────────────────────────►│
    │                            │
    │ 200 OK                     │
    │◄───────────────────────────┤
    │                            │
```

**Request:**
```bash
POST http://localhost:8080/api/auth/refresh-token
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response:**
```json
{
  "success": true,
  "message": "Token refreshed successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400,
    "user": {...}
  }
}
```

---

## 🛡️ Role-Based Authorization

### User Roles

| Role | Description | Permissions |
|------|-------------|-------------|
| **USER** | Regular user | Search trains, book tickets, view own bookings |
| **ADMIN** | Administrator | All USER permissions + manage trains, stations, users |

### Endpoint Protection

#### Public Endpoints (No Authentication)
- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/refresh-token`
- `POST /api/trains/search`
- `GET /api/trains/{id}`
- `GET /api/trains/number/{trainNumber}`
- `GET /api/stations`
- `GET /api/stations/{id}`

#### User Endpoints (Authentication Required)
- `GET /api/bookings/**` - All booking endpoints
- `POST /api/bookings` - Create booking
- `PATCH /api/bookings/{id}/cancel` - Cancel booking

#### Admin Endpoints (ADMIN Role Required)
- `POST /api/trains` - Create train
- `PUT /api/trains/**` - Update train
- `PATCH /api/trains/**` - Update train availability
- `DELETE /api/trains/**` - Delete train
- `POST /api/stations` - Create station
- `PUT /api/stations/**` - Update station
- `DELETE /api/stations/**` - Delete station
- `GET /api/users/**` - Manage users
- `POST /api/users` - Create user
- `PUT /api/users/**` - Update user
- `DELETE /api/users/**` - Delete user

### Authorization Implementation

```java
// Class-level authorization (all endpoints require ADMIN)
@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    // All methods require ADMIN role
}

// Method-level authorization
@PostMapping
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<ApiResponse<TrainResponse>> createTrain(...) {
    // Only ADMIN can access
}
```

### JWT Token Structure

```json
{
  "sub": "john@example.com",
  "roles": ["ROLE_USER"],
  "iat": 1640000000,
  "exp": 1640086400
}
```

---

## 📡 API Endpoints

### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login user | No |
| POST | `/api/auth/refresh-token` | Refresh access token | No |
| GET | `/api/auth/me` | Get current user | Yes |
| POST | `/api/auth/logout` | Logout (client-side) | No |

### OAuth2 Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/oauth2/authorization/google` | Initiate Google OAuth2 flow |
| GET | `/login/oauth2/code/google` | OAuth2 callback (handled by Spring) |

---

## 💻 Frontend Integration

### Setting Up Axios

```javascript
// api/axios.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json'
  }
});

// Request interceptor - Add JWT token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor - Handle 401 and refresh token
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // If 401 and not already retried
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const refreshToken = localStorage.getItem('refreshToken');
        const response = await axios.post(
          'http://localhost:8080/api/auth/refresh-token',
          { refreshToken }
        );

        const { accessToken } = response.data.data;
        localStorage.setItem('accessToken', accessToken);

        // Retry original request
        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
        return api(originalRequest);
      } catch (refreshError) {
        // Refresh failed - redirect to login
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export default api;
```

### React Authentication Context

```javascript
// context/AuthContext.jsx
import { createContext, useState, useEffect } from 'react';
import api from '../api/axios';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    checkAuth();
  }, []);

  const checkAuth = async () => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      try {
        const response = await api.get('/auth/me');
        setUser(response.data.data);
      } catch (error) {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
      }
    }
    setLoading(false);
  };

  const login = async (email, password) => {
    const response = await api.post('/auth/login', { email, password });
    const { accessToken, refreshToken, user } = response.data.data;
    
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
    setUser(user);
    
    return user;
  };

  const register = async (userData) => {
    const response = await api.post('/auth/register', userData);
    const { accessToken, refreshToken, user } = response.data.data;
    
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
    setUser(user);
    
    return user;
  };

  const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, loading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
```

### Protected Route Component

```javascript
// components/ProtectedRoute.jsx
import { Navigate } from 'react-router-dom';
import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';

const ProtectedRoute = ({ children, requiredRole }) => {
  const { user, loading } = useContext(AuthContext);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!user) {
    return <Navigate to="/login" />;
  }

  if (requiredRole && user.role !== requiredRole) {
    return <Navigate to="/unauthorized" />;
  }

  return children;
};

export default ProtectedRoute;
```

### Usage in App

```javascript
// App.jsx
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          
          {/* Protected routes */}
          <Route
            path="/bookings"
            element={
              <ProtectedRoute>
                <BookingsPage />
              </ProtectedRoute>
            }
          />
          
          {/* Admin-only routes */}
          <Route
            path="/admin/trains"
            element={
              <ProtectedRoute requiredRole="ADMIN">
                <AdminTrainsPage />
              </ProtectedRoute>
            }
          />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
```

---

## ⚙️ Configuration

### application.properties

```properties
# JWT Configuration
jwt.secret=your-256-bit-secret-key-change-this-in-production-min-32-chars
jwt.expiration=86400000           # 24 hours in milliseconds
jwt.refresh-expiration=604800000  # 7 days in milliseconds

# OAuth2 Configuration (Google)
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=profile,email
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}

# Frontend URL (for OAuth2 redirect)
frontend.url=http://localhost:5173

# CORS Configuration (handled in SecurityConfig)
# Allowed origins: localhost:5173, localhost:3000, localhost:4173
```

### Security Configuration Highlights

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // 12 rounds
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Allows React frontend to communicate
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",
            "http://localhost:3000"
        ));
    }
}
```

---

## 🔒 Security Best Practices

### Implemented

✅ **BCrypt Password Hashing** - 12 rounds of hashing
✅ **JWT Token-based Authentication** - Stateless authentication
✅ **Refresh Token Pattern** - Long-lived refresh tokens
✅ **Role-Based Access Control** - USER and ADMIN roles
✅ **CORS Protection** - Configured allowed origins
✅ **OAuth2 Integration** - Google authentication
✅ **Password Validation** - Strong password requirements
✅ **Token Expiration** - 24h access, 7d refresh tokens

### Recommendations

🔐 **Change JWT Secret** - Use strong 256-bit secret in production
🔐 **Use HTTPS** - Enable SSL/TLS in production
🔐 **Token Blacklist** - Implement logout token invalidation
🔐 **Rate Limiting** - Add rate limiting for auth endpoints
🔐 **Account Lockout** - Implement failed login attempt limits
🔐 **2FA** - Add two-factor authentication for sensitive operations
🔐 **Audit Logging** - Log authentication events
🔐 **Environment Variables** - Store secrets in environment variables

---

## 📝 Summary

### What's Implemented

1. ✅ **JWT Authentication** - Token-based stateless auth
2. ✅ **User Registration** - With validation and BCrypt
3. ✅ **User Login** - Email/password authentication
4. ✅ **Token Refresh** - Automatic token renewal
5. ✅ **OAuth2 Google Login** - Social authentication
6. ✅ **Role-Based Authorization** - USER/ADMIN access control
7. ✅ **Protected Endpoints** - Method-level security
8. ✅ **CORS Configuration** - Frontend integration support
9. ✅ **Password Encryption** - BCrypt with 12 rounds

### Token Lifetimes

- **Access Token**: 24 hours (86400000 ms)
- **Refresh Token**: 7 days (604800000 ms)

### Next Steps for Frontend

1. Install axios: `npm install axios`
2. Create axios instance with interceptors
3. Implement AuthContext for state management
4. Create Login/Register components
5. Implement OAuth2 redirect handler
6. Add ProtectedRoute wrapper
7. Store tokens in localStorage
8. Handle 401 errors with token refresh

---

**Last Updated:** December 21, 2025  
**Spring Boot Version:** 3.2.1  
**Spring Security Version:** 6.x  
**JWT Library:** JJWT 0.12.3
