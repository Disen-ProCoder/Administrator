-- Vehicle Insurance Management System - Admin Officer Module
-- Database Setup Script for XAMPP MySQL

-- Create database
CREATE DATABASE IF NOT EXISTS vehicle_insurance_db;
USE vehicle_insurance_db;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20),
    user_role VARCHAR(20) NOT NULL,
    user_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    last_login DATETIME,
    login_attempts INT DEFAULT 0,
    account_locked_until DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Create user_activities table
CREATE TABLE IF NOT EXISTS user_activities (
    activity_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    activity_type VARCHAR(50) NOT NULL,
    activity_description VARCHAR(500) NOT NULL,
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    session_id VARCHAR(100),
    activity_timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    success BOOLEAN DEFAULT TRUE,
    error_message VARCHAR(1000),
    additional_data VARCHAR(2000),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Create system_configurations table
CREATE TABLE IF NOT EXISTS system_configurations (
    config_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    config_key VARCHAR(100) UNIQUE NOT NULL,
    config_value VARCHAR(1000) NOT NULL,
    config_description VARCHAR(500),
    config_type VARCHAR(20) NOT NULL,
    is_encrypted BOOLEAN DEFAULT FALSE,
    is_read_only BOOLEAN DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50)
);

-- Insert default admin user
INSERT INTO users (username, email, password, first_name, last_name, user_role, user_status, created_by) 
VALUES ('admin', 'admin@vehicleinsurance.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Admin', 'Officer', 'ADMIN_OFFICER', 'ACTIVE', 'SYSTEM')
ON DUPLICATE KEY UPDATE username = username;

-- Insert default system configurations
INSERT INTO system_configurations (config_key, config_value, config_description, config_type, created_by) VALUES
('system.name', 'Vehicle Insurance Management System', 'System name', 'SYSTEM', 'SYSTEM'),
('system.version', '1.0.0', 'System version', 'SYSTEM', 'SYSTEM'),
('system.admin.email', 'admin@vehicleinsurance.com', 'Admin email address', 'SYSTEM', 'SYSTEM'),
('security.password.min.length', '8', 'Minimum password length', 'SECURITY', 'SYSTEM'),
('security.login.max.attempts', '5', 'Maximum login attempts before lockout', 'SECURITY', 'SYSTEM'),
('security.session.timeout', '30', 'Session timeout in minutes', 'SECURITY', 'SYSTEM'),
('email.smtp.host', 'localhost', 'SMTP server host', 'EMAIL', 'SYSTEM'),
('email.smtp.port', '587', 'SMTP server port', 'EMAIL', 'SYSTEM'),
('database.connection.pool.size', '10', 'Database connection pool size', 'DATABASE', 'SYSTEM'),
('ui.theme', 'default', 'Default UI theme', 'UI', 'SYSTEM'),
('business.insurance.default.validity', '365', 'Default insurance validity in days', 'BUSINESS', 'SYSTEM')
ON DUPLICATE KEY UPDATE config_value = VALUES(config_value);

-- Create indexes for better performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(user_role);
CREATE INDEX idx_users_status ON users(user_status);
CREATE INDEX idx_users_created_at ON users(created_at);

CREATE INDEX idx_activities_user_id ON user_activities(user_id);
CREATE INDEX idx_activities_type ON user_activities(activity_type);
CREATE INDEX idx_activities_timestamp ON user_activities(activity_timestamp);
CREATE INDEX idx_activities_success ON user_activities(success);

CREATE INDEX idx_config_key ON system_configurations(config_key);
CREATE INDEX idx_config_type ON system_configurations(config_type);

-- Show tables
SHOW TABLES;

-- Show user count
SELECT COUNT(*) as total_users FROM users;

-- Show configuration count
SELECT COUNT(*) as total_configurations FROM system_configurations;
