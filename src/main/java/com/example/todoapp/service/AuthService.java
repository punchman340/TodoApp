package com.example.todoapp.service;

import com.example.todoapp.dto.request.LoginRequest;
import com.example.todoapp.dto.request.SignupRequest;
import com.example.todoapp.dto.response.LoginResponse;
import com.example.todoapp.dto.response.UserResponse;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // 비밀번호 암호화

    /**
     * 회원가입
     */
    @Transactional
    public UserResponse signup(SignupRequest request) {
        log.info("회원가입 시도: {}", request.getUsername());

        // 1. 아이디 중복 체크
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다");
        }

        // 2. 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다");
        }

        // 3. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 4. User 엔티티 생성
        User user = User.builder()
                .username(request.getUsername())
                .password(encodedPassword)  // 암호화된 비밀번호 저장
                .email(request.getEmail())
                .nickname(request.getNickname())
                .build();

        // 5. 저장
        User savedUser = userRepository.save(user);

        log.info("회원가입 성공: {}", savedUser.getUsername());

        // 6. DTO로 변환해서 반환
        return UserResponse.fromEntity(savedUser);
    }

    /**
     * 로그인
     */
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        log.info("로그인 시도: {}", request.getUsername());

        // 1. 아이디로 사용자 찾기
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다"));

        // 2. 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다");
        }

        log.info("로그인 성공: {}", user.getUsername());

        // 3. 응답 생성
        UserResponse userResponse = UserResponse.fromEntity(user);
        return LoginResponse.of(userResponse);
    }

    /**
     * 내 정보 조회
     */
    @Transactional(readOnly = true)
    public UserResponse getMyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        return UserResponse.fromEntity(user);
    }
}