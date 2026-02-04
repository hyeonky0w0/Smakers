package com.example.smakersbe.asset.dto.response;

import java.util.List;

public record AssetDetailResponse(
        Long assetId,
        String modelName,          // 전체 모델명
        String modelDescription,   // 모델 설명
        String modelGlbUrl,        // 모델 3D glb
        List<PartItem> parts       // 부품들
) {
    public record PartItem(
            Long partId,
            String partName,
            String partDescription,
            String partThumbnailUrl, // 부품 사진
            String material,
            // 분해도: 조립 완료 / 조립 전(분해 완료) 좌표만
            Position assembled,
            Position exploded
    ) {}

    public record Position(double x, double y, double z) {}
}
