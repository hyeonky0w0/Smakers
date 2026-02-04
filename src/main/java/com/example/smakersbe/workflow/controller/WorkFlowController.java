package com.example.smakersbe.workflow.controller;

import com.example.smakersbe.user.entity.User;
import com.example.smakersbe.user.service.UserResolveService;
import com.example.smakersbe.workflow.dto.request.WorkFlowCreateRequest;
import com.example.smakersbe.workflow.dto.request.WorkFlowSaveRequest;
import com.example.smakersbe.workflow.dto.response.WorkFlowDetailResponse;
import com.example.smakersbe.workflow.dto.response.WorkFlowListItemResponse;
import com.example.smakersbe.workflow.dto.response.WorkFlowSaveResponse;
import com.example.smakersbe.workflow.service.WorkFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 워크플로우 리스트 조회
    @GetMapping
    public ResponseEntity<List<WorkFlowListItemResponse>> list(
            @RequestHeader("X-USER-UUID") String uuid
    ) {
        User user = resolveUser(uuid);
        return ResponseEntity.ok(workFlowService.list(user.getUserId()));
    }

    // 워크플로우 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<WorkFlowDetailResponse> detail(
            @RequestHeader("X-USER-UUID") String uuid,
            @PathVariable Long id
    ) {
        User user = resolveUser(uuid);
        return ResponseEntity.ok(workFlowService.detail(user.getUserId(), id));
    }

    // 워크플로우 생성
    @PostMapping
    public ResponseEntity<WorkFlowDetailResponse> create(
            @RequestHeader("X-USER-UUID") String uuid,
            @RequestBody WorkFlowCreateRequest req
    ) {
        User user = resolveUser(uuid);
        return ResponseEntity.ok(workFlowService.create(user.getUserId(), req, user));
    }

    // 워크플로우 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("X-USER-UUID") String uuid,
            @PathVariable Long id
    ) {
        User user = resolveUser(uuid);
        workFlowService.delete(user.getUserId(), id);
        return ResponseEntity.noContent().build();
    }

    // 워크플로우 저장(전체 PUT)
    @PutMapping("/{id}")
    public ResponseEntity<WorkFlowSaveResponse> save(
            @RequestHeader("X-USER-UUID") String uuid,
            @PathVariable Long id,
            @RequestBody WorkFlowSaveRequest req
    ) {
        User user = resolveUser(uuid);
        return ResponseEntity.ok(workFlowService.save(user.getUserId(), id, req));
    }
}
