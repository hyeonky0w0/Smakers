package com.example.smakersbe.workflow.controller;

import com.example.smakersbe.user.entity.User;
import com.example.smakersbe.user.service.UserResolveService;
import com.example.smakersbe.workflow.dto.request.WorkFlowCreateRequest;
import com.example.smakersbe.workflow.dto.request.WorkFlowSaveRequest;
import com.example.smakersbe.workflow.dto.response.WorkFlowDetailResponse;
import com.example.smakersbe.workflow.dto.response.WorkFlowListItemResponse;
import com.example.smakersbe.workflow.dto.response.WorkFlowSaveResponse;
import com.example.smakersbe.workflow.service.WorkFlowService;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import com.example.smakersbe.workflow.dto.request.WorkFlowRenameRequest;
import com.example.smakersbe.workflow.dto.response.WorkFlowRenameResponse;

@Tag(name = "Workflow", description = "워크플로우(문서) CRUD + Autosave API")
@RestController
@RequestMapping("/api/workflows")
@RequiredArgsConstructor
public class WorkFlowController {

    private final WorkFlowService workFlowService;
    private final UserResolveService userResolveService;

    private User resolveUser(String uuid) {
        if (uuid == null || uuid.isBlank()) {
            throw new IllegalArgumentException("X-USER-UUID header is required");
        }
        return userResolveService.getOrCreateByUuid(uuid);
    }

    @Operation(
            summary = "워크플로우 이름 변경",
            description = """
            워크플로우 이름만 변경합니다.
            - 전체 autosave(PUT) 없이 이름만 바꾸고 싶을 때 사용합니다.
            - revision을 함께 보내 충돌(409)을 방지합니다.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이름 변경 성공"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Revision 충돌",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                        {
                          "status": 409,
                          "error": "CONFLICT",
                          "message": "Revision conflict. Please reload workflow and try again."
                        }
                        """)
                    )
            )
    })
    @PatchMapping("/{workflowId}/name")
    public ResponseEntity<WorkFlowRenameResponse> rename(
            @Parameter(
                    description = "브라우저 스토리지 UUID (로그인 없이 유저 식별)",
                    required = true,
                    example = "test-uuid-001"
            )
            @RequestHeader("X-USER-UUID") String uuid,

            @Parameter(description = "워크플로우 ID", required = true, example = "1")
            @PathVariable Long workflowId,

            @RequestBody(
                    required = true,
                    description = "이름 변경 요청 바디",
                    content = @Content(
                            schema = @Schema(implementation = WorkFlowRenameRequest.class),
                            examples = @ExampleObject(
                                    name = "Rename Example",
                                    summary = "이름 변경 예시",
                                    value = """
                                {
                                  "name": "Renamed Workflow",
                                  "revision": 3
                                }
                                """
                            )
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody WorkFlowRenameRequest req
    ) {
        User user = resolveUser(uuid);
        return ResponseEntity.ok(workFlowService.rename(user.getUserId(), workflowId, req));
    }


    // 워크플로우 리스트 조회
    @Operation(
            summary = "워크플로우 썸네일(목록) 조회",
            description = "유저(UUID) 기준으로 워크플로우 목록을 조회합니다. 응답에는 workflowId, workflowName, updatedAt이 포함됩니다."
    )
    @GetMapping
    public ResponseEntity<List<WorkFlowListItemResponse>> list(
            @Parameter(
                    description = "브라우저 스토리지 UUID (로그인 없이 유저 식별). 예: localStorage uuid",
                    required = true,
                    example = "test-uuid-001"
            )
            @RequestHeader("X-USER-UUID") String uuid
    ) {
        User user = resolveUser(uuid);
        return ResponseEntity.ok(workFlowService.list(user.getUserId()));
    }

    // 워크플로우 상세 조회
    @Operation(
            summary = "워크플로우 상세 조회",
            description = "workflowId에 해당하는 워크플로우의 상세 데이터(nodes, edges)와 revision을 조회합니다."
    )
    @GetMapping("/{workflowId}")
    public ResponseEntity<WorkFlowDetailResponse> detail(
            @Parameter(
                    description = "브라우저 스토리지 UUID (로그인 없이 유저 식별)",
                    required = true,
                    example = "test-uuid-001"
            )
            @RequestHeader("X-USER-UUID") String uuid,
            @Parameter(description = "워크플로우 ID", required = true, example = "1")
            @PathVariable Long workflowId
    ) {
        User user = resolveUser(uuid);
        return ResponseEntity.ok(workFlowService.detail(user.getUserId(), workflowId));
    }

    @Operation(
            summary = "워크플로우 생성",
            description = "워크플로우 문서를 생성합니다. nodes/edges는 빈 배열로 시작해도 됩니다."
    )
    @PostMapping
    public ResponseEntity<WorkFlowDetailResponse> create(
            @Parameter(
                    description = "브라우저 스토리지 UUID",
                    required = true,
                    example = "test-uuid-001"
            )
            @RequestHeader("X-USER-UUID") String uuid,

            @RequestBody(
                    required = true,
                    description = "워크플로우 생성 요청 바디",
                    content = @Content(
                            schema = @Schema(implementation = WorkFlowCreateRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Create Workflow Example",
                                            summary = "빈 워크플로우 생성 예시",
                                            value = """
                                        {
                                          "name": "New Workflow",
                                          "data": {
                                            "nodes": [],
                                            "edges": []
                                          }
                                        }
                                        """
                                    )
                            }
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody WorkFlowCreateRequest req
    ) {
        User user = resolveUser(uuid);
        return ResponseEntity.ok(workFlowService.create(user.getUserId(), req, user));
    }


    @Operation(
            summary = "워크플로우 삭제",
            description = "workflowId에 해당하는 워크플로우 문서를 삭제합니다. (현재는 hard delete)"
    )
    @DeleteMapping("/{workflowId}")
    public ResponseEntity<Void> delete(
            @Parameter(
                    description = "브라우저 스토리지 UUID (로그인 없이 유저 식별)",
                    required = true,
                    example = "test-uuid-001"
            )
            @RequestHeader("X-USER-UUID") String uuid,

            @Parameter(description = "워크플로우 ID", required = true, example = "1")
            @PathVariable Long workflowId
    ) {
        User user = resolveUser(uuid);
        workFlowService.delete(user.getUserId(), workflowId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "워크플로우 저장(Autosave 전체 PUT)",
            description = """
                워크플로우 문서 전체를 저장(덮어쓰기)합니다.
                - nodes / edges는 현재 캔버스 상태 전체를 보냅니다.
                - revision이 서버 값과 다르면 충돌(409)이 발생할 수 있습니다.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "저장 성공"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Revision 충돌 (다른 세션에서 먼저 수정됨)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                    {
                      "status": 409,
                      "error": "CONFLICT",
                      "message": "Revision conflict. Please reload workflow and try again."
                    }
                    """)
                    )
            )
    })
    @PutMapping("/{workflowId}")
    public ResponseEntity<WorkFlowSaveResponse> save(
            @Parameter(
                    description = "브라우저 스토리지 UUID (로그인 없이 유저 식별)",
                    required = true,
                    example = "test-uuid-001"
            )
            @RequestHeader("X-USER-UUID") String uuid,

            @Parameter(description = "워크플로우 ID", required = true, example = "1")
            @PathVariable Long workflowId,

            @RequestBody(
                    required = true,
                    description = "워크플로우 저장 요청 바디 (전체 덮어쓰기)",
                    content = @Content(
                            schema = @Schema(implementation = WorkFlowSaveRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Autosave Example",
                                            summary = "워크플로우 autosave 요청 예시",
                                            value = """
                                        {
                                          "name": "My Workflow",
                                          "data": {
                                            "nodes": [
                                              {
                                                "id": "node-1",
                                                "name": "Start",
                                                "content": "시작 노드",
                                                "positionX": 100,
                                                "positionY": 200
                                              },
                                              {
                                                "id": "node-2",
                                                "name": "Process",
                                                "content": "처리 노드",
                                                "positionX": 300,
                                                "positionY": 200
                                              }
                                            ],
                                            "edges": [
                                              {
                                                "id": "edge-1",
                                                "source": "node-1",
                                                "target": "node-2"
                                              }
                                            ]
                                          },
                                          "revision": 1
                                        }
                                        """
                                    )
                            }
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody WorkFlowSaveRequest req
    ) {
        User user = resolveUser(uuid);
        return ResponseEntity.ok(workFlowService.save(user.getUserId(), workflowId, req));
    }

}
