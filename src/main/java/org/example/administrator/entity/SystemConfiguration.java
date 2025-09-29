package org.example.administrator.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * SystemConfiguration entity for managing system settings and configurations
 */
@Entity
@Table(name = "system_configurations", 
       uniqueConstraints = @UniqueConstraint(columnNames = "config_key"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "config_id")
    private Long id;

    @NotBlank(message = "Configuration key is required")
    @Column(name = "config_key", nullable = false, unique = true, length = 100)
    private String configKey;

    @NotBlank(message = "Configuration value is required")
    @Column(name = "config_value", nullable = false, length = 1000)
    private String configValue;

    @Column(name = "config_description", length = 500)
    private String configDescription;

    @NotNull(message = "Configuration type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "config_type", nullable = false)
    private ConfigurationType configType = ConfigurationType.SYSTEM;

    @Column(name = "is_encrypted")
    private Boolean isEncrypted = false;

    @Column(name = "is_read_only")
    private Boolean isReadOnly = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Enum for configuration types
     */
    public enum ConfigurationType {
        SYSTEM("System Configuration", "Core system settings"),
        SECURITY("Security Configuration", "Security-related settings"),
        EMAIL("Email Configuration", "Email service settings"),
        DATABASE("Database Configuration", "Database connection settings"),
        UI("UI Configuration", "User interface settings"),
        BUSINESS("Business Configuration", "Business logic settings");

        private final String displayName;
        private final String description;

        ConfigurationType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDescription() {
            return description;
        }
    }
}

