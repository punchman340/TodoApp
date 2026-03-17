package com.example.todoapp.controller;

import com.example.todoapp.dto.request.LoginRequest;
import com.example.todoapp.dto.request.SignupRequest;
import com.example.todoapp.dto.response.LoginResponse;
import com.example.todoapp.dto.response.UserResponse;
import com.example.todoapp.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입
     * POST /api/auth/signup
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest request) {
        log.info("회원가입 요청: {}", request.getUsername());

        try {
            UserResponse response = authService.signup(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("회원가입 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 로그인
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session) {

        log.info("로그인 요청: {}", request.getUsername());

        try {
            LoginResponse response = authService.login(request);

            // 🔑 세션에 사용자 ID 저장
            session.setAttribute("userId", response.getUser().getId());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("로그인 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 로그아웃
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        log.info("로그아웃 요청");
        session.invalidate();  // 세션 무효화
        return ResponseEntity.ok().build();
    }

    /**
     * 내 정보 조회
     * GET /api/auth/me
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserResponse response = authService.getMyInfo(userId);
        return ResponseEntity.ok(response);
    }
}