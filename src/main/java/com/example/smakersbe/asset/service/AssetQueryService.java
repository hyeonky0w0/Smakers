package com.example.smakersbe.asset.service;

import com.example.smakersbe.asset.dto.AssetThumbnailResponse;
import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.asset.entity.UserAsset;
import com.example.smakersbe.asset.repository.AssetRepository;
import com.example.smakersbe.asset.repository.UserAssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetQueryService {

    private final AssetRepository assetRepository;
    private final UserAssetRepository userAssetRepository;

    @Transactional(readOnly = true)
    public List<AssetThumbnailResponse> getAssetThumbnails(Long userId) {

        List<Asset> assets = assetRepository.findAll();

        // user의 lastAccessedAt을 assetId 기준으로 맵핑
        Map<Long, LocalDateTime> lastAccessMap = userAssetRepository.findAllByUserIdFetchAsset(userId)
                .stream()
                .collect(Collectors.toMap(
                        ua -> ua.getAsset().getAssetId(),
                        UserAsset::getLastAccessedAt,
                        (a, b) -> a.isAfter(b) ? a : b // 혹시 중복 row가 있으면 최신값 유지
                ));

        // 응답 만들기
        List<AssetThumbnailResponse> result = assets.stream()
                .map(a -> new AssetThumbnailResponse(
                        a.getAssetId(),
                        a.getAssetName(),
                        a.getAssetDescription(),
                        a.getAssetUrl(),
                        lastAccessMap.get(a.getAssetId()) // 없으면 null
                ))
                .collect(Collectors.toList());

        // 정렬: 최근 접근한 것 먼저, 접근 기록 없으면 뒤로
        result.sort(Comparator
                .comparing(AssetThumbnailResponse::lastAccessedAt,
                        Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(AssetThumbnailResponse::assetId));

        return result;
    }
}

