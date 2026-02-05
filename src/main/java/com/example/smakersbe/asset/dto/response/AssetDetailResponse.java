package com.example.smakersbe.asset.dto.response;

import java.util.List;

public record AssetDetailResponse(
        Long assetId,
        String modelName,          // 전체 모델명
        String modelDescription,   // 모델 설명
        List<PartItem> parts       // 부품들
) {
    public record PartItem(
            Long partId,
            String partName,
            String partDescription,
            String partThumbnailUrl, // 부품 사진
            String partGlbUrl, // 부품 Glb Url
            String material,
            // 분해도: 조립 완료 / 조립 전(분해 완료) 좌표만
            Position assembled,
            Position exploded,
            Position rotation
    ) {}

    public record Position(double x, double y, double z) {}
}
