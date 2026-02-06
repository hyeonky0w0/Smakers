package com.example.smakersbe.ai.repository;

import com.example.smakersbe.ai.entity.AiChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiChatRepository extends JpaRepository<AiChat, Long> {
    // 회원의 에셋 대화 목록 조회
    List<AiChat> findAllByAsset_AssetIdAndUser_UuidOrderByCreatedAtDesc(Long assetId, String uuid);

    List<AiChat> findByUser_UserIdAndAsset_AssetIdAndIsImportantTrueOrderByCreatedAtAsc(Long userId, Long assetId);

    List<AiChat> findByUser_UserIdAndAsset_AssetIdOrderByCreatedAtAsc(Long userId, Long assetId);
}
