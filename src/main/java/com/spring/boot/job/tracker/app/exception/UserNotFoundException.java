package com.spring.boot.job.tracker.app.exception;

public class UserNotFoundException extends RuntimeException {

    private String username;


    public UserNotFoundException(String message , String username ){
        super(message);
        this.username = username;
    }

}
