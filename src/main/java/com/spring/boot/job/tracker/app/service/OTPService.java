package com.spring.boot.job.tracker.app.service;

import com.spring.boot.job.tracker.app.entity.OTP;
import com.spring.boot.job.tracker.app.entity.UserEntity;
import com.spring.boot.job.tracker.app.model.User;
import com.spring.boot.job.tracker.app.repository.RepositoryOTP;
import com.spring.boot.job.tracker.app.repository.UserRepository;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OTPService {

    @Autowired
    private final RepositoryOTP otpRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private EmailService emailService;

    private static final int OTP_VALID_MINUTES = 5;

    private int generateOTP() {
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(900000) + 100000;
        return otp;
    }

    public void sendOtp(String email) throws MessagingException {

        UserEntity userEntity = userService.getUserEntityByEmail(email);

        int OTP = generateOTP();
        OTP otp = new OTP();
        otp.setUser(userEntity);
        otp.setOtpCode(OTP);
        otp.setCreatedAt(LocalDateTime.now());
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(OTP_VALID_MINUTES));
        otp.setAttempts(0);
        otp.setUsed(false);

        otpRepository.save(otp);

        emailService.sendEmailOTP(email, userEntity.getUsername(), OTP);


    }

    public boolean verifyOtp(String otpInput, String email) {
        UserEntity userEntity = userService.getUserEntityByEmail(email);

        int otpCode;
        try {
            otpCode = Integer.parseInt(otpInput);
        } catch (NumberFormatException e) {
            incrementLatestOtpAttempts(userEntity);
            return false;
        }

        Optional<OTP> otpOptional = otpRepository.findValidOtp(userEntity.getUserId(), otpCode);

        if (otpOptional.isPresent()) {
            OTP otp = otpOptional.get();
            otp.setUsed(true);
            otpRepository.save(otp);
            return true;
        } else {
            incrementLatestOtpAttempts(userEntity);
            return false;
        }
    }

    private void incrementLatestOtpAttempts(UserEntity user) {
        otpRepository.findTopByUserOrderByCreatedAtDesc(user)
                .ifPresent(latestOtp -> {
                    latestOtp.setAttempts(latestOtp.getAttempts() + 1);
                    otpRepository.save(latestOtp);
                });
    }
}
