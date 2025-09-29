package org.example.administrator.repository;

import org.example.administrator.entity.User;
import org.example.administrator.enums.UserRole;
import org.example.administrator.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity with custom queries
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by username or email
     */
    @Query("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :identifier")
    Optional<User> findByUsernameOrEmail(@Param("identifier") String identifier);

    /**
     * Find users by role
     */
    List<User> findByUserRole(UserRole userRole);

    /**
     * Find users by status
     */
    List<User> findByUserStatus(UserStatus userStatus);

    /**
     * Find users by role and status
     */
    List<User> findByUserRoleAndUserStatus(UserRole userRole, UserStatus userStatus);

    /**
     * Find active users
     */
    @Query("SELECT u FROM User u WHERE u.userStatus = 'ACTIVE' AND u.isDeleted = false")
    List<User> findActiveUsers();

    /**
     * Find users with pagination
     */
    Page<User> findByIsDeletedFalse(Pageable pageable);

    /**
     * Find users by role with pagination
     */
    Page<User> findByUserRoleAndIsDeletedFalse(UserRole userRole, Pageable pageable);

    /**
     * Find users by status with pagination
     */
    Page<User> findByUserStatusAndIsDeletedFalse(UserStatus userStatus, Pageable pageable);

    /**
     * Search users by name
     */
    @Query("SELECT u FROM User u WHERE " +
           "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND u.isDeleted = false")
    Page<User> searchUsersByName(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find users created by a specific admin
     */
    List<User> findByCreatedBy(String createdBy);

    /**
     * Find users created within a date range
     */
    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate AND u.isDeleted = false")
    List<User> findUsersCreatedBetween(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);

    /**
     * Count users by role
     */
    long countByUserRoleAndIsDeletedFalse(UserRole userRole);

    /**
     * Count users by status
     */
    long countByUserStatusAndIsDeletedFalse(UserStatus userStatus);

    /**
     * Find users with failed login attempts
     */
    @Query("SELECT u FROM User u WHERE u.loginAttempts > 0 AND u.isDeleted = false")
    List<User> findUsersWithFailedLoginAttempts();

    /**
     * Find locked users
     */
    @Query("SELECT u FROM User u WHERE u.accountLockedUntil > :currentTime AND u.isDeleted = false")
    List<User> findLockedUsers(@Param("currentTime") LocalDateTime currentTime);

    /**
     * Find users who haven't logged in recently
     */
    @Query("SELECT u FROM User u WHERE u.lastLogin < :cutoffDate OR u.lastLogin IS NULL AND u.isDeleted = false")
    List<User> findInactiveUsers(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Check if username exists excluding a specific user
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username AND u.id != :excludeId")
    boolean existsByUsernameAndIdNot(@Param("username") String username, @Param("excludeId") Long excludeId);

    /**
     * Check if email exists excluding a specific user
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.id != :excludeId")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("excludeId") Long excludeId);
}

