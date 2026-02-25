package com.example.todoapp.service;

import com.example.todoapp.dto.TodoResponseDto;
import com.example.todoapp.dto.TodoRequestDto;
import com.example.todoapp.entity.Todo;
import com.example.todoapp.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

// Service layer for business logic
@Service @RequiredArgsConstructor
@Slf4j  // Enable logging
@Transactional(readOnly = true)
public class TodoService {
    private final TodoRepository todoRepository;
    // Retrieve all todos and convert them to ResponseDTO
    public List<TodoResponseDto> getAllTodos() {
        return todoRepository.findAll().stream()
                .map(TodoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
    // Fetch todos by completion status and convert to DTO
    public List<TodoResponseDto> getTodoByCompleted(Boolean completed) {
        return todoRepository.findByCompletedOrderByCreatedAtDesc(completed).stream()
                .map(TodoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
    // Find a single todo by ID (throws exception)
    public TodoResponseDto getTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("할 일을 찾을 수 없습니다. ID: " + id));
        return TodoResponseDto.fromEntity(todo);
    }
    // Create a new todo (Transcation required!)
    @Transactional
    public TodoResponseDto createTodo(TodoRequestDto requestDto) {
        Todo todo = new Todo();
        todo.setTitle(requestDto.getTitle());
        todo.setDescription(requestDto.getDescription());
        todo.setCompleted(requestDto.getCompleted() != null ? requestDto.getCompleted() : false);
        Todo savedTodo = todoRepository.save(todo);
        return TodoResponseDto.fromEntity(savedTodo);
    }
    // Update an existing todo
    @Transactional
    public TodoResponseDto updateTodo(Long id, TodoRequestDto requestDto) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("할 일을 찾을 수 없습니다. ID: " + id));

        todo.setTitle(requestDto.getTitle());
        todo.setDescription(requestDto.getDescription());
        if (requestDto.getCompleted() != null) {
            todo.setCompleted(requestDto.getCompleted());
        }

        return TodoResponseDto.fromEntity(todo);
    }
    // Toggle the completion todo status
    @Transactional
    public TodoResponseDto toggleTodoCompleted(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("할 일을 찾을 수 없습니다. ID: " + id));
        todo.setCompleted(!todo.getCompleted());
        return TodoResponseDto.fromEntity(todo);
    }
    // Delete a todo
    @Transactional
    public void deleteTodo(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new RuntimeException("할 일을 찾을 수 없습니다. Id: " + id);
        }
        todoRepository.deleteById(id);
    }
    // Search for todo based on keyword
    public List<TodoResponseDto> searchTodos(String keyword) {
        return todoRepository.searchByKeyword(keyword).stream()
                .map(TodoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
