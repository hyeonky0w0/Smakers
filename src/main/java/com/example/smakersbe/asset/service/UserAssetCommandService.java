package com.example.smakersbe.asset.service;

import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.asset.entity.UserAsset;
import com.example.smakersbe.asset.repository.AssetRepository;
import com.example.smakersbe.asset.repository.UserAssetRepository;
import com.example.smakersbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserAssetCommandService {

    private final UserAssetRepository userAssetRepository;
    private final AssetRepository assetRepository;

    @Transactional
    public void touchAsset(User user, Long assetId) {

        // asset 존재 확인 (없으면 404가 더 맞지만, 일단 예외로 터짐)
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found: " + assetId));

        UserAsset userAsset = userAssetRepository
                .findByUser_UserIdAndAsset_AssetId(user.getUserId(), assetId)
                .orElseGet(() -> UserAsset.builder()
                        .user(user)
                        .asset(asset)
                        .build()
                );

        userAsset.setLastAccessedAt(LocalDateTime.now());
        userAssetRepository.save(userAsset);
    }
}
