@echo off
echo ========================================
echo Vehicle Insurance Administrator
echo Spring Boot Application Startup
echo ========================================
echo.

echo [INFO] Starting Vehicle Insurance Administrator Application...
echo [INFO] Make sure XAMPP MySQL is running on localhost:3306
echo [INFO] Database: vehicle_insurance_db
echo [INFO] Default Admin User: admin / admin123
echo.

echo [INFO] Starting Spring Boot application...
echo [INFO] Application will be available at: http://localhost:8080
echo [INFO] Login page: http://localhost:8080/login
echo [INFO] Dashboard: http://localhost:8080/admin/dashboard
echo.

gradlew.bat bootRun

echo.
echo [INFO] Application stopped.
pause
