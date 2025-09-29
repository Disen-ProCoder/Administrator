package org.example.administrator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for system reports and analytics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemReportDTO {

    private String reportType;
    private String reportTitle;
    private LocalDateTime generatedAt;
    private String generatedBy;
    private Map<String, Object> reportData;
    private String description;

    // User statistics
    private Long totalUsers;
    private Long activeUsers;
    private Long blockedUsers;
    private Long pendingUsers;
    private Map<String, Long> usersByRole;
    private Map<String, Long> usersByStatus;

    // Activity statistics
    private Long totalActivities;
    private Long successfulActivities;
    private Long failedActivities;
    private Map<String, Long> activitiesByType;
    private Long activitiesLast24Hours;
    private Long activitiesLast7Days;
    private Long activitiesLast30Days;

    // System statistics
    private Long totalConfigurations;
    private Map<String, Long> configurationsByType;
    private Long systemUptime;
    private String systemVersion;
    private LocalDateTime lastSystemRestart;

    // Security statistics
    private Long failedLoginAttempts;
    private Long lockedAccounts;
    private Long suspiciousActivities;
    private Map<String, Long> activitiesByIpAddress;
}

