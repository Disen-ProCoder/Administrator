package org.example.administrator.enums;

/**
 * Enum representing different user roles in the Vehicle Insurance Management System
 */
public enum UserRole {
    ADMIN_OFFICER("Admin Officer", "Full system access and user management"),
    POLICY_OFFICER("Policy Officer", "Policy creation and management"),
    CLAIMS_OFFICER("Claims Officer", "Claims processing and management"),
    CUSTOMER_SERVICE("Customer Service", "Customer support and assistance"),
    FINANCE_OFFICER("Finance Officer", "Financial operations and billing"),
    SYSTEM_ADMIN("System Administrator", "System configuration and maintenance");

    private final String displayName;
    private final String description;

    UserRole(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Check if the role has admin privileges
     */
    public boolean isAdmin() {
        return this == ADMIN_OFFICER || this == SYSTEM_ADMIN;
    }

    /**
     * Check if the role can manage users
     */
    public boolean canManageUsers() {
        return this == ADMIN_OFFICER || this == SYSTEM_ADMIN;
    }

    /**
     * Check if the role can access system configuration
     */
    public boolean canAccessSystemConfig() {
        return this == ADMIN_OFFICER || this == SYSTEM_ADMIN;
    }
}

