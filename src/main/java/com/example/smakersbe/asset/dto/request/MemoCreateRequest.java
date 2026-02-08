package com.example.smakersbe.asset.dto.request;

import lombok.Data;

@Data
public class MemoCreateRequest {
    private String memoTitle;
    private String memoContents;
    private Boolean isImportant; // null 허용
}
