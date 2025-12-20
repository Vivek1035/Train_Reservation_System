# 🚀 Quick Start Guide

## ⚡ Get Started in 5 Minutes

### **Step 1: Install Prerequisites**
```bash
# Check Java version (need 17+)
java -version

# Check Maven version
mvn -version

# Check PostgreSQL
psql --version
```

### **Step 2: Setup Database**
```bash
# Login to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE train_reservation_db;

# Exit PostgreSQL
\q
```

### **Step 3: Run Sample Data Script (Optional)**
```bash
psql -U postgres -d train_reservation_db -f database/init.sql
```

### **Step 4: Configure Application**
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
```

### **Step 5: Build & Run**
```bash
# Option 1: Using the new pom.xml
mvn -f pom-new.xml spring-boot:run

# Option 2: Rename and use
mv pom-new.xml pom.xml
mvn spring-boot:run
```

### **Step 6: Test the API**
Open browser or use curl:
```bash
# Test health
curl http://localhost:8080/api/stations

# If you see JSON response, you're good to go! 🎉
```

---

## 🧪 Test Endpoints

### **Get All Stations**
```bash
curl http://localhost:8080/api/stations
```

### **Create a User**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Test",
    "lastName": "User",
    "email": "test@example.com",
    "password": "password123",
    "phoneNumber": "1234567890",
    "role": "USER",
    "active": true
  }'
```

### **Search Trains**
```bash
# Replace station IDs with actual IDs from your database
curl "http://localhost:8080/api/trains/search?sourceId=1&destinationId=2"
```

### **Get All Active Trains**
```bash
curl "http://localhost:8080/api/trains?active=true"
```

---

## 🐳 Docker Quick Start (Optional)

### **Create docker-compose.yml**
```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: train_reservation_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

### **Run with Docker**
```bash
# Start PostgreSQL
docker-compose up -d

# Run application
mvn spring-boot:run
```

---

## 🔍 Common Issues & Solutions

### **Issue 1: Port 8080 already in use**
**Solution**: Change port in `application.properties`
```properties
server.port=8081
```

### **Issue 2: Database connection failed**
**Solution**: Check PostgreSQL is running
```bash
# Windows
net start postgresql-x64-15

# Linux/Mac
sudo systemctl start postgresql
```

### **Issue 3: Lombok not working**
**Solution**: Enable annotation processing in your IDE
- **IntelliJ IDEA**: Settings → Build → Compiler → Annotation Processors → Enable
- **Eclipse**: Install Lombok plugin

### **Issue 4: Tables not created**
**Solution**: Check `ddl-auto` setting
```properties
spring.jpa.hibernate.ddl-auto=update
```

---

## 📁 Project Files Overview

```
Train_Reservation_System-main/
├── src/main/java/com/trainreservation/     # Java source code
├── src/main/resources/                     # Configuration files
├── database/                               # SQL scripts
├── pom-new.xml                            # Maven dependencies (Spring Boot)
├── BACKEND_README.md                       # Full documentation
├── PROJECT_SUMMARY.md                      # What was built
└── QUICKSTART.md                          # This file
```

---

## 🎯 Verification Checklist

After starting the application, verify:

- [ ] Application starts without errors
- [ ] You see "Started TrainReservationApplication" in logs
- [ ] Database tables are created
- [ ] `/api/stations` endpoint returns data
- [ ] `/api/trains` endpoint returns data

---

## 📞 Need Help?

1. **Check logs** in the terminal for error messages
2. **Review** `BACKEND_README.md` for detailed docs
3. **Verify** database connection in `application.properties`
4. **Ensure** Java 17+ and PostgreSQL are installed

---

## ✅ You're Ready!

If all tests pass, you now have:
- ✅ Modern Spring Boot backend running
- ✅ PostgreSQL database connected
- ✅ 38 REST API endpoints ready
- ✅ Sample data loaded

**Next**: Start building the frontend or add security! 🚀
