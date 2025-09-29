package org.example.administrator.enums;

/**
 * Enum representing different user statuses in the Vehicle Insurance Management System
 */
public enum UserStatus {
    ACTIVE("Active", "User is active and can access the system"),
    INACTIVE("Inactive", "User account is inactive"),
    BLOCKED("Blocked", "User account is blocked by administrator"),
    PENDING("Pending", "User account is pending approval"),
    SUSPENDED("Suspended", "User account is temporarily suspended"),
    EXPIRED("Expired", "User account has expired");

    private final String displayName;
    private final String description;

    UserStatus(String displayName, String description) {
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
     * Check if the user can access the system
     */
    public boolean canAccess() {
        return this == ACTIVE;
    }

    /**
     * Check if the user account is in a problematic state
     */
    public boolean isProblematic() {
        return this == BLOCKED || this == SUSPENDED || this == EXPIRED;
    }

    /**
     * Check if the user account needs attention
     */
    public boolean needsAttention() {
        return this == PENDING || this == INACTIVE;
    }
}

