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

    //조립 전 glb url
    @Column(name="assest_exploded_glb_url", length = 1000, nullable = false)
    private String assetExplodedGlbUrl;

    //조립 후 glb url
    @Column(name="assest_assembled_glb_url", length = 1000, nullable = false)
    private String assetAssembledGlbUrl;

    // 위치값 (기본 0,0,0)
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name="position_x", nullable = false)),
            @AttributeOverride(name = "y", column = @Column(name="position_y", nullable = false)),
            @AttributeOverride(name = "z", column = @Column(name="position_z", nullable = false))
    })
    @Builder.Default
    private Vec3 position = new Vec3(0, 0, 0);

    // 회전값 (기본 0,0,0)
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name="rotation_x", nullable = false)),
            @AttributeOverride(name = "y", column = @Column(name="rotation_y", nullable = false)),
            @AttributeOverride(name = "z", column = @Column(name="rotation_z", nullable = false))
    })
    @Builder.Default
    private Vec3 rotation = new Vec3(0, 0, 0);


    // 퀴즈 생성 여부
    @Column(name="is_quiz_creating",nullable = false)
    private boolean isQuizCreating = false;
    // 퀴즈 생성하면 true -> false 로 상태 바꾸기
    public void updateQuizCreatingStatus(boolean status) {
        this.isQuizCreating = status;
    }

}
