package org.example.administrator.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * UserActivity entity for tracking user actions and system activities
 */
@Entity
@Table(name = "user_activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long id;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Activity type is required")
    @Column(name = "activity_type", nullable = false, length = 50)
    private String activityType;

    @NotBlank(message = "Activity description is required")
    @Column(name = "activity_description", nullable = false, length = 500)
    private String activityDescription;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "session_id", length = 100)
    private String sessionId;

    @Column(name = "activity_timestamp", nullable = false)
    private LocalDateTime activityTimestamp;

    @Column(name = "success")
    private Boolean success = true;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    @Column(name = "additional_data", length = 2000)
    private String additionalData;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (activityTimestamp == null) {
            activityTimestamp = LocalDateTime.now();
        }
    }

    /**
     * Create a successful activity log
     */
    public static UserActivity createSuccessActivity(User user, String activityType, String description) {
        UserActivity activity = new UserActivity();
        activity.setUser(user);
        activity.setActivityType(activityType);
        activity.setActivityDescription(description);
        activity.setSuccess(true);
        return activity;
    }

    /**
     * Create a failed activity log
     */
    public static UserActivity createFailureActivity(User user, String activityType, String description, String errorMessage) {
        UserActivity activity = new UserActivity();
        activity.setUser(user);
        activity.setActivityType(activityType);
        activity.setActivityDescription(description);
        activity.setSuccess(false);
        activity.setErrorMessage(errorMessage);
        return activity;
    }
}

