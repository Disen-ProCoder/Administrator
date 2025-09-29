package org.example.administrator.repository;

import org.example.administrator.entity.User;
import org.example.administrator.entity.UserActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for UserActivity entity with custom queries
 */
@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

    /**
     * Find activities by user
     */
    List<UserActivity> findByUserOrderByActivityTimestampDesc(User user);

    /**
     * Find activities by user with pagination
     */
    Page<UserActivity> findByUserOrderByActivityTimestampDesc(User user, Pageable pageable);

    /**
     * Find activities by activity type
     */
    List<UserActivity> findByActivityTypeOrderByActivityTimestampDesc(String activityType);

    /**
     * Find activities by activity type with pagination
     */
    Page<UserActivity> findByActivityTypeOrderByActivityTimestampDesc(String activityType, Pageable pageable);

    /**
     * Find activities by user and activity type
     */
    List<UserActivity> findByUserAndActivityTypeOrderByActivityTimestampDesc(User user, String activityType);

    /**
     * Find activities by success status
     */
    List<UserActivity> findBySuccessOrderByActivityTimestampDesc(Boolean success);

    /**
     * Find activities by success status with pagination
     */
    Page<UserActivity> findBySuccessOrderByActivityTimestampDesc(Boolean success, Pageable pageable);

    /**
     * Find activities within a date range
     */
    @Query("SELECT ua FROM UserActivity ua WHERE ua.activityTimestamp BETWEEN :startDate AND :endDate ORDER BY ua.activityTimestamp DESC")
    List<UserActivity> findActivitiesBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                                @Param("endDate") LocalDateTime endDate);

    /**
     * Find activities within a date range with pagination
     */
    @Query("SELECT ua FROM UserActivity ua WHERE ua.activityTimestamp BETWEEN :startDate AND :endDate ORDER BY ua.activityTimestamp DESC")
    Page<UserActivity> findActivitiesBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                                @Param("endDate") LocalDateTime endDate, 
                                                Pageable pageable);

    /**
     * Find activities by user within a date range
     */
    @Query("SELECT ua FROM UserActivity ua WHERE ua.user = :user AND ua.activityTimestamp BETWEEN :startDate AND :endDate ORDER BY ua.activityTimestamp DESC")
    List<UserActivity> findUserActivitiesBetweenDates(@Param("user") User user, 
                                                     @Param("startDate") LocalDateTime startDate, 
                                                     @Param("endDate") LocalDateTime endDate);

    /**
     * Find recent activities
     */
    @Query("SELECT ua FROM UserActivity ua WHERE ua.activityTimestamp >= :cutoffDate ORDER BY ua.activityTimestamp DESC")
    List<UserActivity> findRecentActivities(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Find recent activities with pagination
     */
    @Query("SELECT ua FROM UserActivity ua WHERE ua.activityTimestamp >= :cutoffDate ORDER BY ua.activityTimestamp DESC")
    Page<UserActivity> findRecentActivities(@Param("cutoffDate") LocalDateTime cutoffDate, Pageable pageable);

    /**
     * Find failed activities
     */
    @Query("SELECT ua FROM UserActivity ua WHERE ua.success = false ORDER BY ua.activityTimestamp DESC")
    List<UserActivity> findFailedActivities();

    /**
     * Find failed activities with pagination
     */
    @Query("SELECT ua FROM UserActivity ua WHERE ua.success = false ORDER BY ua.activityTimestamp DESC")
    Page<UserActivity> findFailedActivities(Pageable pageable);

    /**
     * Find activities by IP address
     */
    List<UserActivity> findByIpAddressOrderByActivityTimestampDesc(String ipAddress);

    /**
     * Find activities by session ID
     */
    List<UserActivity> findBySessionIdOrderByActivityTimestampDesc(String sessionId);

    /**
     * Count activities by user
     */
    long countByUser(User user);

    /**
     * Count activities by activity type
     */
    long countByActivityType(String activityType);

    /**
     * Count activities by success status
     */
    long countBySuccess(Boolean success);

    /**
     * Count activities within a date range
     */
    @Query("SELECT COUNT(ua) FROM UserActivity ua WHERE ua.activityTimestamp BETWEEN :startDate AND :endDate")
    long countActivitiesBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);

    /**
     * Find most frequent activity types
     */
    @Query("SELECT ua.activityType, COUNT(ua) FROM UserActivity ua GROUP BY ua.activityType ORDER BY COUNT(ua) DESC")
    List<Object[]> findMostFrequentActivityTypes();

    /**
     * Find activities by user and success status
     */
    List<UserActivity> findByUserAndSuccessOrderByActivityTimestampDesc(User user, Boolean success);

    /**
     * Find activities by user and success status with pagination
     */
    Page<UserActivity> findByUserAndSuccessOrderByActivityTimestampDesc(User user, Boolean success, Pageable pageable);

    /**
     * Find activities containing specific text in description
     */
    @Query("SELECT ua FROM UserActivity ua WHERE LOWER(ua.activityDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY ua.activityTimestamp DESC")
    List<UserActivity> findActivitiesByDescriptionContaining(@Param("searchTerm") String searchTerm);

    /**
     * Find activities containing specific text in description with pagination
     */
    @Query("SELECT ua FROM UserActivity ua WHERE LOWER(ua.activityDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY ua.activityTimestamp DESC")
    Page<UserActivity> findActivitiesByDescriptionContaining(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Delete old activities (for cleanup)
     */
    @Query("DELETE FROM UserActivity ua WHERE ua.activityTimestamp < :cutoffDate")
    void deleteOldActivities(@Param("cutoffDate") LocalDateTime cutoffDate);
}

