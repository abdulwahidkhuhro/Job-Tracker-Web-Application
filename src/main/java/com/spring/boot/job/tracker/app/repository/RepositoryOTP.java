package com.spring.boot.job.tracker.app.repository;

import com.spring.boot.job.tracker.app.entity.OTP;
import com.spring.boot.job.tracker.app.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RepositoryOTP extends JpaRepository<OTP, Long> {

    @Query("""
        SELECT o FROM OTP o
        WHERE o.user.id = :userId
          AND o.otpCode = :otp
          AND o.isUsed = false
          AND o.attempts < 3
          AND o.expiresAt >= CURRENT_TIMESTAMP
    """)
    Optional<OTP> findValidOtp(@Param("userId") UUID userId, @Param("otp") int otp);

    Optional<OTP> findTopByUserOrderByCreatedAtDesc(UserEntity user);
}
