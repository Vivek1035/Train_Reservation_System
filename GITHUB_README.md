# 🚂 Train Reservation System

<div align="center">

![Train Reservation System](https://img.shields.io/badge/Train-Reservation-blue?style=for-the-badge&logo=train&logoColor=white)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen?style=for-the-badge&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.2.0-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://reactjs.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)

**A modern, full-stack train ticket booking platform with secure authentication, payment processing, and real-time seat selection.**

[Features](#-features) •
[Tech Stack](#-technology-stack) •
[Quick Start](#-quick-start) •
[Documentation](#-documentation) •
[Screenshots](#-screenshots) •
[API Docs](#-api-documentation)

</div>

---

## 🌟 Overview

The **Train Reservation System** is an enterprise-grade web application designed to streamline train ticket booking. Built with Spring Boot and React, it provides a seamless experience for users to search trains, book tickets, make payments, and manage reservations. Administrators have full control over train schedules, stations, and system operations.

### ✨ Key Highlights

- 🔐 **Secure Authentication**: JWT + OAuth2 (Google) with BCrypt password encryption
- 💳 **Payment Integration**: Simulated payment gateway with retry mechanism
- 🎫 **Real-time Seat Selection**: Interactive coach layout with availability status
- 👥 **Role-Based Access**: Separate user and admin dashboards
- 📱 **Responsive Design**: Mobile-first UI built with Tailwind CSS
- 🚀 **RESTful APIs**: 30+ endpoints with comprehensive documentation
- ⚡ **Modern Stack**: Spring Boot 3.2, React 18, PostgreSQL 15

---

## 🎯 Features

### For Users

#### 🔍 **Train Search & Booking**
- Search trains by source, destination, and date
- View real-time seat availability
- Interactive seat selection with visual coach layout
- Multiple coach types (Sleeper, AC, General)
- Instant booking confirmation with PNR generation

#### 💰 **Secure Payments**
- Card payment processing
- Test card support for development
- Payment retry mechanism for failed transactions
- Transaction history tracking
- Automatic refund on cancellation

#### 📊 **Booking Management**
- View all past and upcoming bookings
- Real-time booking status tracking (Pending, Confirmed, Cancelled)
- Easy cancellation with instant refunds
- Download ticket receipts
- Email notifications (coming soon)

#### 🔐 **Authentication**
- Email/Password registration and login
- Google OAuth2 single sign-on
- Automatic token refresh for seamless sessions
- Secure password reset (coming soon)

### For Administrators

#### 🚆 **Train Management**
- Add, edit, and delete train schedules
- Manage train routes and timings
- Configure coach types and seat layouts
- Set pricing for different classes
- Real-time availability monitoring

#### 🏢 **Station Management**
- Create and manage stations
- Update station codes and locations
- Configure operational status

#### 📈 **Dashboard & Analytics**
- Total trains, users, and bookings statistics
- Recent booking activity
- Revenue tracking (coming soon)
- User engagement metrics (coming soon)

---

## 🛠 Technology Stack

### Backend

| Technology | Version | Purpose |
|------------|---------|---------|
| **Spring Boot** | 3.2.1 | Application framework |
| **Spring Security** | 6.x | Authentication & authorization |
| **Spring Data JPA** | 3.2.x | ORM and database operations |
| **PostgreSQL** | 15+ | Relational database |
| **JJWT** | 0.12.3 | JWT token generation/validation |
| **BCrypt** | - | Password hashing |
| **Maven** | 3.8+ | Build tool |
| **Java** | 17/21 | Programming language |

### Frontend

| Technology | Version | Purpose |
|------------|---------|---------|
| **React** | 18.2.0 | UI library |
| **Vite** | 5.1.0 | Build tool & dev server |
| **React Router** | 6.22.0 | Client-side routing |
| **Axios** | 1.6.7 | HTTP client |
| **Tailwind CSS** | 3.4.1 | Utility-first styling |
| **Lucide React** | 0.315.0 | Icon library |
| **date-fns** | 3.3.1 | Date manipulation |

### DevOps & Tools

- **Git**: Version control
- **PostgreSQL**: Database management
- **Postman**: API testing
- **VS Code**: Development environment

---

## 🚀 Quick Start

### Prerequisites

Before you begin, ensure you have the following installed:

- **Java**: JDK 17 or 21
- **Maven**: 3.8+
- **Node.js**: 18+
- **npm**: 9+
- **PostgreSQL**: 15+
- **Git**: Latest version

### Installation

#### 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-username/train-reservation-system.git
cd train-reservation-system
```

#### 2️⃣ Database Setup

```bash
# Login to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE train_reservation_db;

# Exit psql
\q

# Run initialization script
psql -U postgres -d train_reservation_db -f database/init.sql
```

#### 3️⃣ Backend Configuration

```bash
cd backend

# Edit application.properties
# Replace database credentials and JWT secret
nano src/main/resources/application.properties
```

**Important**: Update these properties:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/train_reservation_db
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

# JWT Secret (Generate a strong secret)
jwt.secret=YOUR_256_BIT_SECRET_KEY_HERE

# OAuth2 Google (Get from Google Cloud Console)
spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET
```

#### 4️⃣ Start Backend

```bash
# Build and run
mvn clean install
mvn spring-boot:run

# Backend runs on http://localhost:8080
```

#### 5️⃣ Frontend Configuration

```bash
cd ../frontend

# Install dependencies
npm install

# Create .env file
cp .env.example .env

# Edit .env (optional - defaults to localhost:8080)
nano .env
```

#### 6️⃣ Start Frontend

```bash
npm run dev

# Frontend runs on http://localhost:5173
```

#### 7️⃣ Access the Application

- **Frontend**: http://localhost:5173
- **Backend API**: http://localhost:8080/api
- **Swagger UI** (if configured): http://localhost:8080/swagger-ui.html

### Test Credentials

#### User Account
```
Email: user@example.com
Password: User@123
```

#### Admin Account
```
Email: admin@example.com
Password: Admin@123
```

#### Test Payment Cards
```
Success: 4111 1111 1111 1111
Failure: 4000 0000 0000 0002
CVV: Any 3 digits
Expiry: Any future date
```

---

## 📚 Documentation

### Available Guides

| Document | Description |
|----------|-------------|
| [**API Documentation**](documentation/API_DOCUMENTATION.md) | Complete REST API reference with examples |
| [**Backend README**](documentation/BACKEND_README.md) | Backend architecture and setup guide |
| [**Frontend README**](frontend/README.md) | Frontend components and features |
| [**Quick Start Guide**](documentation/QUICKSTART.md) | 5-minute setup tutorial |
| [**Security Guide**](documentation/SECURITY_GUIDE.md) | Authentication and authorization details |
| [**Payment Module**](documentation/PAYMENT_MODULE.md) | Payment processing implementation |
| [**Implementation Summary**](frontend/IMPLEMENTATION_SUMMARY.md) | Technical implementation details |

### Project Structure

```
train-reservation-system/
├── backend/                      # Spring Boot Application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/trainreservation/
│   │   │   │   ├── config/       # Configuration classes
│   │   │   │   ├── controller/   # REST Controllers
│   │   │   │   ├── dto/          # Data Transfer Objects
│   │   │   │   ├── entity/       # JPA Entities
│   │   │   │   ├── repository/   # Spring Data Repositories
│   │   │   │   ├── security/     # Security components
│   │   │   │   └── service/      # Business logic
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/                 # Unit & Integration tests
│   └── pom.xml
│
├── frontend/                     # React Application
│   ├── src/
│   │   ├── components/           # Reusable UI components
│   │   │   ├── common/           # Buttons, Inputs, Cards, etc.
│   │   │   └── layout/           # Navbar, Footer
│   │   ├── contexts/             # React Context (Auth)
│   │   ├── pages/                # Page components
│   │   │   ├── auth/             # Login, Register
│   │   │   ├── bookings/         # Booking management
│   │   │   ├── trains/           # Train search & seats
│   │   │   └── admin/            # Admin dashboard
│   │   ├── services/             # API service layer
│   │   └── App.jsx               # Root component
│   ├── package.json
│   └── vite.config.js
│
├── database/
│   └── init.sql                  # Database initialization
│
├── documentation/                # Comprehensive docs
│   ├── API_DOCUMENTATION.md
│   ├── BACKEND_README.md
│   ├── SECURITY_GUIDE.md
│   └── PAYMENT_MODULE.md
│
└── README.md                     # This file
```

---

## 📸 Screenshots

### 🏠 Landing Page
*Clean, modern homepage with hero section and feature highlights*

![Landing Page](screenshots/landing-page.png)

### 🔍 Train Search
*Search trains with source, destination, and date filters*

![Train Search](screenshots/train-search.png)

### 🪑 Seat Selection
*Interactive coach layout with real-time seat availability*

![Seat Selection](screenshots/seat-selection.png)

### 💳 Payment Processing
*Secure payment form with test card support*

![Payment](screenshots/payment.png)

### 📋 Booking History
*User dashboard showing all bookings with status tracking*

![My Bookings](screenshots/my-bookings.png)

### 👨‍💼 Admin Dashboard
*Administrative panel with statistics and management tools*

![Admin Dashboard](screenshots/admin-dashboard.png)

### 🚆 Train Management
*CRUD operations for train schedules and routes*

![Manage Trains](screenshots/manage-trains.png)

### 📱 Mobile View
*Fully responsive design on mobile devices*

![Mobile View](screenshots/mobile-view.png)

---

## 🔌 API Documentation

### Authentication Endpoints

```http
POST   /api/auth/register          # Register new user
POST   /api/auth/login             # Login with credentials
POST   /api/auth/refresh           # Refresh access token
GET    /api/auth/me                # Get current user
GET    /api/auth/google            # Google OAuth2 login URL
```

### Train Endpoints

```http
GET    /api/trains/search          # Search trains
GET    /api/trains/{id}            # Get train by ID
GET    /api/trains                 # Get all trains (Admin)
POST   /api/trains                 # Create train (Admin)
PUT    /api/trains/{id}            # Update train (Admin)
DELETE /api/trains/{id}            # Delete train (Admin)
GET    /api/trains/{id}/seats      # Get available seats
```

### Booking Endpoints

```http
POST   /api/bookings               # Create new booking
GET    /api/bookings/{id}          # Get booking by ID
GET    /api/bookings/user/{userId} # Get user bookings
GET    /api/bookings/pnr/{pnr}     # Get booking by PNR
PUT    /api/bookings/{id}/cancel   # Cancel booking
GET    /api/bookings               # Get all bookings (Admin)
```

### Payment Endpoints

```http
POST   /api/payments/process       # Process payment
POST   /api/payments/retry         # Retry failed payment
GET    /api/payments/{transactionId} # Get payment details
GET    /api/payments/booking/{bookingId} # Get payment by booking
POST   /api/payments/refund        # Refund payment (Admin)
```

### Station Endpoints

```http
GET    /api/stations               # Get all stations
GET    /api/stations/{id}          # Get station by ID
POST   /api/stations               # Create station (Admin)
PUT    /api/stations/{id}          # Update station (Admin)
DELETE /api/stations/{id}          # Delete station (Admin)
```

### Request Example

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "User@123"
  }'
```

### Response Example

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "fullName": "John Doe",
    "role": "USER"
  }
}
```

For complete API documentation with request/response schemas, see [API_DOCUMENTATION.md](documentation/API_DOCUMENTATION.md).

---

## 🏗 Architecture

### System Architecture

```
┌──────────────┐         HTTP/HTTPS          ┌──────────────┐
│              │    ← REST API Calls →       │              │
│   React      │                             │  Spring Boot │
│   Frontend   │    ← JWT in Headers →       │   Backend    │
│  (Port 5173) │                             │  (Port 8080) │
└──────────────┘                             └───────┬──────┘
                                                     │
                                                     │ JDBC
                                                     │
                                             ┌───────▼──────┐
                                             │              │
                                             │  PostgreSQL  │
                                             │   Database   │
                                             │              │
                                             └──────────────┘
```

### Backend Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Security Layer                       │
│  • JWT Authentication Filter                            │
│  • OAuth2 Success Handler                               │
│  • CORS Configuration                                   │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│                 Controller Layer                        │
│  • REST Endpoints                                       │
│  • Request Validation                                   │
│  • Response Formatting                                  │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│                  Service Layer                          │
│  • Business Logic                                       │
│  • Transaction Management                               │
│  • DTO Mapping                                          │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│                Repository Layer                         │
│  • Spring Data JPA                                      │
│  • Custom Queries                                       │
│  • Database Operations                                  │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│                    Database                             │
│  • PostgreSQL                                           │
│  • 10+ Tables with Relationships                        │
└─────────────────────────────────────────────────────────┘
```

### Frontend Architecture

```
┌─────────────────────────────────────────────────────────┐
│                      App.jsx                            │
│  • BrowserRouter                                        │
│  • AuthProvider (Context)                               │
└────────────────────┬────────────────────────────────────┘
                     │
        ┌────────────┼────────────┐
        │            │            │
        ▼            ▼            ▼
   ┌────────┐  ┌─────────┐  ┌─────────┐
   │ Layout │  │  Routes │  │Services │
   │ Components│ │(Pages) │  │  Layer  │
   └────────┘  └─────────┘  └─────────┘
```

---

## 🔒 Security Features

### Authentication & Authorization

- **JWT Tokens**: Stateless authentication with access & refresh tokens
- **BCrypt Hashing**: Passwords hashed with 12 rounds
- **OAuth2**: Google Sign-In integration
- **Role-Based Access**: USER and ADMIN roles with method-level security
- **Token Refresh**: Automatic token renewal on expiration

### API Security

- **CORS**: Configured for allowed origins
- **CSRF Protection**: Disabled for stateless JWT approach
- **Input Validation**: Bean Validation with custom constraints
- **SQL Injection Prevention**: Parameterized queries via JPA
- **XSS Protection**: Frontend sanitization

### Data Security

- **HTTPS**: SSL/TLS encryption in production
- **Secure Headers**: X-Frame-Options, X-XSS-Protection
- **Password Policy**: Minimum length, complexity requirements
- **Session Management**: Stateless with JWT

---

## 🧪 Testing

### Running Tests

```bash
# Backend tests
cd backend
mvn test

# Frontend tests
cd frontend
npm test

# Integration tests
mvn verify
```

### Test Coverage

- Unit Tests: Service layer logic
- Integration Tests: API endpoints with Testcontainers
- E2E Tests: Complete user flows (coming soon)

---

## 🚢 Deployment

### Backend Deployment

#### Using JAR

```bash
mvn clean package
java -jar target/train-reservation-backend-1.0.0.jar
```

#### Using Docker

```bash
# Build image
docker build -t train-reservation-backend .

# Run container
docker run -p 8080:8080 \
  -e DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/train_reservation_db \
  -e DATABASE_USERNAME=postgres \
  -e DATABASE_PASSWORD=yourpassword \
  -e JWT_SECRET=your-secret-key \
  train-reservation-backend
```

### Frontend Deployment

#### Build for Production

```bash
cd frontend
npm run build
```

#### Deploy to Vercel/Netlify

```bash
# Install Vercel CLI
npm i -g vercel

# Deploy
vercel --prod
```

### Environment Variables

Create a `.env` file in production:

```env
# Backend
DATABASE_URL=jdbc:postgresql://your-db-host:5432/train_reservation_db
DATABASE_USERNAME=your-db-user
DATABASE_PASSWORD=your-db-password
JWT_SECRET=your-256-bit-secret-key
JWT_EXPIRATION=86400000
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
FRONTEND_URL=https://your-frontend-domain.com

# Frontend
VITE_API_BASE_URL=https://your-backend-domain.com
```

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Commit your changes**: `git commit -m 'Add amazing feature'`
4. **Push to the branch**: `git push origin feature/amazing-feature`
5. **Open a Pull Request**

### Coding Standards

- Follow Java Code Conventions for backend
- Use ESLint/Prettier for frontend
- Write meaningful commit messages
- Add tests for new features
- Update documentation

---

## 🐛 Troubleshooting

### Common Issues

**Issue**: Backend fails to start - "Port 8080 already in use"
```bash
# Find process using port 8080
lsof -i :8080
# Kill the process
kill -9 <PID>
```

**Issue**: Database connection refused
```bash
# Check PostgreSQL is running
sudo service postgresql status
# Start if not running
sudo service postgresql start
```

**Issue**: Frontend can't connect to backend
```bash
# Check VITE_API_BASE_URL in .env
echo $VITE_API_BASE_URL
# Should be http://localhost:8080
```

**Issue**: JWT token expired error
- Tokens expire after 24 hours. Login again or use refresh token endpoint.

**Issue**: OAuth2 redirect fails
- Verify Google OAuth2 credentials in application.properties
- Check redirect URI is registered in Google Cloud Console

For more help, see [QUICKSTART.md](documentation/QUICKSTART.md) or open an issue.

---

## 📈 Roadmap

### Version 2.0 (Planned)

- [ ] Email notifications for booking confirmation
- [ ] SMS alerts for journey updates
- [ ] Passenger multiple selection
- [ ] Ticket download as PDF
- [ ] Seat preference (window, aisle)
- [ ] Waitlist management
- [ ] Real-time train tracking
- [ ] Reviews and ratings

### Version 3.0 (Future)

- [ ] Mobile app (React Native)
- [ ] AI-based seat recommendations
- [ ] Dynamic pricing
- [ ] Loyalty program
- [ ] Multi-language support
- [ ] Accessibility improvements
- [ ] Analytics dashboard
- [ ] Integration with real payment gateways

---

## 📜 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Your Name**

- GitHub: [@your-username](https://github.com/your-username)
- LinkedIn: [Your Profile](https://linkedin.com/in/your-profile)
- Email: your.email@example.com
- Portfolio: [yourportfolio.com](https://yourportfolio.com)

---

## 🙏 Acknowledgments

- **Spring Boot Team** for the excellent framework
- **React Team** for the powerful UI library
- **Tailwind CSS** for the utility-first styling approach
- **JWT.io** for token implementation guidance
- **PostgreSQL** for the robust database
- All open-source contributors whose libraries made this project possible

---

## ⭐ Support

If you found this project helpful, please consider:

- ⭐ **Starring** the repository
- 🐛 **Reporting** bugs or issues
- 💡 **Suggesting** new features
- 🔀 **Contributing** code improvements
- 📢 **Sharing** with others

---

<div align="center">

**Made with ❤️ and ☕**

[⬆ Back to Top](#-train-reservation-system)

</div>
