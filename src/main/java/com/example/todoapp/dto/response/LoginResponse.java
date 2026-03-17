package com.example.todoapp.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String token;  // JWT 토큰 (나중에 추가)
    private UserResponse user;  // 사용자 정보

    // 지금은 토큰 없이 사용자 정보만
    public static LoginResponse of(UserResponse user) {
        return LoginResponse.builder()
                .user(user)
                .build();
    }
}