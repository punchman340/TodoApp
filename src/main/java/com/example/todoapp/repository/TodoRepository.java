package com.example.todoapp.repository;

import com.example.todoapp.entity.Todo;
import org.hibernate.boot.BootLogging_$logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>{
    List<Todo> findByCompletedOrderByCreatedAtDesc(Boolean completed);

    List<Todo> findByCompletedContaining(String keyword);

    List<Todo> findByCompletedAndTitleContaining(Boolean completed, String keyword);

    List<Todo> findByCreatedAtAfter(LocalDateTime date);

    long countByCompleted(Boolean completed);

    @Query("SELECT t FROM Todo t WHERE t.title LIKE %:keyword% OR t.description LIKE %:keyword%")
    List<Todo> searchByKeyword(@Param("keyword") String keyword);
}
