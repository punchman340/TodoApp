package com.example.todoapp.dto;

import com.example.todoapp.entity.Todo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDateTime;

@Getter @NoArgsConstructor @ AllArgsConstructor @Builder
public class TodoResponseDto {
    private Long id;
    private String title;
    private String description;
    private Boolean completed;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

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
}
