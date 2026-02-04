package com.example.smakersbe.asset.dto.request;

import lombok.Data;

@Data
public class MemoUpdateRequest {
    private String memoTitle;     // null이면 수정 안함
    private String memoContents;  // null이면 수정 안함
    private Boolean isImportant;  // null이면 수정 안함
}