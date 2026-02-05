package com.example.smakersbe.workflow.controller;

import com.example.smakersbe.user.entity.User;
import com.example.smakersbe.user.service.UserResolveService;
import com.example.smakersbe.workflow.dto.response.NodeFileResponse;
import com.example.smakersbe.workflow.service.NodeFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Workflow Node File", description = "워크플로우 파일(PDF,PNG) CRUD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workflows")
public class NodeFileController {

    private final NodeFileService nodeFileService;
    private final UserResolveService userResolveService;

    private User resolveUser(String uuid) {
        if (uuid == null || uuid.isBlank())
            throw new IllegalArgumentException("X-USER-UUID header is required");
        return userResolveService.getOrCreateByUuid(uuid);
    }

    @Operation(summary = "노드 파일 목록 조회 (workflowId + clientNodeId)")
    @GetMapping("/{workflowId}/nodes/{clientNodeId}/files")
    public ResponseEntity<List<NodeFileResponse>> list(
            @Parameter(
                    description = "브라우저 스토리지 UUID",
                    required = true,
                    example = "test-uuid-001"
            )
            @RequestHeader("X-USER-UUID") String uuid,

            @Parameter(example = "8")
            @PathVariable Long workflowId,

            @Parameter(example = "node-1")
            @PathVariable String clientNodeId
    ) {
        User user = resolveUser(uuid);
        return ResponseEntity.ok(
                nodeFileService.list(user.getUserId(), workflowId, clientNodeId)
        );
    }

    @Operation(summary = "노드에 파일 업로드 (PDF/PNG)")
    @PostMapping(
            value = "/{workflowId}/nodes/{clientNodeId}/files",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<NodeFileResponse> upload(
            @Parameter(example = "test-uuid-001")
            @RequestHeader("X-USER-UUID") String uuid,

            @Parameter(example = "8")
            @PathVariable Long workflowId,

            @Parameter(example = "node-1")
            @PathVariable String clientNodeId,

            @Parameter(description = "업로드 파일(PDF/PNG)")
            @RequestPart MultipartFile file,

            @Parameter(description = "표시 이름(선택)")
            @RequestPart(required = false) String name
    ) {
        User user = resolveUser(uuid);
        return ResponseEntity.ok(
                nodeFileService.upload(user.getUserId(), workflowId, clientNodeId, file, name)
        );
    }

    @Operation(summary = "노드 파일 수정 (이름 변경 / 파일 교체)")
    @PatchMapping(
            value = "/{workflowId}/nodes/{clientNodeId}/files/{nodeFileId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<NodeFileResponse> update(
            @Parameter(example = "test-uuid-001")
            @RequestHeader("X-USER-UUID") String uuid,

            @Parameter(example = "8")
            @PathVariable Long workflowId,

            @Parameter(example = "node-1")
            @PathVariable String clientNodeId,

            @Parameter(example = "1")
            @PathVariable Long nodeFileId,

            @RequestPart(required = false) MultipartFile file,
            @RequestPart(required = false) String name
    ) {
        User user = resolveUser(uuid);
        return ResponseEntity.ok(
                nodeFileService.update(
                        user.getUserId(),
                        workflowId,
                        clientNodeId,
                        nodeFileId,
                        file,
                        name
                )
        );
    }

    @Operation(summary = "노드 파일 삭제")
    @DeleteMapping("/{workflowId}/nodes/{clientNodeId}/files/{nodeFileId}")
    public ResponseEntity<Void> delete(
            @Parameter(example = "test-uuid-001")
            @RequestHeader("X-USER-UUID") String uuid,

            @Parameter(example = "8")
            @PathVariable Long workflowId,

            @Parameter(example = "node-1")
            @PathVariable String clientNodeId,

            @Parameter(example = "1")
            @PathVariable Long nodeFileId
    ) {
        User user = resolveUser(uuid);
        nodeFileService.delete(
                user.getUserId(),
                workflowId,
                clientNodeId,
                nodeFileId
        );
        return ResponseEntity.noContent().build();
    }
}
