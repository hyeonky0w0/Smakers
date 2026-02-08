package com.example.smakersbe.report.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectedMemoData {
    private String memoTitle;
    private String memoContents;
}
