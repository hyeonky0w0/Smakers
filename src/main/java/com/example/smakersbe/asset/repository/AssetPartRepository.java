package com.example.smakersbe.asset.repository;

import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.asset.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetPartRepository extends JpaRepository<Part, Long> {

    List<Part> findAllByAsset(Asset asset);
}
