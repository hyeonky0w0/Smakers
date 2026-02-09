package com.example.smakersbe.workflow.dto.response;

import java.time.LocalDateTime;

public record WorkFlowRenameResponse(
        boolean success,
        Long workflowId,
        String workflowName,
        Long revision,
        LocalDateTime updatedAt
) {}
