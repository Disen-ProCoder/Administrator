package org.example.administrator.repository;

import org.example.administrator.entity.SystemConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for SystemConfiguration entity with custom queries
 */
@Repository
public interface SystemConfigurationRepository extends JpaRepository<SystemConfiguration, Long> {

    /**
     * Find configuration by key
     */
    Optional<SystemConfiguration> findByConfigKey(String configKey);

    /**
     * Find configurations by type
     */
    List<SystemConfiguration> findByConfigType(SystemConfiguration.ConfigurationType configType);

    /**
     * Find configurations by type ordered by key
     */
    List<SystemConfiguration> findByConfigTypeOrderByConfigKey(SystemConfiguration.ConfigurationType configType);

    /**
     * Find read-only configurations
     */
    List<SystemConfiguration> findByIsReadOnlyTrue();

    /**
     * Find encrypted configurations
     */
    List<SystemConfiguration> findByIsEncryptedTrue();

    /**
     * Find configurations by key pattern
     */
    @Query("SELECT sc FROM SystemConfiguration sc WHERE sc.configKey LIKE :pattern ORDER BY sc.configKey")
    List<SystemConfiguration> findByConfigKeyLike(@Param("pattern") String pattern);

    /**
     * Find configurations by description containing text
     */
    @Query("SELECT sc FROM SystemConfiguration sc WHERE LOWER(sc.configDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY sc.configKey")
    List<SystemConfiguration> findByDescriptionContaining(@Param("searchTerm") String searchTerm);

    /**
     * Find configurations created by a specific user
     */
    List<SystemConfiguration> findByCreatedBy(String createdBy);

    /**
     * Find configurations updated by a specific user
     */
    List<SystemConfiguration> findByUpdatedBy(String updatedBy);

    /**
     * Check if configuration key exists
     */
    boolean existsByConfigKey(String configKey);

    /**
     * Check if configuration key exists excluding a specific configuration
     */
    @Query("SELECT COUNT(sc) > 0 FROM SystemConfiguration sc WHERE sc.configKey = :configKey AND sc.id != :excludeId")
    boolean existsByConfigKeyAndIdNot(@Param("configKey") String configKey, @Param("excludeId") Long excludeId);

    /**
     * Find all configuration keys
     */
    @Query("SELECT sc.configKey FROM SystemConfiguration sc ORDER BY sc.configKey")
    List<String> findAllConfigKeys();

    /**
     * Find configuration keys by type
     */
    @Query("SELECT sc.configKey FROM SystemConfiguration sc WHERE sc.configType = :configType ORDER BY sc.configKey")
    List<String> findConfigKeysByType(@Param("configType") SystemConfiguration.ConfigurationType configType);

    /**
     * Count configurations by type
     */
    long countByConfigType(SystemConfiguration.ConfigurationType configType);

    /**
     * Find configurations that need encryption
     */
    @Query("SELECT sc FROM SystemConfiguration sc WHERE sc.isEncrypted = false AND (sc.configKey LIKE '%password%' OR sc.configKey LIKE '%secret%' OR sc.configKey LIKE '%key%')")
    List<SystemConfiguration> findConfigurationsNeedingEncryption();

    /**
     * Find system-critical configurations
     */
    @Query("SELECT sc FROM SystemConfiguration sc WHERE sc.configKey IN ('system.name', 'system.version', 'database.url', 'security.jwt.secret')")
    List<SystemConfiguration> findSystemCriticalConfigurations();

    /**
     * Find email-related configurations
     */
    @Query("SELECT sc FROM SystemConfiguration sc WHERE sc.configKey LIKE 'email.%' OR sc.configKey LIKE 'mail.%' ORDER BY sc.configKey")
    List<SystemConfiguration> findEmailConfigurations();

    /**
     * Find security-related configurations
     */
    @Query("SELECT sc FROM SystemConfiguration sc WHERE sc.configKey LIKE 'security.%' OR sc.configKey LIKE 'auth.%' ORDER BY sc.configKey")
    List<SystemConfiguration> findSecurityConfigurations();

    /**
     * Find database-related configurations
     */
    @Query("SELECT sc FROM SystemConfiguration sc WHERE sc.configKey LIKE 'database.%' OR sc.configKey LIKE 'db.%' ORDER BY sc.configKey")
    List<SystemConfiguration> findDatabaseConfigurations();
}

