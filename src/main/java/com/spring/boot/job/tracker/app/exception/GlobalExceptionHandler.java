package com.spring.boot.job.tracker.app.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExistsException(UserAlreadyExistsException ex, Model model) {
        ex.printStackTrace();
        model.addAttribute("errorTitle", "User Already Exists");
        model.addAttribute("errorMessage", ex.getMessage());
        return "/exception/user_already_exists_error";
    }

    @ExceptionHandler(UserAuthenticationExceptionHandler.class)
    public String handleUserAuthenticationException(UserAuthenticationExceptionHandler ex, Model model) {
        ex.printStackTrace();
        model.addAttribute("errorTitle", "User Already Exists");
        model.addAttribute("errorMessage", ex.getMessage());
        return "/exception/invalid";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        ex.printStackTrace();
        model.addAttribute("errorTitle", "Something Went Wrong");
        model.addAttribute("errorMessage", ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred.");
        return "/exception/generic_exception";
    }
}
