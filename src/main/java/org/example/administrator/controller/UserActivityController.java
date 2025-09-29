package org.example.administrator.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.administrator.dto.UserActivityDTO;
import org.example.administrator.service.UserActivityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for user activity operations
 */
@RestController
@RequestMapping("/api/admin/activities")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserActivityController {

    private final UserActivityService userActivityService;

    /**
     * Get user activities
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<UserActivityDTO>> getUserActivities(@PathVariable Long userId) {
        log.info("Getting activities for user: {}", userId);
        List<UserActivityDTO> activities = userActivityService.getUserActivities(userId);
        return ResponseEntity.ok(activities);
    }

    /**
     * Get user activities with pagination
     */
    @GetMapping("/user/{userId}/paged")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<Page<UserActivityDTO>> getUserActivities(
            @PathVariable Long userId, Pageable pageable) {
        log.info("Getting activities for user: {} with pagination", userId);
        Page<UserActivityDTO> activities = userActivityService.getUserActivities(userId, pageable);
        return ResponseEntity.ok(activities);
    }

    /**
     * Get recent activities
     */
    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<UserActivityDTO>> getRecentActivities(
            @RequestParam(defaultValue = "24") int hours) {
        log.info("Getting recent activities for last {} hours", hours);
        List<UserActivityDTO> activities = userActivityService.getRecentActivities(hours);
        return ResponseEntity.ok(activities);
    }

    /**
     * Get recent activities with pagination
     */
    @GetMapping("/recent/paged")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<Page<UserActivityDTO>> getRecentActivities(
            @RequestParam(defaultValue = "24") int hours, Pageable pageable) {
        log.info("Getting recent activities for last {} hours with pagination", hours);
        Page<UserActivityDTO> activities = userActivityService.getRecentActivities(hours, pageable);
        return ResponseEntity.ok(activities);
    }

    /**
     * Get activities by type
     */
    @GetMapping("/type/{activityType}")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<UserActivityDTO>> getActivitiesByType(
            @PathVariable String activityType) {
        log.info("Getting activities by type: {}", activityType);
        List<UserActivityDTO> activities = userActivityService.getActivitiesByType(activityType);
        return ResponseEntity.ok(activities);
    }

    /**
     * Get activities by type with pagination
     */
    @GetMapping("/type/{activityType}/paged")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<Page<UserActivityDTO>> getActivitiesByType(
            @PathVariable String activityType, Pageable pageable) {
        log.info("Getting activities by type: {} with pagination", activityType);
        Page<UserActivityDTO> activities = userActivityService.getActivitiesByType(activityType, pageable);
        return ResponseEntity.ok(activities);
    }

    /**
     * Get failed activities
     */
    @GetMapping("/failed")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<UserActivityDTO>> getFailedActivities() {
        log.info("Getting failed activities");
        List<UserActivityDTO> activities = userActivityService.getFailedActivities();
        return ResponseEntity.ok(activities);
    }

    /**
     * Get failed activities with pagination
     */
    @GetMapping("/failed/paged")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<Page<UserActivityDTO>> getFailedActivities(Pageable pageable) {
        log.info("Getting failed activities with pagination");
        Page<UserActivityDTO> activities = userActivityService.getFailedActivities(pageable);
        return ResponseEntity.ok(activities);
    }

    /**
     * Get activities within date range
     */
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<UserActivityDTO>> getActivitiesBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("Getting activities between {} and {}", startDate, endDate);
        List<UserActivityDTO> activities = userActivityService.getActivitiesBetweenDates(startDate, endDate);
        return ResponseEntity.ok(activities);
    }

    /**
     * Get activities within date range with pagination
     */
    @GetMapping("/date-range/paged")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<Page<UserActivityDTO>> getActivitiesBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        log.info("Getting activities between {} and {} with pagination", startDate, endDate);
        Page<UserActivityDTO> activities = userActivityService.getActivitiesBetweenDates(startDate, endDate, pageable);
        return ResponseEntity.ok(activities);
    }

    /**
     * Get activities by IP address
     */
    @GetMapping("/ip/{ipAddress}")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<UserActivityDTO>> getActivitiesByIpAddress(
            @PathVariable String ipAddress) {
        log.info("Getting activities by IP address: {}", ipAddress);
        List<UserActivityDTO> activities = userActivityService.getActivitiesByIpAddress(ipAddress);
        return ResponseEntity.ok(activities);
    }

    /**
     * Get activities by session ID
     */
    @GetMapping("/session/{sessionId}")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<UserActivityDTO>> getActivitiesBySessionId(
            @PathVariable String sessionId) {
        log.info("Getting activities by session ID: {}", sessionId);
        List<UserActivityDTO> activities = userActivityService.getActivitiesBySessionId(sessionId);
        return ResponseEntity.ok(activities);
    }

    /**
     * Search activities by description
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<UserActivityDTO>> searchActivitiesByDescription(
            @RequestParam String searchTerm) {
        log.info("Searching activities by description: {}", searchTerm);
        List<UserActivityDTO> activities = userActivityService.searchActivitiesByDescription(searchTerm);
        return ResponseEntity.ok(activities);
    }

    /**
     * Search activities by description with pagination
     */
    @GetMapping("/search/paged")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<Page<UserActivityDTO>> searchActivitiesByDescription(
            @RequestParam String searchTerm, Pageable pageable) {
        log.info("Searching activities by description: {} with pagination", searchTerm);
        Page<UserActivityDTO> activities = userActivityService.searchActivitiesByDescription(searchTerm, pageable);
        return ResponseEntity.ok(activities);
    }

    /**
     * Get activity statistics
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<Object> getActivityStatistics() {
        log.info("Getting activity statistics");
        
        // Create a response object with activity statistics
        Object statistics = new Object() {
            public final long totalActivities = userActivityService.getTotalActivityCount();
            public final long successfulActivities = userActivityService.getActivityCountBySuccess(true);
            public final long failedActivities = userActivityService.getActivityCountBySuccess(false);
        };
        
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get most frequent activity types
     */
    @GetMapping("/frequent-types")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<Object[]>> getMostFrequentActivityTypes() {
        log.info("Getting most frequent activity types");
        List<Object[]> activityTypes = userActivityService.getMostFrequentActivityTypes();
        return ResponseEntity.ok(activityTypes);
    }
}

