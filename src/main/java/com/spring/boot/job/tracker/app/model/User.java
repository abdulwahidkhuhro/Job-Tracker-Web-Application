package com.spring.boot.job.tracker.app.model;

import lombok.Builder;

@Builder
public record User(String username, String password, String email, String role, String fullName,
                String phoneNumber, String address, int age,
                String userId, String gender) {

}
