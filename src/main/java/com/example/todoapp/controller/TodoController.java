package com.example.todoapp.controller;

import com.example.todoapp.dto.TodoRequestDto;
import com.example.todoapp.dto.TodoResponseDto;
import com.example.todoapp.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Todo Controller
@RestController     //Combines @Controller and @ResponseBody
@RequestMapping("/api/todos")   //Base path for all endpoints in controller
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") //Enables CORS for frontend integration
public class TodoController {

    private final TodoService todoService;

    //Retrieve all todos or Filter by status
    //GET /api/todos?completed=true
    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> getAllTodos(
            @RequestParam(required = false) Boolean completed)   //Optional query parameter for filtering
    {
        log.info("Request: Get all todos (Filtered by completed: {})", completed);

        List<TodoResponseDto> todos;
        if (completed != null) {
            todos = todoService.getTodoByCompleted(completed);
        } else {
            todos = todoService.getAllTodos();
        }

        return ResponseEntity.ok(todos);
    }

    //Retrieve a single Todo by ID
    //GET /api/todos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto> getTodoById(@PathVariable Long id)
    {
        log.info("Request: Get todo by ID ({})", id);
        TodoResponseDto todo = todoService.getTodoById(id);
        return ResponseEntity.ok(todo);
    }

    //Create a new Todo
    //POST /api/todos
    @PostMapping
    public ResponseEntity<TodoResponseDto> createTodo(
            //@Valid: Triggers DTO validation (@NotBlank, @Size)
            //@RequestBody: Deserializes incoming JSON into a DTO object
            @Valid @RequestBody TodoRequestDto requestDto)
    {
        log.info("Request: Create todo with title: {}", requestDto.getTitle());
        TodoResponseDto createdTodo = todoService.createTodo(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }

    //Update an existing Todo
    //PUT /api/todos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDto> updateTodo(
            @PathVariable Long id,
            @Valid @RequestBody TodoRequestDto requestDto)
    {
        log.info("Request: Update todo ID: {}", id);
        TodoResponseDto updatedTodo = todoService.updateTodo(id, requestDto);
        return ResponseEntity.ok(updatedTodo);
    }

    //Toggle the completion status of a Todo
    //PATCH /api/todos/{id}/toggle
    //PATCH is used for partial updates
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TodoResponseDto> toggleTodoCompleted(@PathVariable Long id)
    {
        log.info("Request: Toggle status for ID: {}", id);
        TodoResponseDto todo = todoService.toggleTodoCompleted(id);
        return ResponseEntity.ok(todo);
    }

    //Delete a Todo by ID
    //DELETE /api/todos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id)
    {
        log.info("Request: Delete todo ID: {}", id);
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

    //Search Todos by keyword in title or description
    //GET /api/todos/search?keyword=...
    @GetMapping("/search")
    public ResponseEntity<List<TodoResponseDto>> searchTodos(
            @RequestParam String keyword)
    {
        log.info("Request: Search todos with keyword: {}", keyword);
        List<TodoResponseDto> todos = todoService.searchTodos(keyword);
        return ResponseEntity.ok(todos);
    }
}