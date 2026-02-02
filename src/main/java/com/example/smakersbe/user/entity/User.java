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
    @Column(name="user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name="uuid", nullable = false)
    private String uuId;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;


}
