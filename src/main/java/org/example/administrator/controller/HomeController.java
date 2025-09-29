package org.example.administrator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home Controller for root path handling
 */
@Controller
@Slf4j
public class HomeController {

    /**
     * Root path - redirect to dashboard if authenticated, otherwise to login
     */
    @GetMapping("/")
    public String home(Authentication authentication) {
        log.info("Root path accessed");
        
        if (authentication != null && authentication.isAuthenticated()) {
            log.info("User is authenticated, redirecting to dashboard");
            return "redirect:/admin/dashboard";
        } else {
            log.info("User is not authenticated, redirecting to login");
            return "redirect:/login";
        }
    }
}
