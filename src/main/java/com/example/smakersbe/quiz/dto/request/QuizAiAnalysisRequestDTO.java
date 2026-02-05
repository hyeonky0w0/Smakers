package com.example.smakersbe.quiz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizAiAnalysisRequestDTO {

    private String uuid;
    private Long quizAttemptId;


}
