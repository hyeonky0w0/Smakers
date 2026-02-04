package com.example.smakersbe.asset.service;

import com.example.smakersbe.asset.dto.response.PartDetailResponse;
import com.example.smakersbe.asset.entity.Part;

import com.example.smakersbe.asset.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartService {

    private final PartRepository partRepository;

    @Transactional(readOnly = true)
    public PartDetailResponse getPartDetail(Long assetId, Long partId) {
        Part part = partRepository.findByAsset_AssetIdAndPartId(assetId, partId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Part not found. assetId=" + assetId + ", partId=" + partId
                ));

        return PartDetailResponse.builder()
                .partId(part.getPartId())
                .partName(part.getPartName())
                .partGlbUrl(part.getPartGlbUrl())
                .partThumbnailUrl(part.getPartThumbnailUrl())
                .partDescription(part.getPartDescription())
                .material(part.getMaterial())
                .build();
    }
}
