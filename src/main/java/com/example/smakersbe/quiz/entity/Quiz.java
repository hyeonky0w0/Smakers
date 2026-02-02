package com.example.smakersbe.quiz.entity;

import com.example.smakersbe.asset.entity.Asset;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "quizzes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="quiz_id", nullable = false, updatable = false)
    private Long quizId;

    @Column(name="question", columnDefinition = "TEXT",nullable = false)
    private String question;

    @Column(name="options",columnDefinition = "json", nullable = false)
    private String options;

    @Column(name="answer", nullable = false)
    private Long answer;

    @Column(name="explanation",columnDefinition = "TEXT", nullable = false)
    private String explanation;


    @Column(name="hint", columnDefinition = "TEXT",nullable = false)
    private String hint;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="asset_id", nullable = false)
    private Asset asset;
}
