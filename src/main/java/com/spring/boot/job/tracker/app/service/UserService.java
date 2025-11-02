package com.spring.boot.job.tracker.app.service;

import java.sql.SQLException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.spring.boot.job.tracker.app.dtos.UserRequestDto;
import com.spring.boot.job.tracker.app.dtos.UserResponseDto;
import com.spring.boot.job.tracker.app.dtos.user.UserLoginDto;
import com.spring.boot.job.tracker.app.dtos.user.UserRegistrationDto;
import com.spring.boot.job.tracker.app.entity.User;
import com.spring.boot.job.tracker.app.exception.UserAlreadyExistsException;
import com.spring.boot.job.tracker.app.exception.UserAuthenticationExceptionHandler;
import com.spring.boot.job.tracker.app.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto getUserDetails(UserRequestDto requestDto) {

        Optional<User> userOptional = userRepository.findByUsername(requestDto.getUserId());
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

        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + userDto.getUsername());
        } else if (userRepository.existsByEmail(userDto.getEmail())) {
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

        User savedUser = userRepository.save(userEntity);
        log.info("User registered successfully with ID: {}", savedUser.getUserId());
    }

    public boolean authenticateUser(UserLoginDto userLoginDto) {
        Optional<User> user = userRepository.findByUsername(userLoginDto.getUsername());

        if (user.isEmpty()) {
            user = userRepository.findByEmail(userLoginDto.getUsername());
        }

        log.debug("Fetched user from db for authentication {}",user);

        return user
                .map(u -> u.getPassword().equals(userLoginDto.getPassword()))
                .orElse(false);
    }

}
