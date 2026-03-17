package com.example.todoapp.repository;

import com.example.todoapp.entity.Todo;
import com.example.todoapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    // 🆕 추가: 특정 사용자의 할일 조회
    List<Todo> findByUserOrderByCreatedAtDesc(User user);

    // 기존 메서드들은 그대로 유지 가능 (사용 안 해도 됨)
    List<Todo> findByCompletedOrderByCreatedAtDesc(Boolean completed);
}