package com.example.smakersbe.asset.init;

import com.example.smakersbe.asset.entity.Vec3;
import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.asset.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(1)
public class AssetSeeder implements CommandLineRunner {

    private final AssetRepository assetRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (assetRepository.count() > 0) return; // 이미 있으면 스킵

        List<Asset> defaults = List.of(
                Asset.builder()
                        .assetName("Drone")
                        .assetDescription("4개의 독립 로터와 경량 바디로 구성된,\n4K 초고화질 카메라를 탑재한 다목적 드론.")
                        .assetThumbnailUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/thumbnail/drone.png")
                        .assetAssembledGlbUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/glb/as/drone.glb")
                        .assetExplodedGlbUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/glb/ex/drone-ex.glb")
                        .position(new Vec3(-0.1, 0.2, 0))
                        .rotation(new Vec3(0, -155, 0))
                        .build(),

                Asset.builder()
                        .assetName("Leaf Spring")
                        .assetDescription("판 형태의 스프링을 적층하여 구성된 서스펜션 부품.")
                        .assetThumbnailUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/thumbnail/leaf-spring.png")
                        .assetAssembledGlbUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/glb/as/leaf-spring.glb")
                        .assetExplodedGlbUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/glb/ex/leaf-spring-ex.glb")
                        .position(new Vec3(-0.2, 0, -0.2))
                        .rotation(new Vec3(0, -60, 0))
                        .build(),

                Asset.builder()
                        .assetName("Machine Vice")
                        .assetDescription("공작물을 단단히 고정하기 위한 기계 바이스.")
                        .assetThumbnailUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/thumbnail/machine-vice.png")
                        .assetAssembledGlbUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/glb/as/machine-vice.glb")
                        .assetExplodedGlbUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/glb/ex/machine-vice-ex.glb")
                        .position(new Vec3(-0.6, 0, -0.4))
                        .rotation(new Vec3(0, 30, 0))
                        .build(),

                Asset.builder()
                        .assetName("Robot Arm")
                        .assetDescription("다관절 구조의 산업용 로봇 암.")
                        .assetThumbnailUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/thumbnail/robot-arm.png")
                        .assetAssembledGlbUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/glb/as/robot-arm.glb")
                        .assetExplodedGlbUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/glb/ex/robot-arm-ex.glb")
                        .position(new Vec3(0.2, -0.4, 0))
                        .rotation(new Vec3(0, -35, 0))
                        .build(),

                Asset.builder()
                        .assetName("Robot Gripper")
                        .assetDescription("로봇용 파지 장치.")
                        .assetThumbnailUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/thumbnail/robot-gripper.png")
                        .assetAssembledGlbUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/glb/as/robot-gripper.glb")
                        .assetExplodedGlbUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/glb/ex/robot-gripper-ex.glb")
                        .position(new Vec3(0.3, 0.5, 0))
                        .rotation(new Vec3(27, -27, 10))
                        .build(),

                Asset.builder()
                        .assetName("Suspension")
                        .assetDescription("충격과 진동을 흡수하는 서스펜션 구조.")
                        .assetThumbnailUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/thumbnail/suspension.png")
                        .assetAssembledGlbUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/glb/as/suspension.glb")
                        .assetExplodedGlbUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/glb/ex/suspension-ex.glb")
                        .position(new Vec3(-0.5, -0.5, 0))
                        .rotation(new Vec3(-63, -45, -57))
                        .build(),

                Asset.builder()
                        .assetName("V4_Engine")
                        .assetDescription("V형 4기통 엔진 구조 에셋.")
                        .assetThumbnailUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/thumbnail/v4-engine.png")
                        .assetAssembledGlbUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/glb/as/v4-engine.glb")
                        .assetExplodedGlbUrl("https://smakers-bucket.s3.ap-northeast-2.amazonaws.com/static/assets/glb/ex/v4-engine-ex.glb")
                        .position(new Vec3(-0.8, -0.5, 0))
                        .rotation(new Vec3(-8, 22, 0))
                        .build()
        );


        assetRepository.saveAll(defaults);
    }
}
