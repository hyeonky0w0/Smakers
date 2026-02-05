package com.example.smakersbe.asset.repository;

import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.asset.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    List<Memo> findByAsset_AssetIdAndDeletedAtIsNullOrderByIsImportantDescUpdatedAtDesc(Long assetId);

    Optional<Memo> findByMemoIdAndAsset_AssetIdAndDeletedAtIsNull(Long memoId, Long assetId);

    List<Memo> findTop3ByAssetOrderByCreatedAtDesc(Asset asset);
}
