package org.example.administrator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.administrator.dto.UserActivityDTO;
import org.example.administrator.entity.User;
import org.example.administrator.entity.UserActivity;
import org.example.administrator.repository.UserActivityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for user activity logging and monitoring
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserActivityService {

    private final UserActivityRepository userActivityRepository;

    /**
     * Log user activity
     */
    public UserActivity logActivity(User user, String activityType, String description, boolean success) {
        return logActivity(user, activityType, description, success, null, null);
    }

    /**
     * Log user activity with additional data
     */
    public UserActivity logActivity(User user, String activityType, String description, 
                                  boolean success, String additionalData, HttpServletRequest request) {
        log.info("Logging activity for user {}: {} - {}", user.getUsername(), activityType, description);

        UserActivity activity = new UserActivity();
        activity.setUser(user);
        activity.setActivityType(activityType);
        activity.setActivityDescription(description);
        activity.setSuccess(success);
        activity.setAdditionalData(additionalData);

        if (request != null) {
            activity.setIpAddress(getClientIpAddress(request));
            activity.setUserAgent(request.getHeader("User-Agent"));
            activity.setSessionId(request.getSession().getId());
        }

        UserActivity savedActivity = userActivityRepository.save(activity);
        log.info("Activity logged successfully: {}", savedActivity.getId());
        return savedActivity;
    }

    /**
     * Log successful activity
     */
    public UserActivity logSuccessActivity(User user, String activityType, String description) {
        return logActivity(user, activityType, description, true);
    }

    /**
     * Log failed activity
     */
    public UserActivity logFailureActivity(User user, String activityType, String description, String errorMessage) {
        UserActivity activity = logActivity(user, activityType, description, false);
        activity.setErrorMessage(errorMessage);
        return userActivityRepository.save(activity);
    }

    /**
     * Get user activities
     */
    @Transactional(readOnly = true)
    public List<UserActivityDTO> getUserActivities(Long userId) {
        User user = new User();
        user.setId(userId);
        
        return userActivityRepository.findByUserOrderByActivityTimestampDesc(user)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get user activities with pagination
     */
    @Transactional(readOnly = true)
    public Page<UserActivityDTO> getUserActivities(Long userId, Pageable pageable) {
        User user = new User();
        user.setId(userId);
        
        return userActivityRepository.findByUserOrderByActivityTimestampDesc(user, pageable)
            .map(this::convertToDTO);
    }

    /**
     * Get recent activities
     */
    @Transactional(readOnly = true)
    public List<UserActivityDTO> getRecentActivities(int hours) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusHours(hours);
        
        return userActivityRepository.findRecentActivities(cutoffDate)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get recent activities with pagination
     */
    @Transactional(readOnly = true)
    public Page<UserActivityDTO> getRecentActivities(int hours, Pageable pageable) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusHours(hours);
        
        return userActivityRepository.findRecentActivities(cutoffDate, pageable)
            .map(this::convertToDTO);
    }

    /**
     * Get activities by type
     */
    @Transactional(readOnly = true)
    public List<UserActivityDTO> getActivitiesByType(String activityType) {
        return userActivityRepository.findByActivityTypeOrderByActivityTimestampDesc(activityType)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get activities by type with pagination
     */
    @Transactional(readOnly = true)
    public Page<UserActivityDTO> getActivitiesByType(String activityType, Pageable pageable) {
        return userActivityRepository.findByActivityTypeOrderByActivityTimestampDesc(activityType, pageable)
            .map(this::convertToDTO);
    }

    /**
     * Get failed activities
     */
    @Transactional(readOnly = true)
    public List<UserActivityDTO> getFailedActivities() {
        return userActivityRepository.findFailedActivities()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get failed activities with pagination
     */
    @Transactional(readOnly = true)
    public Page<UserActivityDTO> getFailedActivities(Pageable pageable) {
        return userActivityRepository.findFailedActivities(pageable)
            .map(this::convertToDTO);
    }

    /**
     * Get activities within date range
     */
    @Transactional(readOnly = true)
    public List<UserActivityDTO> getActivitiesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return userActivityRepository.findActivitiesBetweenDates(startDate, endDate)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get activities within date range with pagination
     */
    @Transactional(readOnly = true)
    public Page<UserActivityDTO> getActivitiesBetweenDates(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return userActivityRepository.findActivitiesBetweenDates(startDate, endDate, pageable)
            .map(this::convertToDTO);
    }

    /**
     * Get activities by IP address
     */
    @Transactional(readOnly = true)
    public List<UserActivityDTO> getActivitiesByIpAddress(String ipAddress) {
        return userActivityRepository.findByIpAddressOrderByActivityTimestampDesc(ipAddress)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get activities by session ID
     */
    @Transactional(readOnly = true)
    public List<UserActivityDTO> getActivitiesBySessionId(String sessionId) {
        return userActivityRepository.findBySessionIdOrderByActivityTimestampDesc(sessionId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Search activities by description
     */
    @Transactional(readOnly = true)
    public List<UserActivityDTO> searchActivitiesByDescription(String searchTerm) {
        return userActivityRepository.findActivitiesByDescriptionContaining(searchTerm)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Search activities by description with pagination
     */
    @Transactional(readOnly = true)
    public Page<UserActivityDTO> searchActivitiesByDescription(String searchTerm, Pageable pageable) {
        return userActivityRepository.findActivitiesByDescriptionContaining(searchTerm, pageable)
            .map(this::convertToDTO);
    }

    /**
     * Get activity statistics
     */
    @Transactional(readOnly = true)
    public long getTotalActivityCount() {
        return userActivityRepository.count();
    }

    @Transactional(readOnly = true)
    public long getActivityCountByUser(Long userId) {
        User user = new User();
        user.setId(userId);
        return userActivityRepository.countByUser(user);
    }

    @Transactional(readOnly = true)
    public long getActivityCountByType(String activityType) {
        return userActivityRepository.countByActivityType(activityType);
    }

    @Transactional(readOnly = true)
    public long getActivityCountBySuccess(boolean success) {
        return userActivityRepository.countBySuccess(success);
    }

    @Transactional(readOnly = true)
    public long getActivityCountBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return userActivityRepository.countActivitiesBetweenDates(startDate, endDate);
    }

    /**
     * Get most frequent activity types
     */
    @Transactional(readOnly = true)
    public List<Object[]> getMostFrequentActivityTypes() {
        return userActivityRepository.findMostFrequentActivityTypes();
    }

    /**
     * Clean up old activities
     */
    public void cleanupOldActivities(int daysToKeep) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        userActivityRepository.deleteOldActivities(cutoffDate);
        log.info("Cleaned up activities older than {} days", daysToKeep);
    }

    /**
     * Get client IP address from request
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    /**
     * Convert UserActivity entity to UserActivityDTO
     */
    private UserActivityDTO convertToDTO(UserActivity activity) {
        UserActivityDTO dto = new UserActivityDTO();
        dto.setId(activity.getId());
        dto.setUserId(activity.getUser().getId());
        dto.setUsername(activity.getUser().getUsername());
        dto.setActivityType(activity.getActivityType());
        dto.setActivityDescription(activity.getActivityDescription());
        dto.setIpAddress(activity.getIpAddress());
        dto.setUserAgent(activity.getUserAgent());
        dto.setSessionId(activity.getSessionId());
        dto.setActivityTimestamp(activity.getActivityTimestamp());
        dto.setSuccess(activity.getSuccess());
        dto.setErrorMessage(activity.getErrorMessage());
        dto.setAdditionalData(activity.getAdditionalData());
        dto.setCreatedAt(activity.getCreatedAt());
        return dto;
    }
}

