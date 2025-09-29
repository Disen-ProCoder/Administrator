package org.example.administrator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.administrator.dto.SystemReportDTO;
import org.example.administrator.dto.UserCreateDTO;
import org.example.administrator.dto.UserResponseDTO;
import org.example.administrator.dto.UserUpdateDTO;
import org.example.administrator.enums.UserRole;
import org.example.administrator.enums.UserStatus;
import org.example.administrator.service.AdminService;
import org.example.administrator.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for admin operations
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    /**
     * Get dashboard statistics
     */
    @GetMapping("/dashboard/statistics")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<Map<String, Object>> getDashboardStatistics() {
        log.info("Getting dashboard statistics");
        Map<String, Object> statistics = adminService.getDashboardStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get system health status
     */
    @GetMapping("/system/health")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<Map<String, Object>> getSystemHealthStatus() {
        log.info("Getting system health status");
        Map<String, Object> healthStatus = adminService.getSystemHealthStatus();
        return ResponseEntity.ok(healthStatus);
    }

    /**
     * Generate system overview report
     */
    @GetMapping("/reports/system")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<SystemReportDTO> generateSystemOverviewReport() {
        log.info("Generating system overview report");
        SystemReportDTO report = adminService.generateSystemOverviewReport();
        return ResponseEntity.ok(report);
    }

    /**
     * Generate user statistics report
     */
    @GetMapping("/reports/users")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<SystemReportDTO> generateUserStatisticsReport() {
        log.info("Generating user statistics report");
        SystemReportDTO report = adminService.generateUserStatisticsReport();
        return ResponseEntity.ok(report);
    }

    /**
     * Generate activity report
     */
    @GetMapping("/reports/activities")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<SystemReportDTO> generateActivityReport(
            @RequestParam(defaultValue = "24") int hours) {
        log.info("Generating activity report for last {} hours", hours);
        SystemReportDTO report = adminService.generateActivityReport(hours);
        return ResponseEntity.ok(report);
    }

    /**
     * Get system configuration summary
     */
    @GetMapping("/system/configuration/summary")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<Map<String, Object>> getSystemConfigurationSummary() {
        log.info("Getting system configuration summary");
        Map<String, Object> summary = adminService.getSystemConfigurationSummary();
        return ResponseEntity.ok(summary);
    }

    /**
     * Clean up old data
     */
    @PostMapping("/system/cleanup")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<String> cleanupOldData(
            @RequestParam(defaultValue = "30") int daysToKeep) {
        log.info("Starting cleanup of old data, keeping last {} days", daysToKeep);
        adminService.cleanupOldData(daysToKeep);
        return ResponseEntity.ok("Old data cleanup completed successfully");
    }
}

