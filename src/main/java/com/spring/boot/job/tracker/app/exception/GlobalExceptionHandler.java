package com.spring.boot.job.tracker.app.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.spring.boot.job.tracker.app.dtos.user.UserLoginDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExistsException(UserAlreadyExistsException ex, Model model) {
        ex.printStackTrace();
        model.addAttribute("errorTitle", "User Already Exists");
        model.addAttribute("errorMessage", ex.getMessage());
        return "/exception/user_already_exists_error";
    }

    @ExceptionHandler(UserAuthenticationException.class)
    public String handleUserAuthenticationException(UserAuthenticationException ex, Model model) {
        log.error("Authentication error: {}", ex.getMessage(), ex);
        model.addAttribute("loginError", ex.getMessage());
        model.addAttribute("user", new UserLoginDto());
        return "login";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        ex.printStackTrace();
        model.addAttribute("errorTitle", "Something Went Wrong");
        model.addAttribute("errorMessage", ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred.");
        return "/exception/generic_exception";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(Exception ex, Model model) {
        model.addAttribute("errorTitle", "Something Went Wrong");
        model.addAttribute("errorMessage", ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred.");
        return "/exception/generic_exception";
    }
}
