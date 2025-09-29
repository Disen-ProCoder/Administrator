package org.example.administrator.service;

import org.example.administrator.dto.UserCreateDTO;
import org.example.administrator.entity.User;
import org.example.administrator.enums.UserRole;
import org.example.administrator.enums.UserStatus;
import org.example.administrator.exception.UserAlreadyExistsException;
import org.example.administrator.exception.UserNotFoundException;
import org.example.administrator.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserActivityService userActivityService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserCreateDTO userCreateDTO;
    private User user;

    @BeforeEach
    void setUp() {
        userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername("testuser");
        userCreateDTO.setEmail("test@example.com");
        userCreateDTO.setPassword("password123");
        userCreateDTO.setFirstName("Test");
        userCreateDTO.setLastName("User");
        userCreateDTO.setUserRole(UserRole.POLICY_OFFICER);
        userCreateDTO.setUserStatus(UserStatus.PENDING);
        userCreateDTO.setCreatedBy("admin");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setUserRole(UserRole.POLICY_OFFICER);
        user.setUserStatus(UserStatus.PENDING);
        user.setCreatedBy("admin");
    }

    @Test
    void createUser_Success() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        var result = userService.createUser(userCreateDTO);

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Test User", result.getFullName());
        assertEquals(UserRole.POLICY_OFFICER, result.getUserRole());
        assertEquals(UserStatus.PENDING, result.getUserStatus());

        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByEmail("test@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
        verify(userActivityService).logActivity(any(User.class), eq("USER_CREATED"), anyString(), eq(true));
    }

    @Test
    void createUser_UsernameAlreadyExists_ThrowsException() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userCreateDTO));
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_EmailAlreadyExists_ThrowsException() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userCreateDTO));
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        var result = userService.getUserById(1L);

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_UserNotFound_ThrowsException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserByUsername_Success() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // When
        var result = userService.getUserByUsername("testuser");

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void getUserByUsername_UserNotFound_ThrowsException() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("testuser"));
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void blockUser_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        var result = userService.blockUser(1L, "admin");

        // Then
        assertNotNull(result);
        assertEquals(UserStatus.BLOCKED, result.getUserStatus());
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
        verify(userActivityService).logActivity(any(User.class), eq("USER_BLOCKED"), anyString(), eq(true));
    }

    @Test
    void blockUser_UserNotFound_ThrowsException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.blockUser(1L, "admin"));
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void unblockUser_Success() {
        // Given
        user.setUserStatus(UserStatus.BLOCKED);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        var result = userService.unblockUser(1L, "admin");

        // Then
        assertNotNull(result);
        assertEquals(UserStatus.ACTIVE, result.getUserStatus());
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
        verify(userActivityService).logActivity(any(User.class), eq("USER_UNBLOCKED"), anyString(), eq(true));
    }

    @Test
    void deleteUser_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        userService.deleteUser(1L, "admin");

        // Then
        assertTrue(user.getIsDeleted());
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
        verify(userActivityService).logActivity(any(User.class), eq("USER_DELETED"), anyString(), eq(true));
    }

    @Test
    void deleteUser_UserNotFound_ThrowsException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L, "admin"));
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserCount_Success() {
        // Given
        when(userRepository.count()).thenReturn(10L);

        // When
        long count = userService.getUserCount();

        // Then
        assertEquals(10L, count);
        verify(userRepository).count();
    }

    @Test
    void getUserCountByRole_Success() {
        // Given
        when(userRepository.countByUserRoleAndIsDeletedFalse(UserRole.POLICY_OFFICER)).thenReturn(5L);

        // When
        long count = userService.getUserCountByRole(UserRole.POLICY_OFFICER);

        // Then
        assertEquals(5L, count);
        verify(userRepository).countByUserRoleAndIsDeletedFalse(UserRole.POLICY_OFFICER);
    }

    @Test
    void getUserCountByStatus_Success() {
        // Given
        when(userRepository.countByUserStatusAndIsDeletedFalse(UserStatus.ACTIVE)).thenReturn(8L);

        // When
        long count = userService.getUserCountByStatus(UserStatus.ACTIVE);

        // Then
        assertEquals(8L, count);
        verify(userRepository).countByUserStatusAndIsDeletedFalse(UserStatus.ACTIVE);
    }
}

