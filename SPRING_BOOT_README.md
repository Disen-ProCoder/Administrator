# 🚗 Vehicle Insurance Administrator - Spring Boot Application

A comprehensive **Spring Boot 3.x** application for managing Vehicle Insurance system users, activities, and configurations with a modern, responsive web interface.

## ✨ Features

- **🔐 Secure Authentication**: Spring Security with role-based access control
- **👥 User Management**: Complete CRUD operations for user accounts
- **📊 Activity Monitoring**: Real-time user activity tracking and logging
- **⚙️ System Configuration**: Dynamic system settings management
- **📈 Reports & Analytics**: Comprehensive reporting with Chart.js
- **🎨 Modern UI**: Responsive design with Bootstrap 5 and Thymeleaf
- **🔒 Security**: BCrypt password encryption, CSRF protection
- **📱 Mobile Responsive**: Works perfectly on all devices

## 🛠️ Technology Stack

### Backend
- **Spring Boot 3.x** - Main framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - Data persistence
- **Spring Web MVC** - Web layer
- **Hibernate** - ORM framework
- **MySQL** - Database (XAMPP)

### Frontend
- **Thymeleaf** - Server-side templating
- **Bootstrap 5.3.2** - UI framework
- **Chart.js 4.4.0** - Data visualization
- **jQuery 3.7.1** - JavaScript library
- **AOS** - Scroll animations

### Development
- **Gradle** - Build tool
- **Lombok** - Boilerplate reduction
- **SLF4J** - Logging
- **JUnit 5** - Testing

## 🚀 Quick Start

### Prerequisites

1. **Java 17+** installed
2. **XAMPP** installed and running
3. **Gradle** (or use included wrapper)

### Setup Instructions

#### 1. **Start XAMPP Services**
```bash
# Start Apache and MySQL in XAMPP Control Panel
# Ensure MySQL is running on port 3306
```

#### 2. **Create Database**
```sql
-- Run the database_setup.sql script in phpMyAdmin
-- Database: vehicle_insurance_db
-- Or create manually:
CREATE DATABASE vehicle_insurance_db;
```

#### 3. **Configure Application**
```properties
# Default configuration in application.properties works with XAMPP
# Database: localhost:3306/vehicle_insurance_db
# Username: root, Password: (empty)
```

#### 4. **Run Application**
```bash
# Option 1: Using Gradle Wrapper
./gradlew bootRun

# Option 2: Using the batch file (Windows)
start-application.bat

# Option 3: Using IDE
# Run AdministratorApplication.java
```

#### 5. **Access Application**
- **🌐 URL**: http://localhost:8080
- **🔑 Login**: admin / admin123
- **📊 Dashboard**: http://localhost:8080/admin/dashboard
- **❤️ Health**: http://localhost:8080/actuator/health

## 🔐 Default Credentials

| Field | Value |
|-------|-------|
| **Username** | admin |
| **Password** | admin123 |
| **Role** | ADMIN_OFFICER |

## 📡 API Endpoints

### 🔐 Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/login` | Login page |
| `POST` | `/login` | User authentication |
| `POST` | `/logout` | User logout |

### 👥 User Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/admin/users` | Get all users |
| `POST` | `/api/admin/users` | Create user |
| `PUT` | `/api/admin/users/{id}` | Update user |
| `DELETE` | `/api/admin/users/{id}` | Delete user |
| `GET` | `/admin/users` | User management page |

### 📊 Dashboard & Reports
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/admin/dashboard` | Dashboard page |
| `GET` | `/api/admin/reports/users` | User reports |
| `GET` | `/api/admin/reports/activities` | Activity reports |

### ⚙️ System Configuration
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/admin/config` | Get system configuration |
| `PUT` | `/api/admin/config/{key}` | Update configuration |
| `GET` | `/admin/config` | Configuration page |

## 🗄️ Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN_OFFICER', 'POLICY_OFFICER', 'CLAIMS_OFFICER', 'CUSTOMER_SERVICE') NOT NULL,
    status ENUM('ACTIVE', 'BLOCKED', 'INACTIVE', 'PENDING') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### User Activities Table
```sql
CREATE TABLE user_activities (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    activity_type VARCHAR(50) NOT NULL,
    activity_description TEXT,
    ip_address VARCHAR(45),
    user_agent TEXT,
    success BOOLEAN DEFAULT TRUE,
    activity_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### System Configuration Table
```sql
CREATE TABLE system_configurations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_key VARCHAR(100) UNIQUE NOT NULL,
    config_value TEXT,
    config_type ENUM('SYSTEM', 'SECURITY', 'NOTIFICATION', 'INTEGRATION') NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/org/example/administrator/
│   │   ├── controller/          # REST & Web controllers
│   │   │   ├── AdminController.java
│   │   │   ├── UserController.java
│   │   │   ├── WebController.java
│   │   │   ├── LoginController.java
│   │   │   └── HomeController.java
│   │   ├── service/             # Business logic
│   │   │   ├── UserService.java
│   │   │   ├── AdminService.java
│   │   │   └── UserActivityService.java
│   │   ├── repository/          # Data access
│   │   │   ├── UserRepository.java
│   │   │   └── UserActivityRepository.java
│   │   ├── entity/              # JPA entities
│   │   │   ├── User.java
│   │   │   └── UserActivity.java
│   │   ├── dto/                 # Data transfer objects
│   │   │   ├── UserCreateDTO.java
│   │   │   └── UserResponseDTO.java
│   │   ├── exception/           # Custom exceptions
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── config/              # Configuration
│   │   │   ├── SecurityConfig.java
│   │   │   └── AppConfig.java
│   │   └── AdministratorApplication.java
│   └── resources/
│       ├── templates/           # Thymeleaf templates
│       │   ├── layout.html
│       │   ├── login.html
│       │   └── admin/
│       │       ├── dashboard.html
│       │       └── users.html
│       ├── static/              # Static resources
│       │   ├── css/admin.css
│       │   └── js/admin.js
│       └── application.properties
└── test/                        # Test classes
    ├── java/org/example/administrator/
    │   └── AdministratorApplicationTests.java
    └── resources/
        └── application-test.properties
```

## 🧪 Testing

### Run Tests
```bash
# Run all tests
./gradlew test

# Run with coverage
./gradlew test jacocoTestReport

# Run specific test class
./gradlew test --tests AdministratorApplicationTests
```

### Test Configuration
- **Database**: H2 in-memory database
- **Profile**: `test`
- **Port**: Random available port

## 🔧 Development

### Building Application
```bash
# Build project
./gradlew build

# Build without tests
./gradlew build -x test

# Clean build
./gradlew clean build
```

### Running in Development Mode
```bash
# Enable dev tools (auto-restart)
./gradlew bootRun --args='--spring.profiles.active=dev'
```

## 🐛 Troubleshooting

### Common Issues

#### 1. **Database Connection Error**
```bash
# Check XAMPP MySQL is running
# Verify database exists: vehicle_insurance_db
# Check credentials in application.properties
```

#### 2. **Port Already in Use**
```properties
# Change port in application.properties
server.port=8081
```

#### 3. **Login Issues**
```bash
# Use default credentials: admin / admin123
# Check user role is ADMIN_OFFICER
# Verify Spring Security configuration
```

#### 4. **Static Resources Not Loading**
```properties
# Check Thymeleaf configuration
spring.thymeleaf.cache=false
```

### Logs and Debugging
```bash
# Enable debug logging
logging.level.org.example.administrator=DEBUG
logging.level.org.springframework.security=DEBUG
```

## 📱 Mobile Support

The application is fully responsive and works on:
- 📱 Mobile phones (iOS/Android)
- 📱 Tablets (iPad/Android)
- 💻 Desktop computers
- 🖥️ Large screens

## 🔒 Security Features

- **🔐 BCrypt Password Encryption**
- **🛡️ CSRF Protection**
- **🔑 Role-Based Access Control**
- **🚫 SQL Injection Prevention**
- **🔒 Session Management**
- **🌐 CORS Configuration**

## 📊 Monitoring

### Health Checks
- **Health**: http://localhost:8080/actuator/health
- **Info**: http://localhost:8080/actuator/info
- **Metrics**: http://localhost:8080/actuator/metrics

### Logging
- **Application Logs**: Console output
- **Security Logs**: Authentication events
- **Database Logs**: SQL queries (debug mode)

## 🚀 Production Deployment

### Environment Variables
```bash
export SPRING_PROFILES_ACTIVE=prod
export SPRING_DATASOURCE_URL=jdbc:mysql://your-db-host:3306/vehicle_insurance_db
export SPRING_DATASOURCE_USERNAME=your-username
export SPRING_DATASOURCE_PASSWORD=your-password
```

### JAR Deployment
```bash
# Build JAR
./gradlew build

# Run JAR
java -jar build/libs/administrator-0.0.1-SNAPSHOT.jar
```

## 📞 Support

### Getting Help
1. **Check Logs**: Look at console output for errors
2. **Verify XAMPP**: Ensure MySQL is running
3. **Check Java**: Verify Java 17+ is installed
4. **Review Config**: Check application.properties

### Common Solutions
- **Restart XAMPP** if database connection fails
- **Clear Browser Cache** if UI issues occur
- **Check Port Availability** if startup fails
- **Verify Database** exists and is accessible

## 📄 License

This project is part of the **Vehicle Insurance Management System** and is intended for educational and development purposes.

---

## 🎉 **Ready to Use!**

Your **Vehicle Insurance Administrator** application is now ready with:
- ✅ **Modern Spring Boot 3.x** architecture
- ✅ **Secure authentication** with Spring Security
- ✅ **Beautiful responsive UI** with Bootstrap 5
- ✅ **Real-time dashboard** with Chart.js
- ✅ **Complete user management** system
- ✅ **Activity monitoring** and logging
- ✅ **Mobile-friendly** design
- ✅ **Production-ready** configuration

**🚀 Start your application and enjoy the modern admin experience!**
