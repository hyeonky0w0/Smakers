package com.example.smakersbe.report.entity;

import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reports")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="report_id", nullable = false, updatable = false)
    private Long reportId;

    @Column(name="report_url",length = 1000, nullable = false)
    private String reportUrl;

    @Column(name="report_name", nullable = false)
    private String reportName;

    @Column(name="file_size", nullable = false)
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="asset_id", nullable = false)
    private Asset asset;
}
