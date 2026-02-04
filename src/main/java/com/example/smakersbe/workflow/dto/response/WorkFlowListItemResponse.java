package com.example.smakersbe.workflow.dto.response;

import java.time.LocalDateTime;

public record WorkFlowListItemResponse(
        Long workflowId,
        String workflowName,
        LocalDateTime updatedAt
) {}
