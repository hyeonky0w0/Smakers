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
}
