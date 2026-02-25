package com.example.todoapp.dto;

import com.example.todoapp.entity.Todo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Data Transfer Object for Todo response
public class TodoResponseDto {
    private Long id;                //Todo ID
    private String title;           //Todo title
    private String description;     //Todo description
    private Boolean completed;      //Todo completed or Not

    // Formats LocalDateTime object into "pattern" when converting to JSON
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    // convert Todo entity into TodoResponseDto
    public static TodoResponseDto fromEntity(Todo todo) {
        return TodoResponseDto.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .completed(todo.getCompleted())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .build();
    }
    // Json converting:
    // TodoResponseDto = ...;
    //
    // {
    // "id": 1,
    // "title" : "study",
    // "description": "java learning",
    // "completed" : false,
    // "createdAt": "2026-02-17 15:30:30"
    // "updatedAt": "2026-02-17 15:30:30:
    // }
}
