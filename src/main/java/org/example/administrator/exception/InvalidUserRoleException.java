package org.example.administrator.exception;

/**
 * Exception thrown when an invalid user role is provided
 */
public class InvalidUserRoleException extends RuntimeException {

    public InvalidUserRoleException(String message) {
        super(message);
    }

    public InvalidUserRoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUserRoleException(String role, boolean isRole) {
        super("Invalid user role: " + role);
    }
}
