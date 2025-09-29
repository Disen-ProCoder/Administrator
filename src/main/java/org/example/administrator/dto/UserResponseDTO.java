package org.example.administrator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.administrator.enums.UserRole;
import org.example.administrator.enums.UserStatus;

import java.time.LocalDateTime;

/**
 * DTO for user response data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phoneNumber;
    private UserRole userRole;
    private UserStatus userStatus;
    private LocalDateTime lastLogin;
    private Integer loginAttempts;
    private LocalDateTime accountLockedUntil;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private Boolean isDeleted;

    /**
     * Check if the user account is locked
     */
    public boolean isAccountLocked() {
        return accountLockedUntil != null && accountLockedUntil.isAfter(LocalDateTime.now());
    }

    /**
     * Check if the user can login
     */
    public boolean canLogin() {
        return userStatus == UserStatus.ACTIVE && !isAccountLocked() && !isDeleted;
    }
}

