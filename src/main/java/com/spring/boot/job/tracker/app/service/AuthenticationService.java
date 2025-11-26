package com.spring.boot.job.tracker.app.service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spring.boot.job.tracker.app.dtos.user.UserLoginDto;
import com.spring.boot.job.tracker.app.entity.OTP;
import com.spring.boot.job.tracker.app.entity.UserEntity;
import com.spring.boot.job.tracker.app.exception.UserAuthenticationException;
import com.spring.boot.job.tracker.app.repository.RepositoryOTP;
import com.spring.boot.job.tracker.app.repository.UserRepository;
import com.spring.boot.job.tracker.app.utils.Utilities;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthenticationService {

    private final UserRepository USER_REPOSITORY;
    private final RepositoryOTP OTP_REPOSITORY;

    // @Autowired
    public AuthenticationService(UserRepository USER_REPOSITORY, RepositoryOTP OTP_REPOSITORY) {
        this.USER_REPOSITORY = USER_REPOSITORY;
        this.OTP_REPOSITORY = OTP_REPOSITORY;
    }

    @Autowired
    private EmailService emailServiceObj;

    @Autowired
    private Utilities utils;

    public void authenticateUser(UserLoginDto userLoginDto) throws MessagingException {
        UserEntity user = USER_REPOSITORY.findByUsernameOrEmail(userLoginDto.getUsername())
                .orElseThrow(
                        () -> new UserAuthenticationException("No User found against given email or username!"));

        if (user.getPassword().equals(userLoginDto.getPassword())) {
            emailServiceObj.sendWelcomeEmail(user.getEmail(), user.getFullName());
        } else
            throw new UserAuthenticationException("Invalid Credentials!");
    }

    // public void sendEmailOTP(String email) throws MessagingException {
    //     UserEntity user = USER_REPOSITORY.findByEmail(email)
    //             .orElseThrow(() -> new UserAuthenticationException("User not found against email: " + email));

    //     int otp = utils.generateOTP();
    //     log.info("GENERATED OTP:{} FOR USER:{}", otp, user);

    //     OTP entityOTP = new OTP();
    //     entityOTP.setAttempts(0);
    //     entityOTP.setExpiresAt(LocalDateTime.now().plusMinutes(5));
    //     entityOTP.setOtpCode(otp);
    //     entityOTP.setRequestIp(null);
    //     entityOTP.setUsed(false);
    //     entityOTP.setUser(user);
    //     entityOTP.setCreatedAt(LocalDateTime.now());

    //     OTP savedOTP = OTP_REPOSITORY.save(entityOTP);
    //     String username = user.getUsername();

    //     emailServiceObj.sendEmailOTP(email, username, savedOTP.getOtpCode());

    // }

    // public String verifyOTP(String OTP, String email) {

    //     UserEntity user = USER_REPOSITORY.findByEmail(email).orElse(null);

    //     if (OTP_REPOSITORY.findValidOtp(user.getUserId(), Integer.parseInt(OTP)).isPresent())
    //         return user.getUsername();
    //     else
    //         return null;
    // }

}
