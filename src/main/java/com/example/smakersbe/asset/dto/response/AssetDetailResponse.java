package com.example.smakersbe.asset.dto.response;

import java.util.List;

public record AssetDetailResponse(
        Long assetId,
        String assetName,
        String assetDescription,
        String assetThumbnailUrl,

        // ✅ 추가: 전체 모델 GLB 2종
        String assetExplodedGlbUrl,
        String assetAssembledGlbUrl,

        // ✅ 추가: Asset 기준 position/rotation
        Vec3Dto position,
        Vec3Dto rotation,

        // ✅ Part는 메타 + glb만
        List<PartItem> parts
) {
    public record PartItem(
            Long partId,
            String partName,
            String partDescription,
            String partThumbnailUrl,
            String partGlbUrl,
            String material
    ) {}

    public record Vec3Dto(double x, double y, double z) {}
}
