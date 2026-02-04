package com.example.smakersbe.asset.dto.response;

import java.time.LocalDateTime;

public record AssetThumbnailResponse(
        Long assetId,
        String assetName,
        String assetDescription,
        String assetThumbnailUrl,
        LocalDateTime lastAccessedAt
) {}