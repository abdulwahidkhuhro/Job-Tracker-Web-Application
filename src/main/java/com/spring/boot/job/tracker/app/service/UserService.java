package com.spring.boot.job.tracker.app.service;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import com.spring.boot.job.tracker.app.dtos.UserRequestDto;
import com.spring.boot.job.tracker.app.dtos.UserResponseDto;
import com.spring.boot.job.tracker.app.dtos.user.UserRegistrationDto;
import com.spring.boot.job.tracker.app.entity.UserEntity;
import com.spring.boot.job.tracker.app.exception.UserAlreadyExistsException;
import com.spring.boot.job.tracker.app.exception.UserNotFoundException;
import com.spring.boot.job.tracker.app.model.User;
import com.spring.boot.job.tracker.app.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    private final UserRepository USER_REPOSITORY;

    public UserService(UserRepository USER_REPOSITORY) {
        this.USER_REPOSITORY = USER_REPOSITORY;
    }

    public User getUserDetails(String username) {

        UserEntity userEntity = USER_REPOSITORY.findByUsername(username)
                .or(() -> USER_REPOSITORY.findByEmail(username))
                .orElseThrow(() -> new UserNotFoundException("User not found", username));

        User user = User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .role(userEntity.getRole())
                .fullName(userEntity.getFullName())
                .phoneNumber(userEntity.getPhoneNumber())
                .address(userEntity.getAddress())
                .age(userEntity.getAge())
                .userId(userEntity.getUserId().toString())
                .gender(userEntity.getGender())
                .build();

        return user;
    }

    public UserEntity getUserEntityByEmail(String email) {
        return USER_REPOSITORY.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found:" + email, email));
    }

    @Transactional
    public void registerUser(UserRegistrationDto userDto) {

        if (USER_REPOSITORY.existsByUsername(userDto.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + userDto.getUsername());
        } else if (USER_REPOSITORY.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + userDto.getEmail());
        }

        log.info("Registering user {}", userDto);
        UserEntity userEntity = UserEntity.builder()
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

        UserEntity savedUser = USER_REPOSITORY.save(userEntity);
        log.info("User registered successfully with ID: {}", savedUser.getUserId());
    }
}
