package com.spring.boot.job.tracker.app.service;

import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.boot.job.tracker.app.dtos.UserRequestDto;
import com.spring.boot.job.tracker.app.dtos.UserResponseDto;
import com.spring.boot.job.tracker.app.dtos.user.UserLoginDto;
import com.spring.boot.job.tracker.app.dtos.user.UserRegistrationDto;
import com.spring.boot.job.tracker.app.entity.User;
import com.spring.boot.job.tracker.app.exception.UserAlreadyExistsException;
import com.spring.boot.job.tracker.app.exception.UserAuthenticationException;
import com.spring.boot.job.tracker.app.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    private final UserRepository USER_REPOSITORY;

    @Autowired
    private EmailService emailServiceObj;

    public UserService(UserRepository USER_REPOSITORY) {
        this.USER_REPOSITORY = USER_REPOSITORY;
    }

    public UserResponseDto getUserDetails(UserRequestDto requestDto) {

        Optional<User> userOptional = USER_REPOSITORY.findByUsername(requestDto.getUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserResponseDto response = new UserResponseDto();
            response.setUsername(user.getUsername());
            response.setFullName(user.getFullName());
            response.setEmail(user.getEmail());
            return response;
        } else {
            log.warn("User not found with ID: {}", requestDto.getUserId());
            return null;
        }
    }

    @Transactional
    public void registerUser(UserRegistrationDto userDto) {

        if (USER_REPOSITORY.existsByUsername(userDto.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + userDto.getUsername());
        } else if (USER_REPOSITORY.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + userDto.getEmail());
        }

        log.info("Registering user {}", userDto);
        User userEntity = User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .role(userDto.getRole())
                .fullName(userDto.getFullName())
                .phoneNumber(userDto.getPhoneNumber())
                .address(userDto.getAddress())
                .age(userDto.getAge())
                .gender(userDto.getGender())
                .failedAttempts(0)
                .accountLocked(false)
                .isActive(true)
                .lastLoginAt(null)
                .profileImageUrl(null)
                .build();

        User savedUser = USER_REPOSITORY.save(userEntity);
        log.info("User registered successfully with ID: {}", savedUser.getUserId());
    }

    public void authenticateUser(UserLoginDto userLoginDto) throws MessagingException {
        User user = USER_REPOSITORY.findByUsernameOrEmail(userLoginDto.getUsername())
                .orElseThrow(
                        () -> new UserAuthenticationException("No User found against given email or username!"));

        if (user.getPassword().equals(userLoginDto.getPassword())) {
            emailServiceObj.sendWelcomeEmail(user.getEmail(), user.getFullName());
        } else
            throw new UserAuthenticationException("Invalid Credentials!");
    }

}
