package com.example.smakersbe.workflow.dto.response;

import java.time.LocalDateTime;

public record WorkFlowListItemResponse(
        Long workflowId,
        String workflowName,
        Integer schemaVersion,
        LocalDateTime createdAt, // 추가
        LocalDateTime updatedAt
) {}
