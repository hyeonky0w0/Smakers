package com.example.smakersbe.asset.controller;

import com.example.smakersbe.asset.dto.request.MemoCreateRequest;
import com.example.smakersbe.asset.dto.request.MemoUpdateRequest;
import com.example.smakersbe.asset.dto.response.MemoResponse;
import com.example.smakersbe.asset.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Tag(name = "Study Memo", description = "스터디 메모 CRUD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assets/{assetId}/memos")
public class MemoController {

    private final MemoService memoService;

    @Operation(
            summary = "스터디 메모 리스트 조회",
            description = "특정 에셋(assetId)에 속한 메모 목록을 조회합니다."
    )
    @GetMapping
    public List<MemoResponse> getMemos(
            @Parameter(description = "에셋 ID", example = "1", required = true)
            @PathVariable Long assetId
    ) {
        return memoService.getMemos(assetId);
    }


    @Operation(
            summary = "스터디 메모 작성",
            description = "특정 에셋(assetId)에 메모를 생성합니다."
    )
    @PostMapping
    public MemoResponse createMemo(
            @Parameter(description = "에셋 ID", example = "1", required = true)
            @PathVariable Long assetId,
            @RequestBody(
                    required = true,
                    description = "메모 생성 요청 바디",
                    content = @Content(schema = @Schema(implementation = MemoCreateRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody MemoCreateRequest req
    ) {
        return memoService.createMemo(assetId, req);
    }


    @Operation(
            summary = "스터디 메모 수정",
            description = "memoId의 메모를 수정합니다. 요청 필드가 null이면 해당 필드는 수정하지 않습니다. (부분 수정)"
    )
    @PatchMapping("/{memoId}")
    public MemoResponse updateMemo(
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
        return memoService.updateMemo(assetId, memoId, req);
    }

    @Operation(
            summary = "스터디 메모 삭제(소프트 삭제)",
            description = "memoId의 메모를 deletedAt=now 로 처리합니다. (DB에서 실제 delete 아님)"
    )
    @DeleteMapping("/{memoId}")
    public void deleteMemo(
            @Parameter(description = "에셋 ID", example = "1", required = true)
            @PathVariable Long assetId,
            @Parameter(description = "메모 ID", example = "1", required = true)
            @PathVariable Long memoId
    ) {
        memoService.deleteMemo(assetId, memoId);
    }
}
