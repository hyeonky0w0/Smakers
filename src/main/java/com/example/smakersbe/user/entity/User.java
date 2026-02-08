package com.example.smakersbe.user.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id", nullable = false, updatable = false, unique = true)
    private Long userId;

    //uuid 로 시별할거면 DB에서 중복을 막아야해서 중복 제약 조건 추가
    @Column(name="uuid", nullable=false, unique=true, length=64)
    private String uuid;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    //created At 자동생성으로 보완
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }


}
