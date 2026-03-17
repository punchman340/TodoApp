package com.example.todoapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {

    @NotBlank(message = "아이디는 필수입니다")
    @Size(min = 4, max = 20, message = "아이디는 4~20자여야 합니다")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$",
            message = "아이디는 영문, 숫자, 언더스코어만 가능합니다")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, max = 100, message = "비밀번호는 8자 이상이어야 합니다")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "비밀번호는 대문자, 소문자, 숫자를 포함해야 합니다")
    private String password;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @Size(max = 50, message = "닉네임은 50자 이하여야 합니다")
    private String nickname;
}