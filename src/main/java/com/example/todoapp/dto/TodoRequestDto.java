package com.example.todoapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Data Transfer Object for Todo requests
public class TodoRequestDto {

    // Not Blank: Not Null, Not "      "
    @NotBlank(message = "제목은 필수입니다")
    // Size limit 255
    @Size(max = 255, message = "제목은 255자를 초과할 수 없습니다")
    private String title;   // Todo title
    private String description; //Todo description
    private Boolean completed;  // Completed or Not
}
