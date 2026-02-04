package com.example.smakersbe.asset.dto;

import java.time.LocalDateTime;

public record AssetThumbnailResponse(
        Long assetId,
        String assetName,
        String assetDescription,
        String assetUrl,
        LocalDateTime lastAccessedAt
) {}