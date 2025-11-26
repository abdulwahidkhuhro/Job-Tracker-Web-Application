package com.spring.boot.job.tracker.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.boot.job.tracker.app.dtos.UserRequestDto;
import com.spring.boot.job.tracker.app.dtos.UserResponseDto;
import com.spring.boot.job.tracker.app.dtos.user.UserRegistrationDto;
import com.spring.boot.job.tracker.app.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Slf4j
@RestController
@RequestMapping("api/user")
public class UserController {


    @Autowired
    private UserService userService;



    // @GetMapping("/getUsers")
    // public ResponseEntity<UserResponseDto> getUserDetails(@ModelAttribute UserRequestDto requestDto) {
    //     log.info("Received request to get user details: {}", requestDto);
    //     UserResponseDto response = userService.getUserDetails(requestDto);
    //     return ResponseEntity.ok(response);
    // }



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
        userService.registerUser(userDto);
        redirectAttributes.addFlashAttribute("registrationSuccess", true);
        return "redirect:/register";
    }


    // @PostMapping("/getUser")
    // public ResponseEntity<UserResponseDto> getUsers(@RequestBody UserRequestDto requestDto) {
    //     log.info("Received request to get user details: {}", requestDto);
    //     UserResponseDto response = userService.getUserDetails(requestDto);
    //     return ResponseEntity.ok(response);
    // }

}
