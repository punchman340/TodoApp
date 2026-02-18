package com.example.todoapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Todo 엔티티
 *
 * 데이터베이스의 todos 테이블과 매핑되는 클래스
 * JPA가 이 클래스를 보고 자동으로 테이블을 생성하거나 매핑함
 */
@Entity  // 이 클래스가 JPA 엔티티임을 선언
@Table(name = "todos")  // 매핑할 테이블 이름 (생략 시 클래스명의 소문자)
@Getter  // Lombok: 모든 필드의 getter 자동 생성
@Setter  // Lombok: 모든 필드의 setter 자동 생성
@NoArgsConstructor  // Lombok: 기본 생성자 생성 (JPA 필수!)
@AllArgsConstructor  // Lombok: 모든 필드를 받는 생성자 생성
public class Todo {

    /**
     * 고유 ID (Primary Key)
     *
     * @Id: 이 필드가 기본키임을 표시
     * @GeneratedValue: 값을 자동으로 생성
     *   - strategy = IDENTITY: 데이터베이스의 AUTO_INCREMENT 사용
     *   - PostgreSQL의 SERIAL/BIGSERIAL과 매핑
     *
     * Long 타입 사용 이유:
     *   - Integer는 최대 21억 (2^31 - 1)
     *   - Long은 최대 900경 (2^63 - 1)
     *   - null 가능 (long은 불가)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 할 일 제목
     *
     * @Column: 컬럼 속성 지정
     *   - nullable = false: NOT NULL 제약조건
     *   - length = 255: VARCHAR(255)
     *
     * nullable = false를 쓰는 이유:
     *   - 데이터베이스 레벨에서 검증
     *   - NULL 값 저장 시도 시 에러 발생
     */
    @Column(nullable = false, length = 255)
    private String title;

    /**
     * 할 일 설명
     *
     * @Column(columnDefinition = "TEXT"):
     *   - PostgreSQL의 TEXT 타입 사용
     *   - 길이 제한 없음 (최대 1GB)
     *
     * nullable 지정 안 함 = null 허용
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 완료 여부
     *
     * Boolean vs boolean:
     *   - Boolean: 객체 타입, null 가능
     *   - boolean: 원시 타입, null 불가, 기본값 false
     *
     * 우리는 Boolean 사용:
     *   - JPA가 객체 타입 선호
     *   - null 체크 가능
     */
    @Column(nullable = false)
    private Boolean completed = false;  // 기본값 설정

    /**
     * 생성 일시
     *
     * @CreationTimestamp:
     *   - Hibernate 어노테이션
     *   - INSERT 시 자동으로 현재 시간 입력
     *   - UPDATE 시에는 값 변경 안 함
     *
     * LocalDateTime:
     *   - Java 8 이후 날짜/시간 API
     *   - TIMESTAMP 타입과 매핑
     *   - 타임존 없음 (필요하면 ZonedDateTime 사용)
     *
     * updatable = false:
     *   - UPDATE 쿼리에서 이 컬럼 제외
     *   - 생성 후 변경 불가
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 수정 일시
     *
     * @UpdateTimestamp:
     *   - UPDATE 시 자동으로 현재 시간으로 갱신
     *   - INSERT 시에도 현재 시간 입력
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /*
     * Lombok 덕분에 자동 생성되는 메서드들:
     *
     * - public Long getId() { return id; }
     * - public void setId(Long id) { this.id = id; }
     * - public String getTitle() { return title; }
     * - public void setTitle(String title) { this.title = title; }
     * - ... (모든 필드)
     *
     * - public Todo() {}  // 기본 생성자 (JPA 필수!)
     * - public Todo(Long id, String title, ...) {}  // 모든 필드 생성자
     *
     * Lombok 없이 작성하면 100줄 이상!
     */
}