package com.example.smakersbe.quiz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizAttemptRequestDTO {

    private String uuid;
    private Long quizSetId;
    private List<UserAnswerDTO> answers;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserAnswerDTO{
        private Long quizSetItemId;   // 퀴즈 하나 당
        private Long userChoice;      // 제출한 답
    }

}
