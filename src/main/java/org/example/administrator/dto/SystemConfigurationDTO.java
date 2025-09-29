package org.example.administrator.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.administrator.entity.SystemConfiguration;

import java.time.LocalDateTime;

/**
 * DTO for system configuration data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfigurationDTO {

    private Long id;

    @NotBlank(message = "Configuration key is required")
    private String configKey;

    @NotBlank(message = "Configuration value is required")
    private String configValue;

    private String configDescription;
    private SystemConfiguration.ConfigurationType configType;
    private Boolean isEncrypted;
    private Boolean isReadOnly;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}

