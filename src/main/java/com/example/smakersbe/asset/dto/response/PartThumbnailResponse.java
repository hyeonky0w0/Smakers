package com.example.smakersbe.asset.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PartThumbnailResponse {
    private Long partId;
    private String partName;
    private String partThumbnailUrl;
}
