package com.example.smakersbe.asset.init;

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

        //url은 일단 더미 넣어놓고 나중에 S3로 변경 필요
        List<Asset> defaults = List.of(
                Asset.builder()
                        .assetName("Drone")
                        .assetDescription("4개의 독립 로터와 경량 바디로 구성된,\n4K 초고화질 카메라를 탑재한 다목적 드론.")
                        .assetThumbnailUrl("https://example.com/assets/drone.png")
                        .assetGlbUrl("https://example.com/assets/drone.glb")
                        .build(),

                Asset.builder()
                        .assetName("Leaf Spring")
                        .assetDescription("판 형태의 스프링을 적층하여 구성된 서스펜션 부품.\n하중을 지지하면서 충격과 진동을 흡수해\n구조물의 안정성과 승차감을 유지하는 역할.")
                        .assetThumbnailUrl("https://example.com/assets/leaf-spring.png")
                        .assetGlbUrl("https://example.com/assets/drone.glb")
                        .build(),

                Asset.builder()
                        .assetName("Machine Vice")
                        .assetDescription("밀링 머신이나 드릴링 머신 같은\n공작 기계의 테이블에 공작물을 단단히 고정시켜 가공할 수 있도록 하는 기계 장치")
                        .assetThumbnailUrl("https://example.com/assets/machine-vice.png")
                        .assetGlbUrl("https://example.com/assets/drone.glb")
                        .build(),

                Asset.builder()
                        .assetName("Robot Arm")
                        .assetDescription("다관절 구조로 구성된 산업용 로봇 암 에셋.\n각 관절의 회전 운동을 통해 위치와 자세를 정밀하게 제어하며,\n조립, 이송, 가공 등 자동화 작업에 활용.")
                        .assetThumbnailUrl("https://example.com/assets/robot-arm.png")
                        .assetGlbUrl("https://example.com/assets/drone.glb")
                        .build(),

                Asset.builder()
                        .assetName("Robot Gripper")
                        .assetDescription("기어와 링크 구조를 통해 개폐 동작을 수행하는 로봇 그리퍼 에셋.\n회전 동력을 양쪽 그리퍼에 전달해 대상물을 안정적으로 파지하도록 설계.")
                        .assetThumbnailUrl("https://example.com/assets/robot-gripper.png")
                        .assetGlbUrl("https://example.com/assets/drone.glb")
                        .build(),

                Asset.builder()
                        .assetName("Suspension")
                        .assetDescription("스프링과 축 구조로 이루어진 완충 장치 에셋으로, 하중과 충격을 흡수해 구조물의 안정성을 유지.\n진동 완화와 복원 동작을 통해 기계 시스템의 내구성과 동작 신뢰성을 향상시키는 역할을 수행.")
                        .assetThumbnailUrl("https://example.com/assets/suspension.png")
                        .assetGlbUrl("https://example.com/assets/drone.glb")
                        .build(),

                Asset.builder()
                        .assetName("V4_Engine")
                        .assetDescription("4개의 실린더가 V형 구조로 배치된 내연기관 엔진 에셋.\n피스톤, 커넥팅 로드, 크랭크샤프트로 구성된 메커니즘을 통해 동력 생성 과정을 직관적으로 표현하며,\n엔진 구조 이해와 기계 동작 시각화에 적합.")
                        .assetThumbnailUrl("https://example.com/assets/v4-engine.png")
                        .assetGlbUrl("https://example.com/assets/drone.glb")
                        .build()
        );

        assetRepository.saveAll(defaults);
    }
}
