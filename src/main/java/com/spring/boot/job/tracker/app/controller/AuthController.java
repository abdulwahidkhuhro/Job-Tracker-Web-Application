package com.spring.boot.job.tracker.app.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.spring.boot.job.tracker.app.dtos.user.UserLoginDto;
import com.spring.boot.job.tracker.app.dtos.user.UserRegistrationDto;
import com.spring.boot.job.tracker.app.service.EmailService;
import com.spring.boot.job.tracker.app.service.UserService;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AuthController {

    @Autowired
    private UserService userServiceObj;

    // @Autowired -- If there is only one constructor, @Autowired can be omitted
    // public AuthController(UserService userService, EmailService emailServiceObj) {
    //     this.userServiceObj = userService;
    //     this.emailServiceObj = emailServiceObj;
    // }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("user", new UserLoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String processUserLogin(@ModelAttribute("user") UserLoginDto userLoginDto, Model model) throws MessagingException {
        log.info("Authenticate user for login {}", userLoginDto);
        userServiceObj.authenticateUser(userLoginDto);
        return "redirect:/user/dashboard?username=" + userLoginDto.getUsername();
    }

    @GetMapping("/register")
    public String register(Model model) {

        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setUsername("test_123");
        userDto.setFullName("TESTING");
        userDto.setAddress("KARACHI");
        userDto.setEmail("test@example.com");
        userDto.setPassword("password");
        userDto.setPhoneNumber("1234567890");
        userDto.setAge(30);
        userDto.setGender("MALE");
        userDto.setRole("USER");
        userDto.setPassword("test@123");

        model.addAttribute("user", userDto);
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") UserRegistrationDto userDto, RedirectAttributes redirectAttributes) {
        log.info("User Registration Request Dto: {}", userDto);
        userServiceObj.registerUser(userDto);
        redirectAttributes.addFlashAttribute("registrationSuccess", true);
        return "redirect:/register";
    }

}
