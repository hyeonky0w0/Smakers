package com.example.smakersbe.asset.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "parts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="part_id", nullable = false, updatable = false)
    private Long partId;

    @Column(name="part_name", nullable = false)
    private String partName;

    @Column(name="part_description", columnDefinition = "TEXT", nullable = false)
    private String partDescription;

    // 부품 썸네일(캡처 이미지)
    @Column(name="part_thumbnail_url", length = 1000, nullable = false)
    private String partThumbnailUrl;

    // 부품 GLB
    @Column(name="part_glb_url", length = 1000, nullable = false)
    private String partGlbUrl;

    // 소재(표에 있는 "금속/고무/강철..." 저장용) , ?라고 써진 것도 있어서 nullable로 했습니다
    @Column(name="material", length = 50)
    private String material;

    // 조립 완료 좌표
    @Column(name="assembled_x", nullable = false)
    private double assembledX;
    @Column(name="assembled_y", nullable = false)
    private double assembledY;
    @Column(name="assembled_z", nullable = false)
    private double assembledZ;

    // 조립 전 (분해 완료) 좌표
    @Column(name="exploded_x", nullable = false)
    private double explodedX;
    @Column(name="exploded_y", nullable = false)
    private double explodedY;
    @Column(name="exploded_z", nullable = false)
    private double explodedZ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="asset_id", nullable = false)
    private Asset asset;

}
