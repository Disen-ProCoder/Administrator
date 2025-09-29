package org.example.administrator.exception;

import java.time.LocalDateTime;

/**
 * Exception thrown when trying to access a locked account
 */
public class AccountLockedException extends RuntimeException {

    private final LocalDateTime lockedUntil;

    public AccountLockedException(String message) {
        super(message);
        this.lockedUntil = null;
    }

    public AccountLockedException(String message, LocalDateTime lockedUntil) {
        super(message);
        this.lockedUntil = lockedUntil;
    }

    public AccountLockedException(String message, Throwable cause) {
        super(message, cause);
        this.lockedUntil = null;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public boolean isLocked() {
        return lockedUntil != null && lockedUntil.isAfter(LocalDateTime.now());
    }
}

