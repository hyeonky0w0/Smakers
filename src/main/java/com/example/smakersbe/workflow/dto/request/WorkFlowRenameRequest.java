package com.example.smakersbe.workflow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record WorkFlowRenameRequest(
        @Schema(description = "새 워크플로우 이름", example = "My Renamed Workflow")
        String name,

        @Schema(description = "현재 revision(충돌 방지용). detail 응답에서 받은 revision 그대로 보내기", example = "3")
        Long revision
) {}
