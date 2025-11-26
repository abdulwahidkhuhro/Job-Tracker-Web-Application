package com.spring.boot.job.tracker.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_role", columnList = "role"),
        @Index(name = "idx_users_is_active", columnList = "is_active"),
        @Index(name = "idx_users_account_locked", columnList = "account_locked"),
        @Index(name = "idx_users_last_login_at", columnList = "last_login_at"),
        @Index(name = "idx_users_email_active", columnList = "email, is_active")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_users_username", columnNames = "username"),
        @UniqueConstraint(name = "uc_users_email", columnNames = "email")
})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "password", length = 16, nullable = false)
    private String password;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(name = "role", length = 10, nullable = false)
    private String role;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;


    @Column(name = "age" , length = 3)
    private Integer age;

    @Column(name = "gender", length = 10)
    private String gender; // MALE, FEMALE, OTHER

    @Column(name = "profile_image_url", columnDefinition = "TEXT")
    private String profileImageUrl;

    @Column(name = "failed_attempts", nullable = false)
    private Integer failedAttempts;

    @Column(name = "account_locked", nullable = false)
    private Boolean accountLocked;

    @Column(name = "last_login_at")
    private OffsetDateTime lastLoginAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    // ============================
    // Convenience Methods
    // ============================

    public void lockAccount() {
        this.accountLocked = true;
    }

    public void unlockAccount() {
        this.accountLocked = false;
        this.failedAttempts = 0;
    }

    public void incrementFailedAttempts() {
        this.failedAttempts = (this.failedAttempts == null ? 1 : this.failedAttempts + 1);
    }
}
