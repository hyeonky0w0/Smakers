package com.example.smakersbe.workflow.dto.response;

public record WorkFlowDetailResponse(
        Long workflowId,
        String workflowName,
        WorkFlowData data,
        Long revision
) {
    public record WorkFlowData(
            java.util.List<NodeDto> nodes,
            java.util.List<EdgeDto> edges
    ) {}
    public record NodeDto(
            String id,          // clientNodeId
            String name,
            String content,
            Float positionX,
            Float positionY
    ) {}
    public record EdgeDto(
            String id,          // clientEdgeId
            String source,      // start clientNodeId
            String target       // end clientNodeId
    ) {}
}
