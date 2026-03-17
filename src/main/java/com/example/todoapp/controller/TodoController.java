package com.example.todoapp.controller;

import com.example.todoapp.dto.request.TodoRequestDto;
import com.example.todoapp.dto.response.TodoResponseDto;
import com.example.todoapp.service.TodoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class TodoController {

    private final TodoService todoService;

    /**
     * 🔑 로그인 체크 헬퍼 메서드
     */
    private Long getLoginUserId(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new IllegalArgumentException("로그인이 필요합니다");
        }
        return userId;
    }

    /**
     * 🆕 수정: 내 할일 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> getAllTodos(HttpSession session) {
        try {
            Long userId = getLoginUserId(session);
            List<TodoResponseDto> todos = todoService.getAllTodos(userId);
            return ResponseEntity.ok(todos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 🆕 수정: 할일 생성
     */
    @PostMapping
    public ResponseEntity<TodoResponseDto> createTodo(
            @Valid @RequestBody TodoRequestDto requestDto,
            HttpSession session) {
        try {
            Long userId = getLoginUserId(session);
            TodoResponseDto response = todoService.createTodo(userId, requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 🆕 수정: 할일 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDto> updateTodo(
            @PathVariable Long id,
            @Valid @RequestBody TodoRequestDto requestDto,
            HttpSession session) {
        try {
            Long userId = getLoginUserId(session);
            TodoResponseDto response = todoService.updateTodo(userId, id, requestDto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 🆕 수정: 완료 토글
     */
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TodoResponseDto> toggleComplete(
            @PathVariable Long id,
            HttpSession session) {
        try {
            Long userId = getLoginUserId(session);
            TodoResponseDto response = todoService.toggleComplete(userId, id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 🆕 수정: 할일 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable Long id,
            HttpSession session) {
        try {
            Long userId = getLoginUserId(session);
            todoService.deleteTodo(userId, id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}