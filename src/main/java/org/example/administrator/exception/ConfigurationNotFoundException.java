package org.example.administrator.exception;

/**
 * Exception thrown when a system configuration is not found
 */
public class ConfigurationNotFoundException extends RuntimeException {

    public ConfigurationNotFoundException(String message) {
        super(message);
    }

    public ConfigurationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationNotFoundException(String configKey, boolean isKey) {
        super("Configuration not found with key: " + configKey);
    }
}
