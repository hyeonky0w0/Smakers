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

    @Column(name="part_url", length = 1000, nullable = false)
    private String partUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="asset_id", nullable = false)
    private Asset asset;

}
