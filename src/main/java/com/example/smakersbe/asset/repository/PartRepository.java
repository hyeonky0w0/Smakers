package com.example.smakersbe.asset.repository;

import com.example.smakersbe.asset.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartRepository extends JpaRepository<Part, Long> {
    List<Part> findAllByAsset_AssetId(Long assetId);



    List<Part> findByAsset_AssetIdOrderByPartIdAsc(Long assetId);

    Optional<Part> findByAsset_AssetIdAndPartId(Long assetId, Long partId);

}

