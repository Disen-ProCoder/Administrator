package org.example.administrator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.administrator.dto.UserCreateDTO;
import org.example.administrator.dto.UserResponseDTO;
import org.example.administrator.dto.UserUpdateDTO;
import org.example.administrator.entity.User;
import org.example.administrator.entity.UserActivity;
import org.example.administrator.enums.UserRole;
import org.example.administrator.enums.UserStatus;
import org.example.administrator.exception.*;
import org.example.administrator.repository.UserRepository;
import org.example.administrator.repository.UserActivityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for user management operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserActivityRepository userActivityRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserActivityService userActivityService;

    /**
     * Create a new user
     */
    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {
        log.info("Creating new user: {}", userCreateDTO.getUsername());

        // Check if username already exists
        if (userRepository.existsByUsername(userCreateDTO.getUsername())) {
            throw new UserAlreadyExistsException("username", userCreateDTO.getUsername());
        }

        // Check if email already exists
        if (userRepository.existsByEmail(userCreateDTO.getEmail())) {
            throw new UserAlreadyExistsException("email", userCreateDTO.getEmail());
        }

        // Create user entity
        User user = new User();
        user.setUsername(userCreateDTO.getUsername());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        user.setFirstName(userCreateDTO.getFirstName());
        user.setLastName(userCreateDTO.getLastName());
        user.setPhoneNumber(userCreateDTO.getPhoneNumber());
        user.setUserRole(userCreateDTO.getUserRole());
        user.setUserStatus(userCreateDTO.getUserStatus());
        user.setCreatedBy(userCreateDTO.getCreatedBy());

        // Save user
        User savedUser = userRepository.save(user);

        // Log activity
        userActivityService.logActivity(savedUser, "USER_CREATED", 
            "User account created successfully", true);

        log.info("User created successfully: {}", savedUser.getUsername());
        return convertToResponseDTO(savedUser);
    }

    /**
     * Get user by ID
     */
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
        return convertToResponseDTO(user);
    }

    /**
     * Get user by username
     */
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("username", username));
        return convertToResponseDTO(user);
    }

    /**
     * Get all users with pagination
     */
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findByIsDeletedFalse(pageable)
            .map(this::convertToResponseDTO);
    }

    /**
     * Search users by name
     */
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> searchUsers(String searchTerm, Pageable pageable) {
        return userRepository.searchUsersByName(searchTerm, pageable)
            .map(this::convertToResponseDTO);
    }

    /**
     * Get users by role
     */
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByRole(UserRole userRole) {
        return userRepository.findByUserRole(userRole)
            .stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get users by status
     */
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByStatus(UserStatus userStatus) {
        return userRepository.findByUserStatus(userStatus)
            .stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Update user
     */
    public UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        log.info("Updating user: {}", id);

        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));

        // Check if email already exists for another user
        if (userRepository.existsByEmailAndIdNot(userUpdateDTO.getEmail(), id)) {
            throw new UserAlreadyExistsException("email", userUpdateDTO.getEmail());
        }

        // Update user fields
        user.setEmail(userUpdateDTO.getEmail());
        user.setFirstName(userUpdateDTO.getFirstName());
        user.setLastName(userUpdateDTO.getLastName());
        user.setPhoneNumber(userUpdateDTO.getPhoneNumber());
        
        if (userUpdateDTO.getUserRole() != null) {
            user.setUserRole(userUpdateDTO.getUserRole());
        }
        
        if (userUpdateDTO.getUserStatus() != null) {
            user.setUserStatus(userUpdateDTO.getUserStatus());
        }
        
        user.setUpdatedBy(userUpdateDTO.getUpdatedBy());

        User savedUser = userRepository.save(user);

        // Log activity
        userActivityService.logActivity(savedUser, "USER_UPDATED", 
            "User account updated successfully", true);

        log.info("User updated successfully: {}", savedUser.getUsername());
        return convertToResponseDTO(savedUser);
    }

    /**
     * Block user
     */
    public UserResponseDTO blockUser(Long id, String blockedBy) {
        log.info("Blocking user: {}", id);

        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));

        user.setUserStatus(UserStatus.BLOCKED);
        user.setUpdatedBy(blockedBy);

        User savedUser = userRepository.save(user);

        // Log activity
        userActivityService.logActivity(savedUser, "USER_BLOCKED", 
            "User account blocked by administrator", true);

        log.info("User blocked successfully: {}", savedUser.getUsername());
        return convertToResponseDTO(savedUser);
    }

    /**
     * Unblock user
     */
    public UserResponseDTO unblockUser(Long id, String unblockedBy) {
        log.info("Unblocking user: {}", id);

        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));

        user.setUserStatus(UserStatus.ACTIVE);
        user.setUpdatedBy(unblockedBy);

        User savedUser = userRepository.save(user);

        // Log activity
        userActivityService.logActivity(savedUser, "USER_UNBLOCKED", 
            "User account unblocked by administrator", true);

        log.info("User unblocked successfully: {}", savedUser.getUsername());
        return convertToResponseDTO(savedUser);
    }

    /**
     * Delete user (soft delete)
     */
    public void deleteUser(Long id, String deletedBy) {
        log.info("Deleting user: {}", id);

        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));

        user.setIsDeleted(true);
        user.setUpdatedBy(deletedBy);

        userRepository.save(user);

        // Log activity
        userActivityService.logActivity(user, "USER_DELETED", 
            "User account deleted by administrator", true);

        log.info("User deleted successfully: {}", user.getUsername());
    }

    /**
     * Reset user password
     */
    public void resetPassword(Long id, String newPassword, String resetBy) {
        log.info("Resetting password for user: {}", id);

        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedBy(resetBy);

        userRepository.save(user);

        // Log activity
        userActivityService.logActivity(user, "PASSWORD_RESET", 
            "User password reset by administrator", true);

        log.info("Password reset successfully for user: {}", user.getUsername());
    }

    /**
     * Lock user account
     */
    public UserResponseDTO lockUserAccount(Long id, int minutes, String lockedBy) {
        log.info("Locking user account: {} for {} minutes", id, minutes);

        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));

        user.lockAccount(minutes);
        user.setUpdatedBy(lockedBy);

        User savedUser = userRepository.save(user);

        // Log activity
        userActivityService.logActivity(savedUser, "ACCOUNT_LOCKED", 
            "User account locked for " + minutes + " minutes", true);

        log.info("User account locked successfully: {}", savedUser.getUsername());
        return convertToResponseDTO(savedUser);
    }

    /**
     * Unlock user account
     */
    public UserResponseDTO unlockUserAccount(Long id, String unlockedBy) {
        log.info("Unlocking user account: {}", id);

        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));

        user.resetLoginAttempts();
        user.setUpdatedBy(unlockedBy);

        User savedUser = userRepository.save(user);

        // Log activity
        userActivityService.logActivity(savedUser, "ACCOUNT_UNLOCKED", 
            "User account unlocked by administrator", true);

        log.info("User account unlocked successfully: {}", savedUser.getUsername());
        return convertToResponseDTO(savedUser);
    }

    /**
     * Get user statistics
     */
    @Transactional(readOnly = true)
    public long getUserCount() {
        return userRepository.count();
    }

    @Transactional(readOnly = true)
    public long getUserCountByRole(UserRole userRole) {
        return userRepository.countByUserRoleAndIsDeletedFalse(userRole);
    }

    @Transactional(readOnly = true)
    public long getUserCountByStatus(UserStatus userStatus) {
        return userRepository.countByUserStatusAndIsDeletedFalse(userStatus);
    }

    /**
     * Convert User entity to UserResponseDTO
     */
    private UserResponseDTO convertToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setFullName(user.getFullName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setUserRole(user.getUserRole());
        dto.setUserStatus(user.getUserStatus());
        dto.setLastLogin(user.getLastLogin());
        dto.setLoginAttempts(user.getLoginAttempts());
        dto.setAccountLockedUntil(user.getAccountLockedUntil());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setCreatedBy(user.getCreatedBy());
        dto.setUpdatedBy(user.getUpdatedBy());
        dto.setIsDeleted(user.getIsDeleted());
        return dto;
    }
}

