package com.example.smakersbe.asset.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PartDetailResponse {
    private Long partId;
    private String partName;
    private String partGlbUrl;
    private String partThumbnailUrl;
    private String partDescription;
    private String material;
    private Rotation rotation;

    @Getter
    @Builder
    public static class Rotation {
        private double x;
        private double y;
        private double z;
    }
}
