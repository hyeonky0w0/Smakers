package com.example.smakersbe.workflow.dto.response;

import java.time.LocalDateTime;

public record WorkFlowSaveResponse(
        boolean ok,
        Long revision,
        LocalDateTime updatedAt
) {}
