package com.spring.boot.job.tracker.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


import com.spring.boot.job.tracker.app.dtos.UserRequestDto;
import com.spring.boot.job.tracker.app.dtos.UserResponseDto;
import com.spring.boot.job.tracker.app.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



@Slf4j
@RestController
@RequestMapping("api/users")
public class UserController {


    @Autowired
    private UserService userService;



    @GetMapping("/getUsers")
    public ResponseEntity<UserResponseDto> getUserDetails(@ModelAttribute UserRequestDto requestDto) {
        log.info("Received request to get user details: {}", requestDto);
        UserResponseDto response = userService.getUserDetails(requestDto);
        return ResponseEntity.ok(response);
    }

    // @PostMapping("/getUser")
    // public ResponseEntity<UserResponseDto> getUsers(@RequestBody UserRequestDto requestDto) {
    //     log.info("Received request to get user details: {}", requestDto);
    //     UserResponseDto response = userService.getUserDetails(requestDto);
    //     return ResponseEntity.ok(response);
    // }

}
