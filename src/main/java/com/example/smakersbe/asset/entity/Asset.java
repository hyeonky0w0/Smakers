package com.example.smakersbe.asset.entity;
import com.example.smakersbe.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="asset_id", nullable = false, updatable = false)
    private Long assetId;

    @Column(name="asset_name", nullable = false)
    private String assetName;

    @Column(name="asset_description",  columnDefinition = "TEXT", nullable = false)
    private String assetDescription;

    // 모델 썸네일(이미지)
    @Column(name="asset_thumbnail_url", length = 1000, nullable = false)
    private String assetThumbnailUrl;

    // 전체 모델 GLB
    @Column(name="asset_glb_url", length = 1000, nullable = false)
    private String assetGlbUrl;

    // 퀴즈 생성 여부
    @Column(nullable = false)
    private boolean isQuizCreating = false;
    // 퀴즈 생성하면 true -> false 로 상태 바꾸기
    public void updateQuizCreatingStatus(boolean status) {
        this.isQuizCreating = status;
    }

}
