package com.example.smakersbe.quiz.dto.response;

import com.example.smakersbe.quiz.dto.request.QuizAiAnalysisRequestDTO;
import com.example.smakersbe.quiz.entity.QuizResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizAiAnalysisResponseDTO {

    private Long quizAttemptId;
    private String aiReview;

    public static QuizAiAnalysisResponseDTO from(QuizResult quizResult) {
        return QuizAiAnalysisResponseDTO.builder()
                .quizAttemptId(quizResult.getQuizAttempt().getQuizAttemptId()) // 필드명과 일치!
                .aiReview(quizResult.getAiReview())
                .build();
    }

}
