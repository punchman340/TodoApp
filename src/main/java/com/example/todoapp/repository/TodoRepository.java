package com.example.todoapp.repository;

import com.example.todoapp.entity.Todo;
import org.hibernate.boot.BootLogging_$logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

// Data access layer
// JPA provides standard CRUD operations
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>{
    // Find todos by completion status
    List<Todo> findByCompletedOrderByCreatedAtDesc(Boolean completed);
    // Find todos containing a specific keyword
    List<Todo> findByCompletedContaining(String keyword);
    // Search for condition including completion, title keyword
    List<Todo> findByCompletedAndTitleContaining(Boolean completed, String keyword);
    // Retrieve todos created after specific time
    List<Todo> findByCreatedAtAfter(LocalDateTime date);
    // Return the total count of todos filtered by completion
    long countByCompleted(Boolean completed);
    // Search for keyword in title or description
    @Query("SELECT t FROM Todo t WHERE t.title LIKE %:keyword% OR t.description LIKE %:keyword%")
    List<Todo> searchByKeyword(@Param("keyword") String keyword);
}
