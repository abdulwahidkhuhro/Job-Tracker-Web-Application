package com.spring.boot.job.tracker.app.exception;

public class UserAuthenticationExceptionHandler extends RuntimeException{

    public UserAuthenticationExceptionHandler(String message){
        super(message);
    }
}
