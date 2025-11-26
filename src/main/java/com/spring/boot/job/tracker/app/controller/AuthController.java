package com.spring.boot.job.tracker.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.spring.boot.job.tracker.app.dtos.user.UserLoginDto;
import com.spring.boot.job.tracker.app.dtos.user.UserRegistrationDto;
import com.spring.boot.job.tracker.app.service.AuthenticationService;
import com.spring.boot.job.tracker.app.service.OTPService;
import com.spring.boot.job.tracker.app.service.UserService;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private OTPService otpService;

    // @Autowired -- If there is only one constructor, @Autowired can be omitted
    // public AuthController(UserService userService, EmailService emailServiceObj)
    // {
    // this.userServiceObj = userService;
    // this.emailServiceObj = emailServiceObj;
    // }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("user", new UserLoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String processUserLogin(@ModelAttribute("user") UserLoginDto userLoginDto, Model model)throws MessagingException {
        log.info("Authenticate user for login {}", userLoginDto);
        authService.authenticateUser(userLoginDto);
        return "redirect:/user/dashboard?username=" + userLoginDto.getUsername();
    }

    @GetMapping("/reset")
    public String resetPassword(Model model) {
        model.addAttribute("user", new UserLoginDto());
        return "reset";
    }

    @PostMapping("/OTP")
    public String sendEmailOTP(@RequestParam("identifier") String email, Model model)throws MessagingException {

        log.info("Reset Account for user = {}" , email);

        otpService.sendOtp(email);

        model.addAttribute("message", "If this account exists, an OTP has been sent.");
        model.addAttribute("email" , email);
        return "otp_verification";
    }

    @PostMapping("/verifyOTP")
    public String verifyOTP(@RequestParam("otp") String otp , @RequestParam("email") String email) {

        // HIDDEN FIELD FROM otp_verification
        log.info("VERIFY OTP FOR EMAIL: {}" , email);
        if(otpService.verifyOtp(otp , email))
            return "redirect:/user/dashboard?username=" + email;
 

    
        return "entity";
    }

}
