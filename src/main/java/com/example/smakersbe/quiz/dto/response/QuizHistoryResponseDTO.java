package com.example.smakersbe.quiz.dto.response;

import com.example.smakersbe.quiz.entity.QuizAttempt;
import com.example.smakersbe.quiz.entity.QuizResult;
import com.example.smakersbe.quiz.entity.QuizUserAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizHistoryResponseDTO {
    private Long attemptId;
    private String formattedDate;
    private Long correctCount;
    private Long totalCount;
    private String aiReview;
    private List<Long> wrongQuestionNumbers;
    private Long quizSetId;


    public static QuizHistoryResponseDTO from(QuizAttempt attempt, QuizResult result, List<QuizUserAnswer> wrongAnswers) {
        // null 일 때 사용할 값
        boolean hasResult = (result != null);

        return QuizHistoryResponseDTO.builder()
                .attemptId(attempt.getQuizAttemptId())
                .quizSetId(attempt.getQuizSet().getQuizSetId())
                .correctCount(hasResult ? result.getCorrectCount() : 0L)
                .totalCount(hasResult ? result.getTotalCount() : 0L)
                .formattedDate(attempt.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy MM/dd hh:mma", Locale.ENGLISH)))                .aiReview(hasResult ? result.getAiReview() : "현재 AI 분석 결과를 생성 중입니다.")
                .wrongQuestionNumbers(wrongAnswers.stream()
                        .map(answer -> answer.getQuizSetItem().getQuizSetItemId())
                        .toList())
                .build();
    }
}
