package org.example.administrator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for user activity data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityDTO {

    private Long id;
    private Long userId;
    private String username;
    private String activityType;
    private String activityDescription;
    private String ipAddress;
    private String userAgent;
    private String sessionId;
    private LocalDateTime activityTimestamp;
    private Boolean success;
    private String errorMessage;
    private String additionalData;
    private LocalDateTime createdAt;
}

