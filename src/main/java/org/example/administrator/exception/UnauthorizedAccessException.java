package org.example.administrator.exception;

/**
 * Exception thrown when a user tries to access resources they don't have permission for
 */
public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedAccessException(String user, String resource) {
        super("User " + user + " is not authorized to access " + resource);
    }
}

