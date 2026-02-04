package com.example.smakersbe.asset.controller;

import com.example.smakersbe.asset.dto.response.AssetThumbnailResponse;
import com.example.smakersbe.asset.service.AssetQueryService;
import com.example.smakersbe.asset.service.UserAssetCommandService;
import com.example.smakersbe.user.entity.User;
import com.example.smakersbe.user.service.UserResolveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assets")
@Tag(name = "Asset", description = "에셋 관련 API")
public class AssetController {

    private final UserResolveService userResolveService;
    private final AssetQueryService assetQueryService;
    private final UserAssetCommandService userAssetCommandService;

    @Operation(summary = "스터디 썸네일 리스트 조회", description = "스터디 썸네일을 조회합니다. 에셋이름, 설명, 이미지가 제공됩니다")
    @GetMapping
    public ResponseEntity<List<AssetThumbnailResponse>> getAssets(
            @Parameter(description = "사용자 식별을 위한 UUID", example = "test-uuid-001")
            @RequestHeader("X-USER-UUID") String uuid
    ) {
        User user = userResolveService.getOrCreateByUuid(uuid);
        return ResponseEntity.ok(assetQueryService.getAssetThumbnails(user.getUserId()));
    }

    @Operation(summary = "에셋 접근 기록 업데이트", description = "특정 에셋에 접근했을 때 최근 사용 기록을 갱신합니다.")
    @PostMapping("/{assetId}/access")
    public ResponseEntity<Void> accessAsset(
            @Parameter(description = "사용자 식별을 위한 UUID", example = "test-uuid-001")
            @RequestHeader("X-USER-UUID") String uuid,

            @Parameter(description = "에셋의 고유 ID", example = "1")
            @PathVariable Long assetId
    ) {
        User user = userResolveService.getOrCreateByUuid(uuid);
        userAssetCommandService.touchAsset(user, assetId);
        return ResponseEntity.noContent().build(); // 204
    }
}
