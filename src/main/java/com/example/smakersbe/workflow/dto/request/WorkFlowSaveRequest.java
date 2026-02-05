package com.example.smakersbe.workflow.dto.request;

import com.example.smakersbe.workflow.dto.response.WorkFlowDetailResponse;

public record WorkFlowSaveRequest(
        String name,
        WorkFlowDetailResponse.WorkFlowData data,
        Long revision
) {}
