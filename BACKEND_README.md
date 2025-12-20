# Train Reservation System - Modern Spring Boot Backend

## 🎯 Overview
A modern, production-ready train reservation system backend built with **Spring Boot 3.2**, **Spring Data JPA**, and **PostgreSQL**. This project follows clean architecture principles with strict MVC layering.

---

## 🏗️ Architecture

### **Layered Architecture**
```
Controller Layer → Service Layer → Repository Layer → Database
```

- **Controllers**: Handle HTTP requests/responses, input validation
- **Services**: Business logic, transaction management
- **Repositories**: Data access using Spring Data JPA
- **Entities**: JPA entities with proper relationships

---

## 📦 Tech Stack

- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok** (reduces boilerplate)
- **Maven**

---

## 📁 Project Structure

```
src/main/java/com/trainreservation/
├── TrainReservationApplication.java
├── controller/
│   ├── UserController.java
│   ├── StationController.java
│   ├── TrainController.java
│   └── BookingController.java
├── service/
│   ├── UserService.java
│   ├── StationService.java
│   ├── TrainService.java
│   ├── BookingService.java
│   └── impl/
│       ├── UserServiceImpl.java
│       ├── StationServiceImpl.java
│       ├── TrainServiceImpl.java
│       └── BookingServiceImpl.java
├── repository/
│   ├── UserRepository.java
│   ├── StationRepository.java
│   ├── TrainRepository.java
│   ├── CoachRepository.java
│   ├── SeatRepository.java
│   ├── BookingRepository.java
│   ├── ReservationRepository.java
│   └── PaymentRepository.java
├── entity/
│   ├── User.java
│   ├── Station.java
│   ├── Train.java
│   ├── Coach.java
│   ├── Seat.java
│   ├── Booking.java
│   ├── Reservation.java
│   └── Payment.java
└── enums/
    ├── UserRole.java
    ├── QuotaType.java
    ├── CoachType.java
    ├── BookingStatus.java
    ├── PaymentStatus.java
    └── PaymentMethod.java

src/main/resources/
├── application.properties
├── application-dev.properties
└── application-prod.properties
```

---

## 🗄️ Database Design

### **Entity Relationships**

#### **User**
- Role: `USER` or `ADMIN`
- One-to-Many with `Booking`

#### **Station**
- Stores station code, name, city, state
- Referenced by trains as source/destination

#### **Train**
- Train number, name, route, timings
- Many-to-One with `Station` (source & destination)
- One-to-Many with `Coach`
- One-to-Many with `Booking`

#### **Coach**
- Coach type (AC, Sleeper, etc.)
- Many-to-One with `Train`
- One-to-Many with `Seat`

#### **Seat**
- Individual seat with availability
- Many-to-One with `Coach`
- Quota type: `GENERAL`, `TATKAL`, etc.

#### **Booking**
- PNR number, journey date, status
- Many-to-One with `User` and `Train`
- One-to-Many with `Reservation`
- One-to-One with `Payment`

#### **Reservation**
- Passenger details (name, age, gender)
- Many-to-One with `Booking`
- One-to-One with `Seat`

#### **Payment**
- Transaction details
- One-to-One with `Booking`

---

## 🚀 Setup & Installation

### **Prerequisites**
- Java 17 or higher
- PostgreSQL 13+
- Maven 3.6+

### **1. Clone the Repository**
```bash
git clone <repository-url>
cd Train_Reservation_System-main
```

### **2. Setup PostgreSQL Database**
```sql
CREATE DATABASE train_reservation_db;
```

### **3. Configure Database**
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/train_reservation_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### **4. Build the Project**
```bash
mvn clean install
```

### **5. Run the Application**
```bash
mvn spring-boot:run
```

Or use the new `pom-new.xml`:
```bash
mvn -f pom-new.xml spring-boot:run
```

The application will start on `http://localhost:8080`

---

## 📡 API Endpoints

### **User Management**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users` | Create new user |
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users/email/{email}` | Get user by email |
| GET | `/api/users/role/{role}` | Get users by role |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |
| PATCH | `/api/users/{id}/deactivate` | Deactivate user |
| PATCH | `/api/users/{id}/activate` | Activate user |

### **Station Management**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/stations` | Create station |
| GET | `/api/stations` | Get all stations |
| GET | `/api/stations/{id}` | Get station by ID |
| GET | `/api/stations/code/{code}` | Get station by code |
| GET | `/api/stations/search?name={name}` | Search stations |
| PUT | `/api/stations/{id}` | Update station |
| DELETE | `/api/stations/{id}` | Delete station |

### **Train Management**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/trains` | Create train |
| GET | `/api/trains` | Get all trains |
| GET | `/api/trains?active=true` | Get active trains |
| GET | `/api/trains/{id}` | Get train by ID |
| GET | `/api/trains/number/{number}` | Get train by number |
| GET | `/api/trains/search?sourceId={id}&destinationId={id}` | Search trains |
| GET | `/api/trains/available` | Get trains with seats |
| PUT | `/api/trains/{id}` | Update train |
| PATCH | `/api/trains/{id}/seats?change={number}` | Update seats |
| DELETE | `/api/trains/{id}` | Delete train |

### **Booking Management**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/bookings` | Create booking |
| GET | `/api/bookings/{id}` | Get booking by ID |
| GET | `/api/bookings/pnr/{pnr}` | Get booking by PNR |
| GET | `/api/bookings/user/{userId}` | Get user bookings |
| GET | `/api/bookings/train/{trainId}` | Get train bookings |
| GET | `/api/bookings/status/{status}` | Get bookings by status |
| PUT | `/api/bookings/{id}` | Update booking |
| PATCH | `/api/bookings/{id}/cancel` | Cancel booking |
| PATCH | `/api/bookings/{id}/confirm` | Confirm booking |
| DELETE | `/api/bookings/{id}` | Delete booking |

---

## 🧪 Testing

### Example API Call (Create User)
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "password": "password123",
    "phoneNumber": "1234567890",
    "role": "USER",
    "active": true
  }'
```

### Example API Call (Search Trains)
```bash
curl http://localhost:8080/api/trains/search?sourceId=1&destinationId=2
```

---

## 🔧 Configuration Profiles

### **Development Profile**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### **Production Profile**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## 📝 Key Features

✅ **Clean Architecture** with strict MVC layering  
✅ **Spring Data JPA** for database operations  
✅ **Proper entity relationships** with JPA annotations  
✅ **Transaction management** at service layer  
✅ **RESTful API design** with standard HTTP methods  
✅ **Custom queries** using JPQL  
✅ **Lombok** integration for cleaner code  
✅ **Environment-specific configurations**  
✅ **Connection pooling** with HikariCP  
✅ **Comprehensive logging**  

---

## 🚧 Next Steps (Not Implemented Yet)

- [ ] Add JWT authentication & authorization
- [ ] Implement OAuth2 integration
- [ ] Add DTOs for request/response
- [ ] Global exception handling
- [ ] Input validation with Bean Validation
- [ ] API documentation with Swagger/OpenAPI
- [ ] Unit & integration tests
- [ ] React frontend with Vite + Tailwind
- [ ] Docker containerization
- [ ] CI/CD pipeline

---

## 📚 Technologies Used

| Technology | Purpose |
|------------|---------|
| Spring Boot | Application framework |
| Spring Data JPA | ORM & data access |
| PostgreSQL | Relational database |
| Hibernate | JPA implementation |
| Lombok | Boilerplate reduction |
| HikariCP | Connection pooling |
| Maven | Build tool |

---

## 👨‍💻 Development Guidelines

1. **Never skip the service layer** - All business logic goes in services
2. **Use transactions** - Mark service methods with `@Transactional`
3. **Follow naming conventions** - Repository methods follow Spring Data naming
4. **Use DTOs** (to be added) - Never expose entities directly
5. **Validate input** (to be added) - Use `@Valid` annotations
6. **Handle exceptions** (to be added) - Custom exception handlers

---

## 📄 License
This project is developed for educational and portfolio purposes.

---

## 🤝 Contributing
This is a modernization project. Contributions for adding security, validation, DTOs, and frontend are welcome!
