package com.example.smakersbe.asset.repository;

import com.example.smakersbe.asset.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    Optional<Asset> findByAssetName(String assetName);
}