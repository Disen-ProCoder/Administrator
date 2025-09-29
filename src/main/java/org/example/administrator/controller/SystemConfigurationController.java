package org.example.administrator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.administrator.dto.SystemConfigurationDTO;
import org.example.administrator.entity.SystemConfiguration;
import org.example.administrator.service.SystemConfigurationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for system configuration operations
 */
@RestController
@RequestMapping("/api/admin/config")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SystemConfigurationController {

    private final SystemConfigurationService systemConfigurationService;

    /**
     * Get all configurations
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<SystemConfigurationDTO>> getAllConfigurations() {
        log.info("Getting all configurations");
        List<SystemConfigurationDTO> configurations = systemConfigurationService.getAllConfigurations();
        return ResponseEntity.ok(configurations);
    }

    /**
     * Get configuration by key
     */
    @GetMapping("/{configKey}")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<SystemConfigurationDTO> getConfiguration(@PathVariable String configKey) {
        log.info("Getting configuration: {}", configKey);
        SystemConfigurationDTO configuration = systemConfigurationService.getConfiguration(configKey);
        return ResponseEntity.ok(configuration);
    }

    /**
     * Get configurations by type
     */
    @GetMapping("/type/{configType}")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<SystemConfigurationDTO>> getConfigurationsByType(
            @PathVariable SystemConfiguration.ConfigurationType configType) {
        log.info("Getting configurations by type: {}", configType);
        List<SystemConfigurationDTO> configurations = systemConfigurationService.getConfigurationsByType(configType);
        return ResponseEntity.ok(configurations);
    }

    /**
     * Create or update configuration
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<SystemConfigurationDTO> saveConfiguration(
            @Valid @RequestBody SystemConfigurationDTO configDTO,
            @RequestParam String updatedBy) {
        log.info("Saving configuration: {}", configDTO.getConfigKey());
        SystemConfigurationDTO configuration = systemConfigurationService.saveConfiguration(configDTO, updatedBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(configuration);
    }

    /**
     * Update configuration value
     */
    @PutMapping("/{configKey}/value")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<SystemConfigurationDTO> updateConfigurationValue(
            @PathVariable String configKey,
            @RequestParam String configValue,
            @RequestParam String updatedBy) {
        log.info("Updating configuration value: {}", configKey);
        SystemConfigurationDTO configuration = systemConfigurationService.updateConfigurationValue(
            configKey, configValue, updatedBy);
        return ResponseEntity.ok(configuration);
    }

    /**
     * Delete configuration
     */
    @DeleteMapping("/{configKey}")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<Void> deleteConfiguration(
            @PathVariable String configKey,
            @RequestParam String deletedBy) {
        log.info("Deleting configuration: {}", configKey);
        systemConfigurationService.deleteConfiguration(configKey, deletedBy);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get system critical configurations
     */
    @GetMapping("/system/critical")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<SystemConfigurationDTO>> getSystemCriticalConfigurations() {
        log.info("Getting system critical configurations");
        List<SystemConfigurationDTO> configurations = systemConfigurationService.getSystemCriticalConfigurations();
        return ResponseEntity.ok(configurations);
    }

    /**
     * Get email configurations
     */
    @GetMapping("/email")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<SystemConfigurationDTO>> getEmailConfigurations() {
        log.info("Getting email configurations");
        List<SystemConfigurationDTO> configurations = systemConfigurationService.getEmailConfigurations();
        return ResponseEntity.ok(configurations);
    }

    /**
     * Get security configurations
     */
    @GetMapping("/security")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<SystemConfigurationDTO>> getSecurityConfigurations() {
        log.info("Getting security configurations");
        List<SystemConfigurationDTO> configurations = systemConfigurationService.getSecurityConfigurations();
        return ResponseEntity.ok(configurations);
    }

    /**
     * Get database configurations
     */
    @GetMapping("/database")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<SystemConfigurationDTO>> getDatabaseConfigurations() {
        log.info("Getting database configurations");
        List<SystemConfigurationDTO> configurations = systemConfigurationService.getDatabaseConfigurations();
        return ResponseEntity.ok(configurations);
    }

    /**
     * Search configurations by key pattern
     */
    @GetMapping("/search/key")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<SystemConfigurationDTO>> searchConfigurationsByKey(
            @RequestParam String pattern) {
        log.info("Searching configurations by key pattern: {}", pattern);
        List<SystemConfigurationDTO> configurations = systemConfigurationService.searchConfigurationsByKey(pattern);
        return ResponseEntity.ok(configurations);
    }

    /**
     * Search configurations by description
     */
    @GetMapping("/search/description")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<SystemConfigurationDTO>> searchConfigurationsByDescription(
            @RequestParam String searchTerm) {
        log.info("Searching configurations by description: {}", searchTerm);
        List<SystemConfigurationDTO> configurations = systemConfigurationService.searchConfigurationsByDescription(searchTerm);
        return ResponseEntity.ok(configurations);
    }

    /**
     * Get configurations that need encryption
     */
    @GetMapping("/needing-encryption")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<List<SystemConfigurationDTO>> getConfigurationsNeedingEncryption() {
        log.info("Getting configurations that need encryption");
        List<SystemConfigurationDTO> configurations = systemConfigurationService.getConfigurationsNeedingEncryption();
        return ResponseEntity.ok(configurations);
    }

    /**
     * Get configuration statistics
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<Object> getConfigurationStatistics() {
        log.info("Getting configuration statistics");
        
        // Create a response object with configuration statistics
        Object statistics = new Object() {
            public final long totalConfigurations = systemConfigurationService.getTotalConfigurationCount();
            public final long readOnlyConfigurations = systemConfigurationService.getReadOnlyConfigurationCount();
            public final long encryptedConfigurations = systemConfigurationService.getEncryptedConfigurationCount();
        };
        
        return ResponseEntity.ok(statistics);
    }

    /**
     * Check if configuration exists
     */
    @GetMapping("/{configKey}/exists")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<Boolean> configurationExists(@PathVariable String configKey) {
        log.info("Checking if configuration exists: {}", configKey);
        boolean exists = systemConfigurationService.configurationExists(configKey);
        return ResponseEntity.ok(exists);
    }

    /**
     * Get configuration value as string
     */
    @GetMapping("/{configKey}/value")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<String> getConfigurationValue(@PathVariable String configKey) {
        log.info("Getting configuration value: {}", configKey);
        String value = systemConfigurationService.getConfigurationValue(configKey);
        return ResponseEntity.ok(value);
    }

    /**
     * Get configuration value as boolean
     */
    @GetMapping("/{configKey}/boolean")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<Boolean> getConfigurationValueAsBoolean(@PathVariable String configKey) {
        log.info("Getting configuration value as boolean: {}", configKey);
        boolean value = systemConfigurationService.getConfigurationValueAsBoolean(configKey);
        return ResponseEntity.ok(value);
    }

    /**
     * Get configuration value as integer
     */
    @GetMapping("/{configKey}/integer")
    @PreAuthorize("hasRole('ADMIN_OFFICER')")
    public ResponseEntity<Integer> getConfigurationValueAsInteger(@PathVariable String configKey) {
        log.info("Getting configuration value as integer: {}", configKey);
        int value = systemConfigurationService.getConfigurationValueAsInteger(configKey);
        return ResponseEntity.ok(value);
    }
}

