package com.example.smakersbe.asset.controller;

import com.example.smakersbe.asset.dto.request.MemoCreateRequest;
import com.example.smakersbe.asset.dto.request.MemoUpdateRequest;
import com.example.smakersbe.asset.dto.response.MemoResponse;
import com.example.smakersbe.asset.service.MemoService;
import com.example.smakersbe.user.entity.User;
import com.example.smakersbe.user.service.UserResolveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Tag(name = "Study Memo", description = "유저별 스터디 메모 CRUD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assets/{assetId}/memos")
public class MemoController {

    private final MemoService memoService;
    private final UserResolveService userResolveService;

    @Operation(summary = "내 메모 리스트 조회", description = "특정 에셋(assetId)에 대해 로그인 유저의 메모 목록만 조회합니다.")
    @GetMapping
    public List<MemoResponse> getMemos(
            @Parameter(description = "사용자 식별 UUID", example = "test-uuid-001", required = true)
            @RequestHeader("X-USER-UUID") String uuid,
            @Parameter(description = "에셋 ID", example = "1", required = true)
            @PathVariable Long assetId
    ) {
        User user = userResolveService.getOrCreateByUuid(uuid);
        return memoService.getMemos(user.getUserId(), assetId);
    }

    @Operation(summary = "내 메모 작성", description = "특정 에셋(assetId)에 대해 로그인 유저의 메모를 생성합니다.")
    @PostMapping
    public MemoResponse createMemo(
            @Parameter(description = "사용자 식별 UUID", example = "test-uuid-001", required = true)
            @RequestHeader("X-USER-UUID") String uuid,
            @Parameter(description = "에셋 ID", example = "1", required = true)
            @PathVariable Long assetId,
            @RequestBody(
                    required = true,
                    description = "메모 생성 요청 바디",
                    content = @Content(schema = @Schema(implementation = MemoCreateRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody MemoCreateRequest req
    ) {
        User user = userResolveService.getOrCreateByUuid(uuid);
        return memoService.createMemo(user, assetId, req);
    }

    @Operation(summary = "내 메모 수정", description = "memoId의 메모를 수정합니다. (내 메모만 가능)")
    @PatchMapping("/{memoId}")
    public MemoResponse updateMemo(
            @Parameter(description = "사용자 식별 UUID", example = "test-uuid-001", required = true)
            @RequestHeader("X-USER-UUID") String uuid,
            @Parameter(description = "에셋 ID", example = "1", required = true)
            @PathVariable Long assetId,
            @Parameter(description = "메모 ID", example = "1", required = true)
            @PathVariable Long memoId,
            @RequestBody(
                    required = true,
                    description = "메모 수정 요청 바디(부분 수정)",
                    content = @Content(schema = @Schema(implementation = MemoUpdateRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody MemoUpdateRequest req
    ) {
        User user = userResolveService.getOrCreateByUuid(uuid);
        return memoService.updateMemo(user.getUserId(), assetId, memoId, req);
    }

    @Operation(summary = "내 메모 삭제(소프트 삭제)", description = "memoId의 메모를 deletedAt=now 처리합니다. (내 메모만 가능)")
    @DeleteMapping("/{memoId}")
    public void deleteMemo(
            @Parameter(description = "사용자 식별 UUID", example = "test-uuid-001", required = true)
            @RequestHeader("X-USER-UUID") String uuid,
            @Parameter(description = "에셋 ID", example = "1", required = true)
            @PathVariable Long assetId,
            @Parameter(description = "메모 ID", example = "1", required = true)
            @PathVariable Long memoId
    ) {
        User user = userResolveService.getOrCreateByUuid(uuid);
        memoService.deleteMemo(user.getUserId(), assetId, memoId);
    }

    @Operation(summary = "메모 중요 표시 토글", description = "memoId의 isImportant를 true/false로 토글합니다. (내 메모만 가능)")
    @PatchMapping("/{memoId}/important")
    public MemoResponse toggleImportant(
            @RequestHeader("X-USER-UUID") String uuid,
            @PathVariable Long assetId,
            @PathVariable Long memoId
    ) {
        User user = userResolveService.getOrCreateByUuid(uuid);
        return memoService.toggleImportant(user.getUserId(), assetId, memoId);
    }

}
