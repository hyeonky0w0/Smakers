package com.example.smakersbe.asset.service;

import com.example.smakersbe.asset.dto.response.AssetDetailResponse;
import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.asset.entity.Part;
import com.example.smakersbe.asset.repository.AssetRepository;
import com.example.smakersbe.asset.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetDetailService {

    private final AssetRepository assetRepository;
    private final PartRepository partRepository;

    @Transactional(readOnly = true)
    public AssetDetailResponse getAssetDetail(Long assetId) {

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found: " + assetId));

        List<Part> parts = partRepository.findAllByAsset_AssetId(assetId);

        List<AssetDetailResponse.PartItem> partItems = parts.stream()
                .map(p -> new AssetDetailResponse.PartItem(
                        p.getPartId(),
                        p.getPartName(),
                        p.getPartDescription(),
                        p.getPartThumbnailUrl(),
                        p.getMaterial(),
                        new AssetDetailResponse.Position(
                                p.getAssembledX(), p.getAssembledY(), p.getAssembledZ()
                        ),
                        new AssetDetailResponse.Position(
                                p.getExplodedX(), p.getExplodedY(), p.getExplodedZ()
                        )
                ))
                .toList();

        return new AssetDetailResponse(
                asset.getAssetId(),
                asset.getAssetName(),
                asset.getAssetDescription(),
                asset.getAssetGlbUrl(),
                partItems
        );
    }
}
