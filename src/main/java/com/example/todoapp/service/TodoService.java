package com.example.todoapp.service;

import com.example.todoapp.dto.TodoRequestDto;
import com.example.todoapp.dto.TodoResponseDto;
import com.example.todoapp.entity.Todo;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.TodoRepository;
import com.example.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;  // 🆕 추가

    /**
     * 🆕 수정: 특정 사용자의 모든 할일 조회
     */
    @Transactional(readOnly = true)
    public List<TodoResponseDto> getAllTodos(Long userId) {
        log.info("사용자 {}의 할일 목록 조회", userId);

        // User 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        // 해당 사용자의 할일만 조회
        List<Todo> todos = todoRepository.findByUserOrderByCreatedAtDesc(user);

        return todos.stream()
                .map(TodoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 🆕 수정: 할일 생성 (사용자 연결)
     */
    @Transactional
    public TodoResponseDto createTodo(Long userId, TodoRequestDto requestDto) {
        log.info("사용자 {}의 할일 생성: {}", userId, requestDto.getTitle());

        // User 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        // Todo 생성
        Todo todo = Todo.builder()
                .title(requestDto.getTitle())
                .completed(false)
                .user(user)  // 🆕 사용자 연결
                .build();

        Todo savedTodo = todoRepository.save(todo);

        return TodoResponseDto.fromEntity(savedTodo);
    }

    /**
     * 🆕 수정: 할일 수정 (본인 할일만)
     */
    @Transactional
    public TodoResponseDto updateTodo(Long userId, Long todoId, TodoRequestDto requestDto) {
        log.info("사용자 {}의 할일 {} 수정", userId, todoId);

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("할일을 찾을 수 없습니다"));

        // 🆕 본인 할일인지 확인
        if (!todo.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("다른 사용자의 할일은 수정할 수 없습니다");
        }

        todo.setTitle(requestDto.getTitle());

        return TodoResponseDto.fromEntity(todo);
    }

    /**
     * 🆕 수정: 완료 토글 (본인 할일만)
     */
    @Transactional
    public TodoResponseDto toggleTodoCompleted(Long userId, Long todoId) {
        log.info("사용자 {}의 할일 {} 완료 토글", userId, todoId);

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("할일을 찾을 수 없습니다"));

        // 🆕 본인 할일인지 확인
        if (!todo.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("다른 사용자의 할일은 수정할 수 없습니다");
        }

        todo.setCompleted(!todo.getCompleted());

        return TodoResponseDto.fromEntity(todo);
    }

    /**
     * 🆕 수정: 할일 삭제 (본인 할일만)
     */
    @Transactional
    public void deleteTodo(Long userId, Long todoId) {
        log.info("사용자 {}의 할일 {} 삭제", userId, todoId);

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("할일을 찾을 수 없습니다"));

        // 🆕 본인 할일인지 확인
        if (!todo.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("다른 사용자의 할일은 삭제할 수 없습니다");
        }

        todoRepository.delete(todo);
    }

    @Transactional(readOnly = true)
    public List<TodoResponseDto> searchTodos(Long userId, String keyword) {
        log.info("사용자 {}의 할일 키워드 검색: {}", userId, keyword);
        List<Todo> searchResults = todoRepository.searchByKeyword(keyword);
        return searchResults.stream()
                .filter(todo -> todo.getUser().getId().equals(userId))
                .map(TodoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}