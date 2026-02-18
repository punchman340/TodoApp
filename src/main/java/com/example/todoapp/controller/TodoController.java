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

/**
 * Todo Controller - REST API 엔드포인트
 *
 * @RestController = @Controller + @ResponseBody
 *   - 모든 메서드가 JSON으로 응답
 *   - HTML 뷰가 아닌 데이터 반환
 *
 * @RequestMapping("/api/todos")
 *   - 이 컨트롤러의 모든 경로 앞에 /api/todos 붙음
 *   - /api/todos, /api/todos/1, /api/todos/1/toggle 등
 */
@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")  // CORS 허용 (프론트엔드 연동 위해)
public class TodoController {

    private final TodoService todoService;

    /**
     * 전체 할 일 목록 조회 또는 필터링
     *
     * GET /api/todos
     * GET /api/todos?completed=true
     * GET /api/todos?completed=false
     *
     * @RequestParam:
     *   - URL 쿼리 파라미터 받기
     *   - required = false: 선택사항
     *   - 없으면 null
     *
     * ResponseEntity:
     *   - HTTP 상태 코드 + 데이터 함께 반환
     *   - ResponseEntity.ok() = 200 OK
     */
    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> getAllTodos(
            @RequestParam(required = false) Boolean completed
    ) {
        log.info("전체 할 일 조회 요청 (completed: {})", completed);

        List<TodoResponseDto> todos;

        if (completed != null) {
            // completed 파라미터가 있으면 필터링
            todos = todoService.getTodoByCompleted(completed);
        } else {
            // 없으면 전체 조회
            todos = todoService.getAllTodos();
        }

        log.info("조회된 할 일 개수: {}", todos.size());
        return ResponseEntity.ok(todos);
    }

    /**
     * 단건 조회
     *
     * GET /api/todos/1
     * GET /api/todos/2
     *
     * @PathVariable:
     *   - URL 경로의 변수 받기
     *   - {id} 부분이 Long id로 전달됨
     *
     * 예시:
     * GET /api/todos/1 → id = 1
     * GET /api/todos/999 → id = 999
     */
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto> getTodoById(@PathVariable Long id) {
        log.info("할 일 단건 조회 요청 (id: {})", id);

        TodoResponseDto todo = todoService.getTodoById(id);

        return ResponseEntity.ok(todo);
    }

    /**
     * 할 일 생성
     *
     * POST /api/todos
     * Body: { "title": "공부하기", "description": "..." }
     *
     * @Valid:
     *   - DTO 검증 실행
     *   - @NotBlank, @Size 등 체크
     *   - 실패 시 MethodArgumentNotValidException
     *
     * @RequestBody:
     *   - HTTP Body의 JSON을 DTO로 변환
     *   - Content-Type: application/json 필요
     *
     * ResponseEntity.status(HttpStatus.CREATED):
     *   - 201 Created 상태 코드
     *   - 생성 성공 시 사용
     */
    @PostMapping
    public ResponseEntity<TodoResponseDto> createTodo(
            @Valid @RequestBody TodoRequestDto requestDto
    ) {
        log.info("할 일 생성 요청 (title: {})", requestDto.getTitle());

        TodoResponseDto createdTodo = todoService.createTodo(requestDto);

        log.info("할 일 생성 완료 (id: {})", createdTodo.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }

    /**
     * 할 일 수정
     *
     * PUT /api/todos/1
     * Body: { "title": "수정된 제목", "description": "..." }
     *
     * PUT vs PATCH:
     * - PUT: 전체 교체 (모든 필드 필요)
     * - PATCH: 부분 수정 (일부 필드만)
     *
     * 우리는 PUT 사용 (간단하게)
     */
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDto> updateTodo(
            @PathVariable Long id,
            @Valid @RequestBody TodoRequestDto requestDto
    ) {
        log.info("할 일 수정 요청 (id: {}, title: {})", id, requestDto.getTitle());

        TodoResponseDto updatedTodo = todoService.updateTodo(id, requestDto);

        log.info("할 일 수정 완료 (id: {})", id);
        return ResponseEntity.ok(updatedTodo);
    }

    /**
     * 완료 상태 토글
     *
     * PATCH /api/todos/1/toggle
     *
     * PATCH:
     *   - 부분 수정
     *   - Body 없음 (토글만 하면 되니까)
     *
     * completed: false → true
     * completed: true → false
     */
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TodoResponseDto> toggleTodoCompleted(@PathVariable Long id) {
        log.info("할 일 완료 상태 토글 요청 (id: {})", id);

        TodoResponseDto todo = todoService.toggleTodoCompleted(id);

        log.info("할 일 완료 상태 변경 완료 (id: {}, completed: {})", id, todo.getCompleted());
        return ResponseEntity.ok(todo);
    }

    /**
     * 할 일 삭제
     *
     * DELETE /api/todos/1
     *
     * ResponseEntity<Void>:
     *   - 반환 데이터 없음
     *
     * ResponseEntity.noContent().build():
     *   - 204 No Content
     *   - 삭제 성공, 반환 데이터 없음
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        log.info("할 일 삭제 요청 (id: {})", id);

        todoService.deleteTodo(id);

        log.info("할 일 삭제 완료 (id: {})", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 검색 (보너스)
     *
     * GET /api/todos/search?keyword=공부
     *
     * 제목 또는 설명에 키워드 포함된 것 검색
     */
    @GetMapping("/search")
    public ResponseEntity<List<TodoResponseDto>> searchTodos(
            @RequestParam String keyword
    ) {
        log.info("할 일 검색 요청 (keyword: {})", keyword);

        List<TodoResponseDto> todos = todoService.searchTodos(keyword);

        log.info("검색 결과: {}개", todos.size());
        return ResponseEntity.ok(todos);
    }
}