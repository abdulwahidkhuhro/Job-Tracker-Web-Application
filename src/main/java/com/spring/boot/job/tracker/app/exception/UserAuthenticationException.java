package com.spring.boot.job.tracker.app.exception;

public class UserAuthenticationException extends RuntimeException{

    public UserAuthenticationException(String message){
        super(message);
    }
}
