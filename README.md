# Vehicle Insurance Management System - Admin Officer Module

## 📋 Project Overview

This is the **Admin Officer** module for the Vehicle Insurance Management System, developed as part of a 6-member university Software Engineering project. The module provides comprehensive user management, system configuration, and activity monitoring capabilities.

## 🏗️ Architecture

### Tech Stack
- **Backend:** Spring Boot 3.x (Java 21)
- **Frontend:** Thymeleaf with Bootstrap 5
- **Database:** Microsoft SQL Server
- **Security:** Spring Security with role-based access control
- **Build Tool:** Gradle
- **Testing:** JUnit 5, Mockito, Spring Boot Test

### Project Structure
```
src/
├── main/
│   ├── java/org/example/administrator/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST and Web controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA entities
│   │   ├── enums/          # Enumerations
│   │   ├── exception/      # Custom exceptions
│   │   ├── repository/     # Data access layer
│   │   └── service/        # Business logic layer
│   └── resources/
│       ├── static/js/      # JavaScript files
│       └── templates/      # Thymeleaf templates
└── test/                   # Test classes
```

## 🚀 Features

### Core Functionalities

#### 1. User Management System
- **User CRUD Operations:** Create, read, update, delete users
- **User Status Management:** Active, blocked, pending, inactive statuses
- **Role-based Access:** Admin Officer, Policy Officer, Claims Officer, etc.
- **Account Security:** Password reset, account locking/unlocking
- **User Search & Filtering:** Advanced search capabilities

#### 2. System Configuration
- **Configuration Management:** System settings and parameters
- **Configuration Types:** System, Security, Email, Database, UI, Business
- **Encryption Support:** Sensitive configuration encryption
- **Read-only Configurations:** Protected system settings

#### 3. User Activity Monitoring
- **Activity Logging:** Comprehensive user action tracking
- **Activity Types:** Login, logout, user creation, system changes
- **Audit Trail:** IP address, session tracking, timestamps
- **Activity Reports:** Detailed activity analysis and reporting

#### 4. System Reports & Analytics
- **Dashboard Statistics:** Real-time system overview
- **User Reports:** User statistics and analytics
- **Activity Reports:** System activity analysis
- **System Health:** System status monitoring

## 🔧 API Endpoints

### User Management
```
POST   /api/admin/users           - Create new user
GET    /api/admin/users           - Get all users (paginated)
GET    /api/admin/users/{id}      - Get user by ID
PUT    /api/admin/users/{id}      - Update user
DELETE /api/admin/users/{id}      - Delete user (soft delete)
POST   /api/admin/users/{id}/block - Block user
POST   /api/admin/users/{id}/unblock - Unblock user
POST   /api/admin/users/{id}/reset-password - Reset password
POST   /api/admin/users/{id}/lock - Lock account
POST   /api/admin/users/{id}/unlock - Unlock account
```

### User Activities
```
GET    /api/admin/activities/user/{userId} - Get user activities
GET    /api/admin/activities/recent - Get recent activities
GET    /api/admin/activities/type/{type} - Get activities by type
GET    /api/admin/activities/failed - Get failed activities
GET    /api/admin/activities/date-range - Get activities by date range
```

### System Configuration
```
GET    /api/admin/config - Get all configurations
GET    /api/admin/config/{key} - Get configuration by key
POST   /api/admin/config - Create/update configuration
PUT    /api/admin/config/{key}/value - Update configuration value
DELETE /api/admin/config/{key} - Delete configuration
```

### Admin Dashboard
```
GET    /api/admin/dashboard/statistics - Get dashboard statistics
GET    /api/admin/system/health - Get system health status
GET    /api/admin/reports/system - Generate system overview report
GET    /api/admin/reports/users - Generate user statistics report
GET    /api/admin/reports/activities - Generate activity report
```

## 🗃️ Database Schema

### Core Entities

#### User Entity
```sql
CREATE TABLE users (
    user_id BIGINT IDENTITY PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20),
    user_role VARCHAR(20) NOT NULL,
    user_status VARCHAR(20) NOT NULL,
    last_login DATETIME,
    login_attempts INT DEFAULT 0,
    account_locked_until DATETIME,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    is_deleted BIT DEFAULT 0
);
```

#### UserActivity Entity
```sql
CREATE TABLE user_activities (
    activity_id BIGINT IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    activity_type VARCHAR(50) NOT NULL,
    activity_description VARCHAR(500) NOT NULL,
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    session_id VARCHAR(100),
    activity_timestamp DATETIME NOT NULL,
    success BIT DEFAULT 1,
    error_message VARCHAR(1000),
    additional_data VARCHAR(2000),
    created_at DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

#### SystemConfiguration Entity
```sql
CREATE TABLE system_configurations (
    config_id BIGINT IDENTITY PRIMARY KEY,
    config_key VARCHAR(100) UNIQUE NOT NULL,
    config_value VARCHAR(1000) NOT NULL,
    config_description VARCHAR(500),
    config_type VARCHAR(20) NOT NULL,
    is_encrypted BIT DEFAULT 0,
    is_read_only BIT DEFAULT 0,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    created_by VARCHAR(50),
    updated_by VARCHAR(50)
);
```

## 🔒 Security Features

### Authentication & Authorization
- **Spring Security Integration:** Comprehensive security framework
- **Role-based Access Control:** ADMIN_OFFICER role required for all operations
- **Password Encryption:** BCrypt password hashing
- **Session Management:** Secure session handling
- **CSRF Protection:** Cross-site request forgery protection

### Security Annotations
```java
@PreAuthorize("hasRole('ADMIN_OFFICER')")
@CrossOrigin(origins = "*")
```

### Activity Logging
- **All admin actions are logged:** User creation, modification, deletion
- **IP address tracking:** Security monitoring
- **Session tracking:** User session management
- **Error logging:** Failed operations tracking

## 🧪 Testing

### Test Coverage
- **Unit Tests:** Service layer with Mockito
- **Integration Tests:** Controller layer with MockMvc
- **Repository Tests:** Data access layer testing
- **Security Tests:** Authentication and authorization

### Running Tests
```bash
./gradlew test
./gradlew integrationTest
```

## 🚀 Getting Started

### Prerequisites
- Java 21+
- Microsoft SQL Server
- Gradle 7.0+

### Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd administrator
```

2. **Configure Database**
   - Update `application.properties` with your SQL Server connection details
   - Create database: `VehicleInsuranceDB`

3. **Run the application**
```bash
./gradlew bootRun
```

4. **Access the application**
   - URL: `http://localhost:8080/admin`
   - Default credentials: `admin` / `admin123`

### Configuration

#### Database Configuration
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=VehicleInsuranceDB
spring.datasource.username=sa
spring.datasource.password=YourPassword123!
```

#### Security Configuration
```properties
spring.security.user.name=admin
spring.security.user.password=admin123
spring.security.user.roles=ADMIN_OFFICER
```

## 📊 Monitoring & Logging

### Logging Configuration
- **Application Logs:** DEBUG level for development
- **Security Logs:** Authentication and authorization events
- **Activity Logs:** User actions and system events
- **Error Logs:** Exception handling and error tracking

### Health Monitoring
- **System Health Endpoint:** `/api/admin/system/health`
- **Database Connectivity:** Automatic health checks
- **User Statistics:** Real-time user metrics
- **Activity Monitoring:** System activity tracking

## 🔧 Development Guidelines

### Code Standards
- **Java Conventions:** Follow Java naming conventions
- **Spring Boot Best Practices:** Use proper annotations
- **Exception Handling:** Comprehensive error handling
- **Logging:** Structured logging with SLF4J
- **Validation:** Input validation with Bean Validation

### Database Guidelines
- **Naming Convention:** snake_case for columns
- **Primary Keys:** BIGINT IDENTITY
- **Timestamps:** created_at, updated_at
- **Soft Deletes:** is_deleted flag
- **Foreign Keys:** Proper relationships

## 🤝 Team Integration

### APIs for Other Modules
- **User Authentication Service:** For other modules
- **Role-based Access Validation:** Permission checking
- **Activity Logging Service:** Cross-module activity tracking
- **System Configuration Access:** Shared configuration

### Integration Points
- **Authentication Service:** User login/logout
- **Email Notification Service:** User notifications
- **File Upload Service:** Document management
- **Reporting Service:** System reports

## 📈 Performance Considerations

### Database Optimization
- **Indexing:** Proper database indexes
- **Pagination:** Large dataset handling
- **Query Optimization:** Efficient database queries
- **Connection Pooling:** Database connection management

### Caching Strategy
- **Configuration Caching:** System settings
- **User Session Caching:** Active sessions
- **Activity Caching:** Recent activities
- **Statistics Caching:** Dashboard metrics

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Issues**
   - Check SQL Server connection string
   - Verify database exists
   - Check firewall settings

2. **Authentication Issues**
   - Verify user credentials
   - Check role assignments
   - Review security configuration

3. **Performance Issues**
   - Monitor database queries
   - Check memory usage
   - Review logging levels

### Debug Mode
```properties
logging.level.org.example.administrator=DEBUG
logging.level.org.springframework.security=DEBUG
```

## 📚 Documentation

### API Documentation
- **Swagger/OpenAPI:** Available at `/swagger-ui.html`
- **Postman Collection:** Available in `/docs` folder
- **API Examples:** Comprehensive request/response examples

### User Guide
- **Admin Dashboard:** System overview and statistics
- **User Management:** Complete user lifecycle management
- **Activity Monitoring:** User action tracking
- **System Configuration:** Settings and parameters

## 🔄 Version History

### Version 1.0.0
- Initial release
- Core user management functionality
- System configuration management
- Activity monitoring and logging
- Admin dashboard with statistics
- Comprehensive security implementation

## 📞 Support

### Development Team
- **Lead Developer:** [Your Name]
- **Backend Developer:** [Team Member]
- **Frontend Developer:** [Team Member]
- **Database Developer:** [Team Member]
- **QA Engineer:** [Team Member]
- **DevOps Engineer:** [Team Member]

### Contact Information
- **Email:** [team-email@university.edu]
- **Project Repository:** [GitHub URL]
- **Documentation:** [Documentation URL]

---

## 📝 License

This project is developed as part of a university Software Engineering course. All rights reserved.

## 🙏 Acknowledgments

- Spring Boot community for excellent framework
- Bootstrap team for responsive UI components
- Microsoft for SQL Server database
- University faculty for project guidance
- Team members for collaborative development

---

**Note:** This is a comprehensive Admin Officer module for the Vehicle Insurance Management System. The module provides all necessary functionality for user management, system configuration, and activity monitoring as specified in the project requirements.

