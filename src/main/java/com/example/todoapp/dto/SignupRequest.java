package com.example.todoapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {
    @NotBlank(message = "아이디는 필수입니다")
    @Size(min = 4, max =  15, message = "4~15자 아이디를 입력해주세요")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$",
            message = "아이디는 영문, 숫자, 언더스코어만 가능합니다")
    private String username;

    @NotBlank
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상이어야합니다")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "비밀번호는 대문자, 소문자, 숫자를 포함해야합니다.")
    private String password;

    @NotBlank(message = "이메일은 필수 사항입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @Size(max = 10, message = "닉네임은 10자 이하여야 합니다")
    private String nickname;
}