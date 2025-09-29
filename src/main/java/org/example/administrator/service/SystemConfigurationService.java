package org.example.administrator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.administrator.dto.SystemConfigurationDTO;
import org.example.administrator.entity.SystemConfiguration;
import org.example.administrator.exception.ConfigurationNotFoundException;
import org.example.administrator.repository.SystemConfigurationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for system configuration management
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SystemConfigurationService {

    private final SystemConfigurationRepository configurationRepository;

    /**
     * Get configuration by key
     */
    @Transactional(readOnly = true)
    public SystemConfigurationDTO getConfiguration(String configKey) {
        SystemConfiguration config = configurationRepository.findByConfigKey(configKey)
            .orElseThrow(() -> new ConfigurationNotFoundException(configKey));
        return convertToDTO(config);
    }

    /**
     * Get all configurations
     */
    @Transactional(readOnly = true)
    public List<SystemConfigurationDTO> getAllConfigurations() {
        return configurationRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get configurations by type
     */
    @Transactional(readOnly = true)
    public List<SystemConfigurationDTO> getConfigurationsByType(SystemConfiguration.ConfigurationType configType) {
        return configurationRepository.findByConfigTypeOrderByConfigKey(configType)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Create or update configuration
     */
    public SystemConfigurationDTO saveConfiguration(SystemConfigurationDTO configDTO, String updatedBy) {
        log.info("Saving configuration: {}", configDTO.getConfigKey());

        SystemConfiguration config;
        boolean isNew = configDTO.getId() == null;

        if (isNew) {
            // Check if configuration already exists
            if (configurationRepository.existsByConfigKey(configDTO.getConfigKey())) {
                throw new IllegalArgumentException("Configuration with key '" + configDTO.getConfigKey() + "' already exists");
            }
            config = new SystemConfiguration();
        } else {
            config = configurationRepository.findById(configDTO.getId())
                .orElseThrow(() -> new ConfigurationNotFoundException(configDTO.getId().toString()));
        }

        // Update configuration fields
        config.setConfigKey(configDTO.getConfigKey());
        config.setConfigValue(configDTO.getConfigValue());
        config.setConfigDescription(configDTO.getConfigDescription());
        config.setConfigType(configDTO.getConfigType());
        config.setIsEncrypted(configDTO.getIsEncrypted());
        config.setIsReadOnly(configDTO.getIsReadOnly());

        if (isNew) {
            config.setCreatedBy(updatedBy);
        } else {
            config.setUpdatedBy(updatedBy);
        }

        SystemConfiguration savedConfig = configurationRepository.save(config);

        log.info("Configuration saved successfully: {}", savedConfig.getConfigKey());
        return convertToDTO(savedConfig);
    }

    /**
     * Update configuration value
     */
    public SystemConfigurationDTO updateConfigurationValue(String configKey, String configValue, String updatedBy) {
        log.info("Updating configuration value: {}", configKey);

        SystemConfiguration config = configurationRepository.findByConfigKey(configKey)
            .orElseThrow(() -> new ConfigurationNotFoundException(configKey));

        if (config.getIsReadOnly()) {
            throw new IllegalArgumentException("Cannot update read-only configuration: " + configKey);
        }

        config.setConfigValue(configValue);
        config.setUpdatedBy(updatedBy);

        SystemConfiguration savedConfig = configurationRepository.save(config);

        log.info("Configuration value updated successfully: {}", savedConfig.getConfigKey());
        return convertToDTO(savedConfig);
    }

    /**
     * Delete configuration
     */
    public void deleteConfiguration(String configKey, String deletedBy) {
        log.info("Deleting configuration: {}", configKey);

        SystemConfiguration config = configurationRepository.findByConfigKey(configKey)
            .orElseThrow(() -> new ConfigurationNotFoundException(configKey));

        if (config.getIsReadOnly()) {
            throw new IllegalArgumentException("Cannot delete read-only configuration: " + configKey);
        }

        configurationRepository.delete(config);

        log.info("Configuration deleted successfully: {}", configKey);
    }

    /**
     * Get system critical configurations
     */
    @Transactional(readOnly = true)
    public List<SystemConfigurationDTO> getSystemCriticalConfigurations() {
        return configurationRepository.findSystemCriticalConfigurations()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get email configurations
     */
    @Transactional(readOnly = true)
    public List<SystemConfigurationDTO> getEmailConfigurations() {
        return configurationRepository.findEmailConfigurations()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get security configurations
     */
    @Transactional(readOnly = true)
    public List<SystemConfigurationDTO> getSecurityConfigurations() {
        return configurationRepository.findSecurityConfigurations()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get database configurations
     */
    @Transactional(readOnly = true)
    public List<SystemConfigurationDTO> getDatabaseConfigurations() {
        return configurationRepository.findDatabaseConfigurations()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Search configurations by key pattern
     */
    @Transactional(readOnly = true)
    public List<SystemConfigurationDTO> searchConfigurationsByKey(String pattern) {
        return configurationRepository.findByConfigKeyLike(pattern)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Search configurations by description
     */
    @Transactional(readOnly = true)
    public List<SystemConfigurationDTO> searchConfigurationsByDescription(String searchTerm) {
        return configurationRepository.findByDescriptionContaining(searchTerm)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get configurations that need encryption
     */
    @Transactional(readOnly = true)
    public List<SystemConfigurationDTO> getConfigurationsNeedingEncryption() {
        return configurationRepository.findConfigurationsNeedingEncryption()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get configuration statistics
     */
    @Transactional(readOnly = true)
    public long getTotalConfigurationCount() {
        return configurationRepository.count();
    }

    @Transactional(readOnly = true)
    public long getConfigurationCountByType(SystemConfiguration.ConfigurationType configType) {
        return configurationRepository.countByConfigType(configType);
    }

    @Transactional(readOnly = true)
    public long getReadOnlyConfigurationCount() {
        return configurationRepository.findByIsReadOnlyTrue().size();
    }

    @Transactional(readOnly = true)
    public long getEncryptedConfigurationCount() {
        return configurationRepository.findByIsEncryptedTrue().size();
    }

    /**
     * Check if configuration exists
     */
    @Transactional(readOnly = true)
    public boolean configurationExists(String configKey) {
        return configurationRepository.existsByConfigKey(configKey);
    }

    /**
     * Get configuration value as string
     */
    @Transactional(readOnly = true)
    public String getConfigurationValue(String configKey) {
        SystemConfiguration config = configurationRepository.findByConfigKey(configKey)
            .orElseThrow(() -> new ConfigurationNotFoundException(configKey));
        return config.getConfigValue();
    }

    /**
     * Get configuration value as string with default
     */
    @Transactional(readOnly = true)
    public String getConfigurationValue(String configKey, String defaultValue) {
        try {
            return getConfigurationValue(configKey);
        } catch (ConfigurationNotFoundException e) {
            return defaultValue;
        }
    }

    /**
     * Get configuration value as boolean
     */
    @Transactional(readOnly = true)
    public boolean getConfigurationValueAsBoolean(String configKey) {
        String value = getConfigurationValue(configKey);
        return Boolean.parseBoolean(value);
    }

    /**
     * Get configuration value as boolean with default
     */
    @Transactional(readOnly = true)
    public boolean getConfigurationValueAsBoolean(String configKey, boolean defaultValue) {
        try {
            return getConfigurationValueAsBoolean(configKey);
        } catch (ConfigurationNotFoundException e) {
            return defaultValue;
        }
    }

    /**
     * Get configuration value as integer
     */
    @Transactional(readOnly = true)
    public int getConfigurationValueAsInteger(String configKey) {
        String value = getConfigurationValue(configKey);
        return Integer.parseInt(value);
    }

    /**
     * Get configuration value as integer with default
     */
    @Transactional(readOnly = true)
    public int getConfigurationValueAsInteger(String configKey, int defaultValue) {
        try {
            return getConfigurationValueAsInteger(configKey);
        } catch (ConfigurationNotFoundException e) {
            return defaultValue;
        }
    }

    /**
     * Convert SystemConfiguration entity to SystemConfigurationDTO
     */
    private SystemConfigurationDTO convertToDTO(SystemConfiguration config) {
        SystemConfigurationDTO dto = new SystemConfigurationDTO();
        dto.setId(config.getId());
        dto.setConfigKey(config.getConfigKey());
        dto.setConfigValue(config.getConfigValue());
        dto.setConfigDescription(config.getConfigDescription());
        dto.setConfigType(config.getConfigType());
        dto.setIsEncrypted(config.getIsEncrypted());
        dto.setIsReadOnly(config.getIsReadOnly());
        dto.setCreatedAt(config.getCreatedAt());
        dto.setUpdatedAt(config.getUpdatedAt());
        dto.setCreatedBy(config.getCreatedBy());
        dto.setUpdatedBy(config.getUpdatedBy());
        return dto;
    }
}

