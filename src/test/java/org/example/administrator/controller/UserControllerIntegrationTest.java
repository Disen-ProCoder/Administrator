package org.example.administrator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.administrator.dto.UserCreateDTO;
import org.example.administrator.entity.User;
import org.example.administrator.enums.UserRole;
import org.example.administrator.enums.UserStatus;
import org.example.administrator.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for UserController
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setUserRole(UserRole.POLICY_OFFICER);
        testUser.setUserStatus(UserStatus.ACTIVE);
        testUser.setCreatedBy("admin");
        testUser = userRepository.save(testUser);
    }

    @Test
    @WithMockUser(roles = "ADMIN_OFFICER")
    void createUser_Success() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername("newuser");
        userCreateDTO.setEmail("newuser@example.com");
        userCreateDTO.setPassword("password123");
        userCreateDTO.setFirstName("New");
        userCreateDTO.setLastName("User");
        userCreateDTO.setUserRole(UserRole.CLAIMS_OFFICER);
        userCreateDTO.setUserStatus(UserStatus.PENDING);
        userCreateDTO.setCreatedBy("admin");

        mockMvc.perform(post("/api/admin/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.firstName").value("New"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.userRole").value("CLAIMS_OFFICER"))
                .andExpect(jsonPath("$.userStatus").value("PENDING"));
    }

    @Test
    @WithMockUser(roles = "ADMIN_OFFICER")
    void createUser_UsernameAlreadyExists_ReturnsConflict() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername("testuser"); // Already exists
        userCreateDTO.setEmail("different@example.com");
        userCreateDTO.setPassword("password123");
        userCreateDTO.setFirstName("Test");
        userCreateDTO.setLastName("User");
        userCreateDTO.setUserRole(UserRole.CLAIMS_OFFICER);
        userCreateDTO.setUserStatus(UserStatus.PENDING);
        userCreateDTO.setCreatedBy("admin");

        mockMvc.perform(post("/api/admin/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("USER_ALREADY_EXISTS"));
    }

    @Test
    @WithMockUser(roles = "ADMIN_OFFICER")
    void createUser_EmailAlreadyExists_ReturnsConflict() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername("differentuser");
        userCreateDTO.setEmail("test@example.com"); // Already exists
        userCreateDTO.setPassword("password123");
        userCreateDTO.setFirstName("Test");
        userCreateDTO.setLastName("User");
        userCreateDTO.setUserRole(UserRole.CLAIMS_OFFICER);
        userCreateDTO.setUserStatus(UserStatus.PENDING);
        userCreateDTO.setCreatedBy("admin");

        mockMvc.perform(post("/api/admin/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("USER_ALREADY_EXISTS"));
    }

    @Test
    @WithMockUser(roles = "ADMIN_OFFICER")
    void createUser_InvalidData_ReturnsBadRequest() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        // Missing required fields
        userCreateDTO.setUsername("");
        userCreateDTO.setEmail("invalid-email");
        userCreateDTO.setPassword("123"); // Too short
        userCreateDTO.setCreatedBy("admin");

        mockMvc.perform(post("/api/admin/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN_OFFICER")
    void getAllUsers_Success() throws Exception {
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].username").value("testuser"))
                .andExpect(jsonPath("$.content[0].email").value("test@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN_OFFICER")
    void getUserById_Success() throws Exception {
        mockMvc.perform(get("/api/admin/users/{id}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN_OFFICER")
    void getUserById_UserNotFound_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/admin/users/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("USER_NOT_FOUND"));
    }

    @Test
    @WithMockUser(roles = "ADMIN_OFFICER")
    void getUserByUsername_Success() throws Exception {
        mockMvc.perform(get("/api/admin/users/username/{username}", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN_OFFICER")
    void getUserByUsername_UserNotFound_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/admin/users/username/{username}", "nonexistent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("USER_NOT_FOUND"));
    }

    @Test
    @WithMockUser(roles = "ADMIN_OFFICER")
    void getUsersByRole_Success() throws Exception {
        mockMvc.perform(get("/api/admin/users/role/{role}", "POLICY_OFFICER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    @Test
    @WithMockUser(roles = "ADMIN_OFFICER")
    void getUsersByStatus_Success() throws Exception {
        mockMvc.perform(get("/api/admin/users/status/{status}", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    @Test
    @WithMockUser(roles = "ADMIN_OFFICER")
    void blockUser_Success() throws Exception {
        mockMvc.perform(post("/api/admin/users/{id}/block?blockedBy=admin", testUser.getId())
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userStatus").value("BLOCKED"));
    }

    @Test
    @WithMockUser(roles = "ADMIN_OFFICER")
    void blockUser_UserNotFound_ReturnsNotFound() throws Exception {
        mockMvc.perform(post("/api/admin/users/{id}/block?blockedBy=admin", 999L)
                .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("USER_NOT_FOUND"));
    }

    @Test
    @WithMockUser(roles = "ADMIN_OFFICER")
    void unblockUser_Success() throws Exception {
        // First block the user
        testUser.setUserStatus(UserStatus.BLOCKED);
        userRepository.save(testUser);

        mockMvc.perform(post("/api/admin/users/{id}/unblock?unblockedBy=admin", testUser.getId())
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userStatus").value("ACTIVE"));
    }

    @Test
    @WithMockUser(roles = "ADMIN_OFFICER")
    void deleteUser_Success() throws Exception {
        mockMvc.perform(delete("/api/admin/users/{id}?deletedBy=admin", testUser.getId())
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN_OFFICER")
    void deleteUser_UserNotFound_ReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/admin/users/{id}?deletedBy=admin", 999L)
                .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("USER_NOT_FOUND"));
    }

    @Test
    @WithMockUser(roles = "ADMIN_OFFICER")
    void getUserStatistics_Success() throws Exception {
        mockMvc.perform(get("/api/admin/users/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalUsers").exists())
                .andExpect(jsonPath("$.activeUsers").exists())
                .andExpect(jsonPath("$.blockedUsers").exists())
                .andExpect(jsonPath("$.pendingUsers").exists());
    }

    @Test
    @WithMockUser(roles = "USER") // Wrong role
    void createUser_Unauthorized_ReturnsForbidden() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername("newuser");
        userCreateDTO.setEmail("newuser@example.com");
        userCreateDTO.setPassword("password123");
        userCreateDTO.setFirstName("New");
        userCreateDTO.setLastName("User");
        userCreateDTO.setUserRole(UserRole.CLAIMS_OFFICER);
        userCreateDTO.setUserStatus(UserStatus.PENDING);
        userCreateDTO.setCreatedBy("admin");

        mockMvc.perform(post("/api/admin/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isForbidden());
    }
}

