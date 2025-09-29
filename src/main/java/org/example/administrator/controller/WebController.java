package org.example.administrator.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.administrator.service.AdminService;
import org.example.administrator.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Web Controller for Thymeleaf templates
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class WebController {

    private final AdminService adminService;
    private final UserService userService;

    /**
     * Dashboard page
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public String dashboard(Model model) {
        log.info("Loading dashboard page");
        
        try {
            Map<String, Object> statistics = adminService.getDashboardStatistics();
            model.addAttribute("statistics", statistics);
            model.addAttribute("pageTitle", "Dashboard");
        } catch (Exception e) {
            log.error("Error loading dashboard", e);
            model.addAttribute("errorMessage", "Error loading dashboard data");
        }
        
        return "admin/dashboard";
    }

    /**
     * User management page
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public String users(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String userRole,
            @RequestParam(required = false) String userStatus,
            Model model) {
        
        log.info("Loading users page with page={}, size={}, searchTerm={}, userRole={}, userStatus={}", 
                page, size, searchTerm, userRole, userStatus);
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            
            // For now, we'll use a simple implementation
            // In a real application, you would implement filtering in the service layer
            var users = userService.getAllUsers(pageable);
            
            model.addAttribute("users", users);
            model.addAttribute("pageTitle", "User Management");
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", users.getTotalPages());
            
        } catch (Exception e) {
            log.error("Error loading users page", e);
            model.addAttribute("errorMessage", "Error loading users data");
        }
        
        return "admin/users";
    }

    /**
     * User activities page
     */
    @GetMapping("/activities")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public String activities(Model model) {
        log.info("Loading activities page");
        
        try {
            model.addAttribute("pageTitle", "User Activities");
        } catch (Exception e) {
            log.error("Error loading activities page", e);
            model.addAttribute("errorMessage", "Error loading activities data");
        }
        
        return "admin/activities";
    }

    /**
     * System configuration page
     */
    @GetMapping("/config")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public String config(Model model) {
        log.info("Loading system configuration page");
        
        try {
            model.addAttribute("pageTitle", "System Configuration");
        } catch (Exception e) {
            log.error("Error loading configuration page", e);
            model.addAttribute("errorMessage", "Error loading configuration data");
        }
        
        return "admin/config";
    }

    /**
     * Reports page
     */
    @GetMapping("/reports")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public String reports(Model model) {
        log.info("Loading reports page");
        
        try {
            model.addAttribute("pageTitle", "Reports");
        } catch (Exception e) {
            log.error("Error loading reports page", e);
            model.addAttribute("errorMessage", "Error loading reports data");
        }
        
        return "admin/reports";
    }
}

