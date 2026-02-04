package com.example.smakersbe.asset.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MemoResponse {
    private Long memoId;
    private Long assetId;
    private String memoTitle;
    private String memoContents; // 상세에서 그대로 쓰고 싶으면 포함
    private Boolean isImportant;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}