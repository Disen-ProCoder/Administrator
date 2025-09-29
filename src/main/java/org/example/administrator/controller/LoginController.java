package org.example.administrator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Login Controller for authentication pages
 */
@Controller
@Slf4j
public class LoginController {

    /**
     * Login page
     */
    @GetMapping("/login")
    public String login(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            Model model) {
        
        log.info("Loading login page");
        
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password. Please try again.");
        }
        
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }
        
        return "login";
    }

    /**
     * Access denied page
     */
    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        log.warn("Access denied page accessed");
        model.addAttribute("errorMessage", "Access Denied. You don't have permission to access this resource.");
        return "error/access-denied";
    }
}
