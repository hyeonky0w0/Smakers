package com.example.smakersbe.quiz.dto.response;

import com.example.smakersbe.quiz.entity.QuizAttempt;
import com.example.smakersbe.quiz.entity.QuizSetItem;
import com.example.smakersbe.quiz.entity.QuizUserAnswer;
import com.example.smakersbe.quiz.entity.QuizResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizAttemptResponseDTO {
    private Long quizAttemptId;
    private Long quizSetId;
    private LocalDateTime createdAt;
    private Long totalCount;
    private Long correctCount;
    private List<QuizDetailResponseDTO> details; // 문제별 상세 결과

    private static final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

    public static QuizAttemptResponseDTO from(QuizAttempt attempt, List<QuizUserAnswer> answers, QuizResult result) {
        return QuizAttemptResponseDTO.builder()
                .quizAttemptId(attempt.getQuizAttemptId())
                .quizSetId(attempt.getQuizSet().getQuizSetId())
                .createdAt(result.getCreatedAt())
                .totalCount(result.getTotalCount())
                .correctCount(result.getCorrectCount())
                .details(answers.stream()
                        .map(QuizDetailResponseDTO::from)
                        .toList())
                .build();
    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuizDetailResponseDTO {
        private Long quizSetItemId;
        private String question;
        private String explanation;
        private List<String> options;
        private Long answer;
        private Long userChoice;
        private Boolean isCorrect;

        public static QuizDetailResponseDTO from(QuizUserAnswer userAnswer) {
            QuizSetItem item = userAnswer.getQuizSetItem();

            // json 파싱
            List<String> parsedOptions = new ArrayList<>();
            try {
                parsedOptions = objectMapper.readValue(item.getOptions(),
                        new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
            } catch (Exception e) {
                // 파싱 실패 시 예외 처리 (로그를 남기거나 빈 리스트 반환)
            }

            return QuizDetailResponseDTO.builder()
                    .quizSetItemId(item.getQuizSetItemId())
                    .question(item.getQuestion())
                    .explanation(item.getExplanation())
                    .options(parsedOptions)
                    .answer(item.getAnswer())
                    .userChoice(userAnswer.getUserChoice())
                    .isCorrect(userAnswer.getIsCorrect())
                    .build();
        }
    }



}