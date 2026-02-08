package com.example.smakersbe.workflow.dto.request;

import com.example.smakersbe.workflow.dto.response.WorkFlowDetailResponse;

public record WorkFlowCreateRequest(
        String name,
        WorkFlowDetailResponse.WorkFlowData data
) {}

