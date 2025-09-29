package org.example.administrator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.administrator.dto.UserCreateDTO;
import org.example.administrator.dto.UserResponseDTO;
import org.example.administrator.dto.UserUpdateDTO;
import org.example.administrator.enums.UserRole;
import org.example.administrator.enums.UserStatus;
import org.example.administrator.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for user management operations
 */
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    /**
     * Create new user
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        log.info("Creating new user: {}", userCreateDTO.getUsername());
        UserResponseDTO user = userService.createUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Get all users with pagination
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(Pageable pageable) {
        log.info("Getting all users with pagination");
        Page<UserResponseDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    /**
     * Get user by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        log.info("Getting user by ID: {}", id);
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Get user by username
     */
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        log.info("Getting user by username: {}", username);
        UserResponseDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    /**
     * Search users by name
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<Page<UserResponseDTO>> searchUsers(
            @RequestParam String searchTerm, Pageable pageable) {
        log.info("Searching users with term: {}", searchTerm);
        Page<UserResponseDTO> users = userService.searchUsers(searchTerm, pageable);
        return ResponseEntity.ok(users);
    }

    /**
     * Get users by role
     */
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(@PathVariable UserRole role) {
        log.info("Getting users by role: {}", role);
        List<UserResponseDTO> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    /**
     * Get users by status
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<UserResponseDTO>> getUsersByStatus(@PathVariable UserStatus status) {
        log.info("Getting users by status: {}", status);
        List<UserResponseDTO> users = userService.getUsersByStatus(status);
        return ResponseEntity.ok(users);
    }

    /**
     * Update user
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("Updating user: {}", id);
        UserResponseDTO user = userService.updateUser(id, userUpdateDTO);
        return ResponseEntity.ok(user);
    }

    /**
     * Block user
     */
    @PostMapping("/{id}/block")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<UserResponseDTO> blockUser(
            @PathVariable Long id, @RequestParam String blockedBy) {
        log.info("Blocking user: {}", id);
        UserResponseDTO user = userService.blockUser(id, blockedBy);
        return ResponseEntity.ok(user);
    }

    /**
     * Unblock user
     */
    @PostMapping("/{id}/unblock")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<UserResponseDTO> unblockUser(
            @PathVariable Long id, @RequestParam String unblockedBy) {
        log.info("Unblocking user: {}", id);
        UserResponseDTO user = userService.unblockUser(id, unblockedBy);
        return ResponseEntity.ok(user);
    }

    /**
     * Delete user (soft delete)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id, @RequestParam String deletedBy) {
        log.info("Deleting user: {}", id);
        userService.deleteUser(id, deletedBy);
        return ResponseEntity.noContent().build();
    }

    /**
     * Reset user password
     */
    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<String> resetPassword(
            @PathVariable Long id, 
            @RequestParam String newPassword, 
            @RequestParam String resetBy) {
        log.info("Resetting password for user: {}", id);
        userService.resetPassword(id, newPassword, resetBy);
        return ResponseEntity.ok("Password reset successfully");
    }

    /**
     * Lock user account
     */
    @PostMapping("/{id}/lock")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<UserResponseDTO> lockUserAccount(
            @PathVariable Long id, 
            @RequestParam int minutes, 
            @RequestParam String lockedBy) {
        log.info("Locking user account: {} for {} minutes", id, minutes);
        UserResponseDTO user = userService.lockUserAccount(id, minutes, lockedBy);
        return ResponseEntity.ok(user);
    }

    /**
     * Unlock user account
     */
    @PostMapping("/{id}/unlock")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<UserResponseDTO> unlockUserAccount(
            @PathVariable Long id, @RequestParam String unlockedBy) {
        log.info("Unlocking user account: {}", id);
        UserResponseDTO user = userService.unlockUserAccount(id, unlockedBy);
        return ResponseEntity.ok(user);
    }

    /**
     * Get user statistics
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<Object> getUserStatistics() {
        log.info("Getting user statistics");
        
        // Create a response object with user statistics
        Object statistics = new Object() {
            public final long totalUsers = userService.getUserCount();
            public final long activeUsers = userService.getUserCountByStatus(UserStatus.ACTIVE);
            public final long blockedUsers = userService.getUserCountByStatus(UserStatus.BLOCKED);
            public final long pendingUsers = userService.getUserCountByStatus(UserStatus.PENDING);
        };
        
        return ResponseEntity.ok(statistics);
    }
}

