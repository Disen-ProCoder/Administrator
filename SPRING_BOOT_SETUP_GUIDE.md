# 🚀 Spring Boot Setup Guide - Vehicle Insurance Administrator

This guide will help you set up and run the Vehicle Insurance Administrator application using Spring Boot.

## 📋 Prerequisites

### Required Software
1. **Java 17 or higher**
   - Download from: https://adoptium.net/
   - Verify installation: `java -version`

2. **XAMPP (for MySQL database)**
   - Download from: https://www.apachefriends.org/
   - Install and start Apache + MySQL services

3. **IDE (Optional but recommended)**
   - IntelliJ IDEA, Eclipse, or VS Code
   - Spring Boot extensions recommended

## 🗄️ Database Setup

### Step 1: Start XAMPP
1. Open XAMPP Control Panel
2. Start **Apache** and **MySQL** services
3. Ensure MySQL is running on port **3306**

### Step 2: Create Database
1. Open **phpMyAdmin** (http://localhost/phpmyadmin)
2. Create new database: `vehicle_insurance_db`
3. Or run the provided SQL script:

```sql
CREATE DATABASE vehicle_insurance_db;
USE vehicle_insurance_db;

-- Users table
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

-- User activities table
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

-- System configurations table
CREATE TABLE system_configurations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_key VARCHAR(100) UNIQUE NOT NULL,
    config_value TEXT,
    config_type ENUM('SYSTEM', 'SECURITY', 'NOTIFICATION', 'INTEGRATION') NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert default admin user
INSERT INTO users (username, email, password, role, status) 
VALUES ('admin', 'admin@vehicleinsurance.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'ADMIN_OFFICER', 'ACTIVE');
```

## 🚀 Running the Application

### Method 1: Using Gradle Wrapper (Recommended)

#### Windows:
```cmd
# Navigate to project directory
cd C:\Users\User\Desktop\Administrator

# Run the application
gradlew.bat bootRun
```

#### Linux/Mac:
```bash
# Navigate to project directory
cd /path/to/Administrator

# Make gradlew executable
chmod +x gradlew

# Run the application
./gradlew bootRun
```

### Method 2: Using IDE

1. **Open Project in IDE**
   - Import as Gradle project
   - Wait for dependencies to download

2. **Run Main Class**
   - Navigate to `AdministratorApplication.java`
   - Right-click → Run 'AdministratorApplication'
   - Or use the green play button

### Method 3: Using Batch File (Windows)

```cmd
# Double-click start-application.bat
# Or run from command line:
start-application.bat
```

## 🌐 Accessing the Application

### URLs
- **Main Application**: http://localhost:8080
- **Login Page**: http://localhost:8080/login
- **Dashboard**: http://localhost:8080/admin/dashboard
- **Health Check**: http://localhost:8080/actuator/health

### Default Credentials
- **Username**: `admin`
- **Password**: `admin123`
- **Role**: `ADMIN_OFFICER`

## 🔧 Configuration

### Application Properties
The application is pre-configured for XAMPP MySQL:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/vehicle_insurance_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Server Configuration
server.port=8080
server.servlet.context-path=/

# Security Configuration
spring.security.user.name=admin
spring.security.user.password=admin123
spring.security.user.roles=ADMIN_OFFICER
```

### Custom Configuration
If you need to change settings:

1. **Database Connection**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your_database
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

2. **Server Port**
   ```properties
   server.port=8081
   ```

3. **Context Path**
   ```properties
   server.servlet.context-path=/admin
   ```

## 🧪 Testing

### Run Tests
```bash
# Run all tests
./gradlew test

# Run specific test
./gradlew test --tests AdministratorApplicationTests
```

### Test Database
Tests use H2 in-memory database automatically.

## 🐛 Troubleshooting

### Common Issues

#### 1. **Port 8080 Already in Use**
```bash
# Solution 1: Kill process using port 8080
netstat -ano | findstr :8080
taskkill /PID <PID_NUMBER> /F

# Solution 2: Change port in application.properties
server.port=8081
```

#### 2. **Database Connection Failed**
```bash
# Check XAMPP MySQL is running
# Verify database exists: vehicle_insurance_db
# Check MySQL credentials
```

#### 3. **Java Version Issues**
```bash
# Check Java version
java -version

# Should show Java 17 or higher
# If not, install Java 17+ from https://adoptium.net/
```

#### 4. **Gradle Build Issues**
```bash
# Clean and rebuild
./gradlew clean build

# Or delete .gradle folder and rebuild
rm -rf .gradle
./gradlew build
```

#### 5. **Static Resources Not Loading**
```properties
# Check Thymeleaf configuration
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
```

### Debug Mode
Enable debug logging:

```properties
logging.level.org.example.administrator=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.web=DEBUG
```

## 📊 Application Features

### Dashboard
- **Real-time Statistics**: User counts, activity metrics
- **Interactive Charts**: User activity trends, role distribution
- **System Health**: Database status, server metrics
- **Quick Actions**: Direct access to common tasks

### User Management
- **Create Users**: Add new system users
- **Edit Users**: Update user information
- **User Roles**: Assign appropriate roles
- **User Status**: Activate, block, or deactivate users
- **Search & Filter**: Find users quickly

### Activity Monitoring
- **User Activities**: Track all user actions
- **Login/Logout**: Authentication events
- **System Changes**: Configuration updates
- **Error Tracking**: Failed operations

### System Configuration
- **Settings Management**: System parameters
- **Security Settings**: Password policies, session timeouts
- **Notification Settings**: Email configurations
- **Integration Settings**: External service connections

## 🔒 Security Features

### Authentication
- **Spring Security**: Comprehensive security framework
- **BCrypt Encryption**: Secure password hashing
- **Session Management**: Secure session handling
- **CSRF Protection**: Cross-site request forgery prevention

### Authorization
- **Role-Based Access**: ADMIN_OFFICER role required
- **Method Security**: @PreAuthorize annotations
- **URL Protection**: Secure endpoint access
- **Activity Logging**: All actions are logged

## 📱 Mobile Support

The application is fully responsive:
- **Mobile Phones**: iOS and Android
- **Tablets**: iPad and Android tablets
- **Desktop**: All screen sizes
- **Touch Support**: Touch-friendly interface

## 🚀 Production Deployment

### Environment Setup
```bash
# Set production profile
export SPRING_PROFILES_ACTIVE=prod

# Set database connection
export SPRING_DATASOURCE_URL=jdbc:mysql://your-db-host:3306/vehicle_insurance_db
export SPRING_DATASOURCE_USERNAME=your-username
export SPRING_DATASOURCE_PASSWORD=your-password
```

### JAR Deployment
```bash
# Build production JAR
./gradlew build

# Run JAR
java -jar build/libs/administrator-0.0.1-SNAPSHOT.jar
```

## 📞 Support

### Getting Help
1. **Check Console Logs**: Look for error messages
2. **Verify Prerequisites**: Java 17+, XAMPP MySQL
3. **Check Configuration**: application.properties
4. **Test Database**: Ensure MySQL is accessible

### Common Solutions
- **Restart XAMPP**: If database connection fails
- **Clear Browser Cache**: If UI issues occur
- **Check Port Availability**: If startup fails
- **Verify Java Version**: Must be 17 or higher

## 🎉 Success!

If everything is working correctly, you should see:

```
🚀 Vehicle Insurance Administrator Application Started Successfully!
📊 Application Name: Vehicle Insurance Administrator
🌐 Server Port: 8080
🗄️ Database URL: jdbc:mysql://localhost:3306/vehicle_insurance_db
👤 Default Admin User: admin
🔗 Access URLs:
   - Login: http://localhost:8080/login
   - Dashboard: http://localhost:8080/admin/dashboard
   - Health Check: http://localhost:8080/actuator/health
```

**🎊 Congratulations! Your Vehicle Insurance Administrator application is now running!**

---

## 📚 Additional Resources

- **Spring Boot Documentation**: https://spring.io/projects/spring-boot
- **Thymeleaf Documentation**: https://www.thymeleaf.org/
- **Bootstrap Documentation**: https://getbootstrap.com/
- **MySQL Documentation**: https://dev.mysql.com/doc/

**Happy coding! 🚀**
