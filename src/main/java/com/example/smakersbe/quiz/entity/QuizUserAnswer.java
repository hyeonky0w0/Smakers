package com.example.smakersbe.quiz.entity;

import com.example.smakersbe.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quiz_user_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizUserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="answer_id", nullable = false, updatable = false)
    private Long answerId;

    @Column(name="user_choice", nullable = false)
    private Long userChoice;

    @Column(name="is_correct", nullable = false)
    private Boolean isCorrect = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="quiz_id", nullable = false)
    private Quiz quiz;
}
