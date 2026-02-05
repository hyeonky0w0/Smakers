package com.example.smakersbe.quiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizzableAssetListResponseDTO {
    private String assetId;
    private String assetName;
    private String assetThumbnailUrl;
}
