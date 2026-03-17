package com.example.todoapp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private UserResponse user;

    public static LoginResponse of (UserResponse user) {
        return LoginResponse.builder()
                .user(user)
                .build();
    }
}
