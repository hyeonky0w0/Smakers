package com.example.smakersbe.quiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_user_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizUserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="quiz_user_answer_id", nullable = false, updatable = false)
    private Long quizUserAnswerId;

    @Column(name="user_choice", nullable = false)
    private Long userChoice;

    @Column(name="is_correct", nullable = false)
    private Boolean isCorrect = false;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="quiz_set_item_id", nullable = false)
    private QuizSetItem quizSetItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="quiz_attempt_id", nullable = false)
    private QuizAttempt quizAttempt;
}
