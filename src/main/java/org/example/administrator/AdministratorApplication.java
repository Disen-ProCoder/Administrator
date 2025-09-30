package org.example.administrator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * Main Spring Boot Application for Vehicle Insurance Administrator Module
 */
@SpringBootApplication
@Slf4j
public class AdministratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdministratorApplication.class, args);
    }

    /**
     * Application ready event listener
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(ApplicationReadyEvent event) {
        Environment environment = event.getApplicationContext().getEnvironment();
        log.info(" Vehicle Insurance Administrator Application Started Successfully!");
        log.info(" Application Name: {}", environment.getProperty("spring.application.name"));
        log.info(" Server Port: {}", environment.getProperty("server.port"));
        log.info(" Database URL: {}", environment.getProperty("spring.datasource.url"));
        log.info(" Default Admin User: {}", environment.getProperty("spring.security.user.name"));
        log.info(" Access URLs:");
        log.info("   - Login: http://localhost:8080/login");
        log.info("   - Dashboard: http://localhost:8080/admin/dashboard");
        log.info("   - Health Check: http://localhost:8080/actuator/health");

        // Log active profiles
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length == 0) {
            log.info(" Active Profile: default");
        } else {
            log.info(" Active Profiles: {}", Arrays.toString(activeProfiles));
        }
    }
}
