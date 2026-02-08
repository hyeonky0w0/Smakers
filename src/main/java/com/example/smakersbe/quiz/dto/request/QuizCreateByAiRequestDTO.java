package com.example.smakersbe.quiz.dto.request;


import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.asset.entity.Memo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizCreateByAiRequestDTO {

    private Long assetId;
    private String memoContents;

}
