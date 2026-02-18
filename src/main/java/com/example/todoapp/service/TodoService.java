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

@Service @RequiredArgsConstructor
@Slf4j @Transactional(readOnly = true)
public class TodoService {
    private final TodoRepository todoRepository;

    public List<TodoResponseDto> getAllTodos() {
        return todoRepository.findAll().stream()
                .map(TodoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<TodoResponseDto> getTodoByCompleted(Boolean completed) {
        return todoRepository.findByCompletedOrderByCreatedAtDesc(completed).stream()
                .map(TodoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public TodoResponseDto getTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("할 일을 찾을 수 없습니다. ID: " + id));
        return TodoResponseDto.fromEntity(todo);
    }

    @Transactional
    public TodoResponseDto createTodo(TodoRequestDto requestDto) {
        Todo todo = new Todo();
        todo.setTitle(requestDto.getTitle());
        todo.setDescription(requestDto.getDescription());
        todo.setCompleted(requestDto.getCompleted() != null ? requestDto.getCompleted() : false);

        Todo savedTodo = todoRepository.save(todo);
        return TodoResponseDto.fromEntity(savedTodo);
    }

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

    @Transactional
    public TodoResponseDto toggleTodoCompleted(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("할 일을 찾을 수 없습니다. ID: " + id));
        todo.setCompleted(!todo.getCompleted());
        return TodoResponseDto.fromEntity(todo);
    }

    @Transactional
    public void deleteTodo(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new RuntimeException("할 일을 찾을 수 없습니다. Id: " + id);
        }
        todoRepository.deleteById(id);
    }

    public List<TodoResponseDto> searchTodos(String keyword) {
        return todoRepository.searchByKeyword(keyword).stream()
                .map(TodoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
