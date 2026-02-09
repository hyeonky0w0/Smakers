package com.example.smakersbe.asset.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "seed_migration")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeedMigration {
    @Id
    private String id; // 예: "part-migration-20260209"
    private LocalDateTime appliedAt;
}
