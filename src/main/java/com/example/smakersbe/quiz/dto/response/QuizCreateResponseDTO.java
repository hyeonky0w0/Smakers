package com.example.smakersbe.quiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizCreateResponseDTO {

    private Long quizSetId;
    private Long assetId;
    private Long quizSetItemId;
    private String question;
    private List<String> options;
    private Long answer;
    private String explanation;
    private String hint;

}
