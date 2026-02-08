package com.example.smakersbe.asset.entity;

import com.example.smakersbe.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_assets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_asset_id", nullable = false, updatable = false)
    private Long userAssetId;

    @Column(name="last_accessed_at", nullable = false)
    private LocalDateTime lastAccessedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="asset_id", nullable = false)
    private Asset asset;
}
