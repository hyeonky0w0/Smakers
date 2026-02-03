package com.example.smakersbe.quiz.entity;

import com.example.smakersbe.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="quiz_result_id", nullable = false, updatable = false)
    private Long quizResultId;

    @Column(name="score", nullable = false)
    private Long score;

    @Column(name="total_count", nullable = false)
    private Long totalCount;

    @Column(name="correct_count", nullable = false)
    private Long correctCount;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="ai_review", columnDefinition = "TEXT", nullable = false)
    private String aiReview;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="quiz_attempt_id", nullable = false)
    private QuizAttempt quizAttempt;

}
