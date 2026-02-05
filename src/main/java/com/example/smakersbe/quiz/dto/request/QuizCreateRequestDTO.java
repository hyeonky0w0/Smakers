package com.example.smakersbe.quiz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizCreateRequestDTO {
    private Long assetId;
    private String uuid;
    private Long quizSetId;
}
