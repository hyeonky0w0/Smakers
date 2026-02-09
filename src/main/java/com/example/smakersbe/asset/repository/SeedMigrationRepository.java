package com.example.smakersbe.asset.repository;


import com.example.smakersbe.asset.entity.SeedMigration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeedMigrationRepository extends JpaRepository<SeedMigration, String> {}
