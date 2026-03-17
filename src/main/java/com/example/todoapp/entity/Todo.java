package com.example.todoapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

// Todo Entity
// JPA will manage table creation and mapping based on this entity
//
@Entity
@Table(name = "todos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {
    // PK
    @Id
    // Auto_increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // unique number to do

    // Column properties

    // Todo Title
    // Column Type: VARCHAR(255)
    // Not NULL
    @Column(nullable = false, length = 255)
    private String title;

    // Todo Description
    // Column Type: TEXT
    @Column(columnDefinition = "TEXT")
    private String description;

    // Todo Completed or NOT
    // Column Type: Boolean
    @Column(nullable = false)
    @Builder.Default
    private Boolean completed = false;  // 기본값 설정


    // Creation time
    // Column Type: TIMESTAMP
    // Unupdatable
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Update time
    // Column Type: TIMESTAMP
    // Updatable
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY) // ManyToMany를 ManyToOne으로 변경
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}