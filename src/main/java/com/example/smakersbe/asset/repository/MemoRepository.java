package com.example.smakersbe.asset.repository;

import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.asset.entity.Memo;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    List<Memo> findByAsset_AssetIdAndDeletedAtIsNullOrderByIsImportantDescUpdatedAtDesc(Long assetId);

    Optional<Memo> findByMemoIdAndAsset_AssetIdAndDeletedAtIsNull(Long memoId, Long assetId);

    List<Memo> findTop3ByAssetOrderByCreatedAtDesc(Asset asset);

    List<Memo> findByUser_UserIdAndAsset_AssetIdAndDeletedAtIsNullOrderByIsImportantDescUpdatedAtDesc(
            Long userId, Long assetId
    );

    Optional<Memo> findByMemoIdAndUser_UserIdAndAsset_AssetIdAndDeletedAtIsNull(
            Long memoId, Long userId, Long assetId
    );

    List<Memo> findByUser_UserIdAndAsset_AssetIdAndIsImportantTrueAndDeletedAtIsNullOrderByCreatedAtAsc(Long userId, Long assetId);

    List<Memo> findByUser_UserIdAndAsset_AssetIdAndDeletedAtIsNullOrderByCreatedAtAsc(Long userId, Long assetId);

    // ✅ 토글(중요표시 변경) 같은 “수정 직전 조회”에 쓰는 락 메서드
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Memo> findForUpdateByMemoIdAndUser_UserIdAndAsset_AssetIdAndDeletedAtIsNull(
            Long memoId, Long userId, Long assetId
    );
}
