package org.example.administrator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.administrator.dto.SystemReportDTO;
import org.example.administrator.dto.UserActivityDTO;
import org.example.administrator.dto.UserResponseDTO;
import org.example.administrator.entity.SystemConfiguration;
import org.example.administrator.entity.User;
import org.example.administrator.enums.UserRole;
import org.example.administrator.enums.UserStatus;
import org.example.administrator.repository.UserActivityRepository;
import org.example.administrator.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main service class for admin operations and system management
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminService {

    private final UserService userService;
    private final UserActivityService userActivityService;
    private final SystemConfigurationService systemConfigurationService;
    private final UserRepository userRepository;
    private final UserActivityRepository userActivityRepository;

    /**
     * Generate system overview report
     */
    @Transactional(readOnly = true)
    public SystemReportDTO generateSystemOverviewReport() {
        log.info("Generating system overview report");

        SystemReportDTO report = new SystemReportDTO();
        report.setReportType("SYSTEM_OVERVIEW");
        report.setReportTitle("System Overview Report");
        report.setGeneratedAt(LocalDateTime.now());
        report.setGeneratedBy("SYSTEM");

        // User statistics
        report.setTotalUsers(userService.getUserCount());
        report.setActiveUsers(userService.getUserCountByStatus(UserStatus.ACTIVE));
        report.setBlockedUsers(userService.getUserCountByStatus(UserStatus.BLOCKED));
        report.setPendingUsers(userService.getUserCountByStatus(UserStatus.PENDING));

        // Users by role
        Map<String, Long> usersByRole = new HashMap<>();
        for (UserRole role : UserRole.values()) {
            usersByRole.put(role.name(), userService.getUserCountByRole(role));
        }
        report.setUsersByRole(usersByRole);

        // Users by status
        Map<String, Long> usersByStatus = new HashMap<>();
        for (UserStatus status : UserStatus.values()) {
            usersByStatus.put(status.name(), userService.getUserCountByStatus(status));
        }
        report.setUsersByStatus(usersByStatus);

        // Activity statistics
        report.setTotalActivities(userActivityService.getTotalActivityCount());
        report.setSuccessfulActivities(userActivityService.getActivityCountBySuccess(true));
        report.setFailedActivities(userActivityService.getActivityCountBySuccess(false));
        report.setActivitiesLast24Hours(userActivityService.getActivityCountBetweenDates(
            LocalDateTime.now().minusHours(24), LocalDateTime.now()));
        report.setActivitiesLast7Days(userActivityService.getActivityCountBetweenDates(
            LocalDateTime.now().minusDays(7), LocalDateTime.now()));
        report.setActivitiesLast30Days(userActivityService.getActivityCountBetweenDates(
            LocalDateTime.now().minusDays(30), LocalDateTime.now()));

        // System statistics
        report.setTotalConfigurations(systemConfigurationService.getTotalConfigurationCount());
        report.setSystemVersion("1.0.0");
        report.setLastSystemRestart(LocalDateTime.now().minusDays(1)); // Placeholder

        // Security statistics
        report.setFailedLoginAttempts(userActivityService.getActivityCountByType("LOGIN_FAILED"));
        report.setLockedAccounts(userService.getUserCountByStatus(UserStatus.BLOCKED));

        log.info("System overview report generated successfully");
        return report;
    }

    /**
     * Generate user statistics report
     */
    @Transactional(readOnly = true)
    public SystemReportDTO generateUserStatisticsReport() {
        log.info("Generating user statistics report");

        SystemReportDTO report = new SystemReportDTO();
        report.setReportType("USER_STATISTICS");
        report.setReportTitle("User Statistics Report");
        report.setGeneratedAt(LocalDateTime.now());
        report.setGeneratedBy("SYSTEM");

        // User counts
        report.setTotalUsers(userService.getUserCount());
        report.setActiveUsers(userService.getUserCountByStatus(UserStatus.ACTIVE));
        report.setBlockedUsers(userService.getUserCountByStatus(UserStatus.BLOCKED));
        report.setPendingUsers(userService.getUserCountByStatus(UserStatus.PENDING));

        // Users by role
        Map<String, Long> usersByRole = new HashMap<>();
        for (UserRole role : UserRole.values()) {
            usersByRole.put(role.name(), userService.getUserCountByRole(role));
        }
        report.setUsersByRole(usersByRole);

        // Users by status
        Map<String, Long> usersByStatus = new HashMap<>();
        for (UserStatus status : UserStatus.values()) {
            usersByStatus.put(status.name(), userService.getUserCountByStatus(status));
        }
        report.setUsersByStatus(usersByStatus);

        // Recent user activities
        List<UserActivityDTO> recentActivities = userActivityService.getRecentActivities(24);
        report.setActivitiesLast24Hours((long) recentActivities.size());

        log.info("User statistics report generated successfully");
        return report;
    }

    /**
     * Generate activity report
     */
    @Transactional(readOnly = true)
    public SystemReportDTO generateActivityReport(int hours) {
        log.info("Generating activity report for last {} hours", hours);

        SystemReportDTO report = new SystemReportDTO();
        report.setReportType("ACTIVITY_REPORT");
        report.setReportTitle("Activity Report - Last " + hours + " Hours");
        report.setGeneratedAt(LocalDateTime.now());
        report.setGeneratedBy("SYSTEM");

        LocalDateTime startDate = LocalDateTime.now().minusHours(hours);
        LocalDateTime endDate = LocalDateTime.now();

        // Activity statistics
        report.setTotalActivities(userActivityService.getActivityCountBetweenDates(startDate, endDate));
        report.setSuccessfulActivities(userActivityService.getActivityCountBySuccess(true));
        report.setFailedActivities(userActivityService.getActivityCountBySuccess(false));

        // Activities by type
        Map<String, Long> activitiesByType = new HashMap<>();
        List<Object[]> activityTypes = userActivityService.getMostFrequentActivityTypes();
        for (Object[] activityType : activityTypes) {
            activitiesByType.put((String) activityType[0], (Long) activityType[1]);
        }
        report.setActivitiesByType(activitiesByType);

        // Recent activities
        List<UserActivityDTO> recentActivities = userActivityService.getRecentActivities(hours);
        report.setActivitiesLast24Hours((long) recentActivities.size());

        log.info("Activity report generated successfully");
        return report;
    }

    /**
     * Get system health status
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getSystemHealthStatus() {
        log.info("Checking system health status");

        Map<String, Object> healthStatus = new HashMap<>();

        // Database connectivity
        try {
            userRepository.count();
            healthStatus.put("database", "HEALTHY");
        } catch (Exception e) {
            healthStatus.put("database", "UNHEALTHY");
            log.error("Database connectivity issue", e);
        }

        // User system status
        long totalUsers = userService.getUserCount();
        long activeUsers = userService.getUserCountByStatus(UserStatus.ACTIVE);
        healthStatus.put("totalUsers", totalUsers);
        healthStatus.put("activeUsers", activeUsers);
        healthStatus.put("userSystem", totalUsers > 0 ? "HEALTHY" : "WARNING");

        // Activity system status
        long totalActivities = userActivityService.getTotalActivityCount();
        long failedActivities = userActivityService.getActivityCountBySuccess(false);
        double failureRate = totalActivities > 0 ? (double) failedActivities / totalActivities : 0;
        healthStatus.put("totalActivities", totalActivities);
        healthStatus.put("failedActivities", failedActivities);
        healthStatus.put("failureRate", failureRate);
        healthStatus.put("activitySystem", failureRate < 0.1 ? "HEALTHY" : "WARNING");

        // Configuration system status
        long totalConfigurations = systemConfigurationService.getTotalConfigurationCount();
        healthStatus.put("totalConfigurations", totalConfigurations);
        healthStatus.put("configurationSystem", totalConfigurations > 0 ? "HEALTHY" : "WARNING");

        // Overall system status
        boolean allHealthy = healthStatus.get("database").equals("HEALTHY") &&
                           healthStatus.get("userSystem").equals("HEALTHY") &&
                           healthStatus.get("activitySystem").equals("HEALTHY") &&
                           healthStatus.get("configurationSystem").equals("HEALTHY");
        healthStatus.put("overallStatus", allHealthy ? "HEALTHY" : "WARNING");

        healthStatus.put("timestamp", LocalDateTime.now());

        log.info("System health status checked: {}", healthStatus.get("overallStatus"));
        return healthStatus;
    }

    /**
     * Get dashboard statistics
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        log.info("Getting dashboard statistics");

        Map<String, Object> statistics = new HashMap<>();

        // User statistics
        statistics.put("totalUsers", userService.getUserCount());
        statistics.put("activeUsers", userService.getUserCountByStatus(UserStatus.ACTIVE));
        statistics.put("blockedUsers", userService.getUserCountByStatus(UserStatus.BLOCKED));
        statistics.put("pendingUsers", userService.getUserCountByStatus(UserStatus.PENDING));

        // Activity statistics
        statistics.put("totalActivities", userActivityService.getTotalActivityCount());
        statistics.put("activitiesLast24Hours", userActivityService.getActivityCountBetweenDates(
            LocalDateTime.now().minusHours(24), LocalDateTime.now()));
        statistics.put("activitiesLast7Days", userActivityService.getActivityCountBetweenDates(
            LocalDateTime.now().minusDays(7), LocalDateTime.now()));

        // System statistics
        statistics.put("totalConfigurations", systemConfigurationService.getTotalConfigurationCount());

        // Recent activities
        List<UserActivityDTO> recentActivities = userActivityService.getRecentActivities(24);
        statistics.put("recentActivities", recentActivities);

        // Failed activities
        List<UserActivityDTO> failedActivities = userActivityService.getFailedActivities();
        statistics.put("failedActivities", failedActivities);

        // Users by role
        Map<String, Long> usersByRole = new HashMap<>();
        for (UserRole role : UserRole.values()) {
            usersByRole.put(role.name(), userService.getUserCountByRole(role));
        }
        statistics.put("usersByRole", usersByRole);

        // Users by status
        Map<String, Long> usersByStatus = new HashMap<>();
        for (UserStatus status : UserStatus.values()) {
            usersByStatus.put(status.name(), userService.getUserCountByStatus(status));
        }
        statistics.put("usersByStatus", usersByStatus);

        statistics.put("timestamp", LocalDateTime.now());

        log.info("Dashboard statistics retrieved successfully");
        return statistics;
    }

    /**
     * Clean up old data
     */
    public void cleanupOldData(int daysToKeep) {
        log.info("Starting cleanup of old data, keeping last {} days", daysToKeep);

        // Clean up old activities
        userActivityService.cleanupOldActivities(daysToKeep);

        log.info("Old data cleanup completed");
    }

    /**
     * Get system configuration summary
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getSystemConfigurationSummary() {
        log.info("Getting system configuration summary");

        Map<String, Object> summary = new HashMap<>();

        summary.put("totalConfigurations", systemConfigurationService.getTotalConfigurationCount());
        summary.put("readOnlyConfigurations", systemConfigurationService.getReadOnlyConfigurationCount());
        summary.put("encryptedConfigurations", systemConfigurationService.getEncryptedConfigurationCount());

        // Configuration counts by type
        Map<String, Long> configurationsByType = new HashMap<>();
        for (SystemConfiguration.ConfigurationType type : SystemConfiguration.ConfigurationType.values()) {
            configurationsByType.put(type.name(), systemConfigurationService.getConfigurationCountByType(type));
        }
        summary.put("configurationsByType", configurationsByType);

        summary.put("timestamp", LocalDateTime.now());

        log.info("System configuration summary retrieved successfully");
        return summary;
    }
}
