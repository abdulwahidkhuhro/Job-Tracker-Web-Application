package com.spring.boot.job.tracker.app.utils;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class Utilities {

    public int generateOTP() {
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(900000) + 100000;
        return otp;
    }

}
