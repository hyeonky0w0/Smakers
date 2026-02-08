package com.example.smakersbe.report.dto.request;

import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequestDTO {

    private List<Long> aiChatId;
    private List<Long> memoId;

}
