package com.example.smakersbe.asset.service;

import com.example.smakersbe.asset.dto.request.MemoCreateRequest;
import com.example.smakersbe.asset.dto.request.MemoUpdateRequest;
import com.example.smakersbe.asset.dto.response.MemoResponse;
import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.asset.entity.Memo;
import com.example.smakersbe.asset.repository.AssetRepository;
import com.example.smakersbe.asset.repository.MemoRepository;
import com.example.smakersbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemoService {

    private final MemoRepository memoRepository;
    private final AssetRepository assetRepository;

    @Transactional(readOnly = true)
    public List<MemoResponse> getMemos(Long userId, Long assetId) {
        return memoRepository
                .findByUser_UserIdAndAsset_AssetIdAndDeletedAtIsNullOrderByIsImportantDescUpdatedAtDesc(userId, assetId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MemoResponse createMemo(User user, Long assetId, MemoCreateRequest req) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found: " + assetId));

        if (req.getMemoTitle() == null || req.getMemoTitle().isBlank())
            throw new IllegalArgumentException("memoTitle is required");
        if (req.getMemoContents() == null || req.getMemoContents().isBlank())
            throw new IllegalArgumentException("memoContents is required");

        Memo memo = Memo.builder()
                .memoTitle(req.getMemoTitle())
                .memoContents(req.getMemoContents())
                .isImportant(req.getIsImportant() != null && req.getIsImportant())
                .asset(asset)
                .user(user) // ✅ 작성자 연결
                .build();

        return toResponse(memoRepository.save(memo));
    }

    public MemoResponse updateMemo(Long userId, Long assetId, Long memoId, MemoUpdateRequest req) {
        Memo memo = memoRepository
                .findByMemoIdAndUser_UserIdAndAsset_AssetIdAndDeletedAtIsNull(memoId, userId, assetId)
                .orElseThrow(() -> new IllegalArgumentException("Memo not found: " + memoId));

        // 부분 수정
        if (req.getMemoTitle() != null) memo.setMemoTitle(req.getMemoTitle());
        if (req.getMemoContents() != null) memo.setMemoContents(req.getMemoContents());
        if (req.getIsImportant() != null) memo.setIsImportant(req.getIsImportant());

        // @PreUpdate로 updatedAt 갱신
        return toResponse(memo);
    }

    public void deleteMemo(Long userId, Long assetId, Long memoId) {
        Memo memo = memoRepository
                .findByMemoIdAndUser_UserIdAndAsset_AssetIdAndDeletedAtIsNull(memoId, userId, assetId)
                .orElseThrow(() -> new IllegalArgumentException("Memo not found: " + memoId));

        memo.setDeletedAt(LocalDateTime.now());
    }

    private MemoResponse toResponse(Memo memo) {
        return MemoResponse.builder()
                .memoId(memo.getMemoId())
                .assetId(memo.getAsset().getAssetId())
                .memoTitle(memo.getMemoTitle())
                .memoContents(memo.getMemoContents())
                .isImportant(memo.getIsImportant())
                .createdAt(memo.getCreatedAt())
                .updatedAt(memo.getUpdatedAt())
                .build();
    }

    public MemoResponse toggleImportant(Long userId, Long assetId, Long memoId) {
        Memo memo = memoRepository
                .findByMemoIdAndUser_UserIdAndAsset_AssetIdAndDeletedAtIsNull(memoId, userId, assetId)
                .orElseThrow(() -> new IllegalArgumentException("Memo not found: " + memoId));

        memo.setIsImportant(!Boolean.TRUE.equals(memo.getIsImportant())); // null-safe
        return toResponse(memo); // @PreUpdate로 updatedAt 갱신
    }

}
