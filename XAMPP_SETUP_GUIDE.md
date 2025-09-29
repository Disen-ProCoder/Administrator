# XAMPP Setup Guide for Vehicle Insurance Admin Module

## 🚀 Quick Setup with XAMPP

### Prerequisites
- XAMPP installed on your system
- Java 21+ installed
- Gradle 7.0+ installed

## 📋 Step-by-Step Setup

### 1. Start XAMPP Services

1. **Open XAMPP Control Panel**
2. **Start the following services:**
   - ✅ **Apache** (for web server)
   - ✅ **MySQL** (for database)

### 2. Create Database

1. **Open phpMyAdmin:**
   - Go to: `http://localhost/phpmyadmin`
   - Or click "Admin" button next to MySQL in XAMPP Control Panel

2. **Import Database:**
   - Click "Import" tab
   - Choose file: `database_setup.sql` (from project root)
   - Click "Go" to import

3. **Verify Database Creation:**
   - You should see `vehicle_insurance_db` database
   - Check that tables are created: `users`, `user_activities`, `system_configurations`

### 3. Configure Application

The application is already configured for XAMPP MySQL:

```properties
# Database Configuration - XAMPP MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/vehicle_insurance_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 4. Run the Application

1. **Open Command Prompt/Terminal in project directory**
2. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```
   Or on Windows:
   ```cmd
   gradlew.bat bootRun
   ```

### 5. Access the Application

1. **Open browser and go to:**
   ```
   http://localhost:8080/admin
   ```

2. **Login with default credentials:**
   - **Username:** `admin`
   - **Password:** `admin123`

## 🔧 Configuration Details

### Database Connection
- **Host:** localhost
- **Port:** 3306
- **Database:** vehicle_insurance_db
- **Username:** root
- **Password:** (empty - default XAMPP setup)

### Application URLs
- **Main Application:** http://localhost:8080/admin
- **Dashboard:** http://localhost:8080/admin/dashboard
- **User Management:** http://localhost:8080/admin/users
- **API Base:** http://localhost:8080/api/admin

### Default Admin User
- **Username:** admin
- **Email:** admin@vehicleinsurance.com
- **Password:** admin123
- **Role:** ADMIN_OFFICER
- **Status:** ACTIVE

## 🗃️ Database Schema

### Tables Created:
1. **users** - User management
2. **user_activities** - Activity logging
3. **system_configurations** - System settings

### Default Data:
- 1 admin user
- 11 system configurations
- Proper indexes for performance

## 🚨 Troubleshooting

### Common Issues:

#### 1. Port 8080 Already in Use
```bash
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID with actual process ID)
taskkill /PID <PID> /F

# Or change port in application.properties
server.port=8081
```

#### 2. MySQL Connection Failed
- Check if MySQL is running in XAMPP
- Verify database `vehicle_insurance_db` exists
- Check username/password (should be root with empty password)

#### 3. Database Import Failed
- Make sure MySQL is running
- Check file path to `database_setup.sql`
- Try importing manually through phpMyAdmin

#### 4. Application Won't Start
- Check Java version: `java -version`
- Check Gradle version: `gradle --version`
- Make sure all dependencies are downloaded: `./gradlew build`

### Debug Mode
Add this to `application.properties` for detailed logging:
```properties
logging.level.org.example.administrator=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

## 📊 Verification Steps

### 1. Check Database Connection
- Application should start without database errors
- Check console for "HikariCP" connection messages

### 2. Test Login
- Go to http://localhost:8080/admin
- Login with admin/admin123
- Should redirect to dashboard

### 3. Test API Endpoints
```bash
# Test dashboard statistics
curl http://localhost:8080/api/admin/dashboard/statistics

# Test user list
curl http://localhost:8080/api/admin/users
```

### 4. Check Database Data
- Open phpMyAdmin
- Browse `vehicle_insurance_db` database
- Check `users` table for admin user
- Check `system_configurations` table for default settings

## 🔄 Development Workflow

### 1. Start XAMPP
- Always start Apache and MySQL services

### 2. Run Application
```bash
./gradlew bootRun
```

### 3. Make Changes
- Edit code
- Application will auto-reload (dev-tools enabled)

### 4. Database Changes
- Hibernate will auto-update schema (ddl-auto=update)
- For major changes, restart application

## 📱 Mobile Access

The application is responsive and works on mobile devices:
- **Mobile URL:** http://localhost:8080/admin
- **Responsive design** with Bootstrap 5
- **Touch-friendly** interface

## 🔒 Security Notes

### Default Credentials
- **Change default admin password** after first login
- **Update database credentials** for production
- **Enable SSL** for production deployment

### XAMPP Security
- **Don't use XAMPP for production**
- **Change default MySQL password**
- **Use proper firewall settings**

## 📈 Performance Tips

### Database Optimization
- **Indexes are created** for better performance
- **Connection pooling** is configured
- **Query optimization** with proper relationships

### Application Optimization
- **Enable caching** for production
- **Monitor memory usage**
- **Use production database** for large datasets

## 🆘 Support

### Common Commands
```bash
# Clean and rebuild
./gradlew clean build

# Run tests
./gradlew test

# Check dependencies
./gradlew dependencies

# Run with debug
./gradlew bootRun --debug-jvm
```

### Log Files
- **Application logs:** Console output
- **XAMPP logs:** C:\xampp\mysql\data\*.err
- **Apache logs:** C:\xampp\apache\logs\

---

## ✅ Success Checklist

- [ ] XAMPP MySQL is running
- [ ] Database `vehicle_insurance_db` exists
- [ ] Application starts without errors
- [ ] Can access http://localhost:8080/admin
- [ ] Can login with admin/admin123
- [ ] Dashboard loads with statistics
- [ ] User management works
- [ ] API endpoints respond correctly

**🎉 Your Vehicle Insurance Admin Module is now running with XAMPP!**
