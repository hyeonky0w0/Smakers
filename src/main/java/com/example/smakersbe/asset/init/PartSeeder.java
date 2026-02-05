package com.example.smakersbe.asset.init;

import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.asset.entity.Part;
import com.example.smakersbe.asset.entity.Vec3;
import com.example.smakersbe.asset.repository.AssetRepository;
import com.example.smakersbe.asset.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(2)
public class PartSeeder implements CommandLineRunner {

    private final AssetRepository assetRepository;
    private final PartRepository partRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (partRepository.count() > 0) return;

        Asset drone = getAsset("Drone");
        Asset leafSpring = getAsset("Leaf Spring");
        Asset machineVice = getAsset("Machine Vice");
        Asset robotArm = getAsset("Robot Arm");
        Asset robotGripper = getAsset("Robot Gripper");
        Asset suspension = getAsset("Suspension");
        Asset v4Engine = getAsset("V4_Engine");

        // 좌표는 아직 확정값이 없어서: assembled는 (0,0,0), exploded는 "순번 기반으로 살짝 벌려서" 넣어둠
        partRepository.saveAll(List.of(

                // ===================== Drone (1-*) =====================
                Part.builder()
                        .asset(drone)
                        .partName("Arm gear")
                        .partDescription("드론 암(Drone Arm)의 회전 구동을 담당하는 기어 부품입니다.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/drone/1-1-arm-gear.png")
                        .partGlbUrl("https://example.com/parts/drone/1-1-arm-gear.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(1).explodedY(0).explodedZ(0)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                Part.builder()
                        .asset(drone)
                        .partName("Beater disc")
                        .partDescription("고속 회전 및 충격 완화 기능을 동시에 수행하는 핵심 구동 부품.\n드론 모터의 회전력을 프로펠러로 전달하며, 비행 시 발생하는 미세 진동을 흡수.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/drone/1-2-beater-disc.png")
                        .partGlbUrl("https://example.com/parts/drone/1-2-beater-disc.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(1).explodedZ(0)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                Part.builder()
                        .asset(drone)
                        .partName("Geearing")
                        .partDescription("드론 구동 시스템에서 회전력을 전달하는 기어 부품.\n드론 구동부의 동력 전달 효율과 안정성에 기여.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/drone/1-3-geearing.png")
                        .partGlbUrl("https://example.com/parts/drone/1-3-geearing.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(0).explodedZ(1)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                Part.builder()
                        .asset(drone)
                        .partName("Impellar Blade")
                        .partDescription("회전 시 공기를 밀어 추력을 발생시키는 블레이드 부품.\n드론 비행에 필요한 양력을 생성.")
                        .material("금속/플라스틱")
                        .partThumbnailUrl("https://example.com/parts/drone/1-4-impellar-blade.png")
                        .partGlbUrl("https://example.com/parts/drone/1-4-impellar-blade.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(-1).explodedY(0).explodedZ(0)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                Part.builder()
                        .asset(drone)
                        .partName("Leg")
                        .partDescription("드론 기체 하부를 지지하는 구조 부품.\n이착륙 시 기체와 지면 사이의 간격을 확보하며, 하중을 분산해 기체 안정성을 유지.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/drone/1-5-leg.png")
                        .partGlbUrl("https://example.com/parts/drone/1-5-leg.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(-1).explodedZ(0)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                Part.builder()
                        .asset(drone)
                        .partName("Main frame")
                        .partDescription("드론의 모든 주요 부품을 지지하는 기체의 핵심 구조 부품.\n모터, 암, 구동부 등 각 구성 요소를 결합하며, 전체 기체의 강성 및 구조적 안정성을 담당.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/drone/1-6-main-frame.png")
                        .partGlbUrl("https://example.com/parts/drone/1-6-main-frame.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(0).explodedZ(-1)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                Part.builder()
                        .asset(drone)
                        .partName("Main frame_MIR")
                        .partDescription("메인 프레임 구조를 기반으로 설계된 확장형 기체 프레임입니다.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/drone/1-7-main-frame-mir.png")
                        .partGlbUrl("https://example.com/parts/drone/1-7-main-frame-mir.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(1).explodedY(1).explodedZ(0)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                Part.builder()
                        .asset(drone)
                        .partName("Nut")
                        .partDescription("볼트와 결합되어 부품을 고정하는 체결용 부품.\n회전 시 풀림을 방지하며, 구조물의 결합 안정성을 유지.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/drone/1-8-nut.png")
                        .partGlbUrl("https://example.com/parts/drone/1-8-nut.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(1).explodedY(0).explodedZ(1)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                Part.builder()
                        .asset(drone)
                        .partName("Screw")
                        .partDescription("회전 체결을 통해 부품을 고정하는 체결용 부품.\n너트 또는 탭 가공된 구조와 결합되어 기체 구성 요소를 안정적으로 고정.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/drone/1-9-screw.png")
                        .partGlbUrl("https://example.com/parts/drone/1-9-screw.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(1).explodedZ(1)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                Part.builder()
                        .asset(drone)
                        .partName("xyz")
                        .partDescription("원통형 형상의 구조 부품으로, 다른 구성 요소를 지지하거나 간격을 유지하는 역할. 하중 전달 및 정렬에 활용.")
                        .material("알류미늄 합금")
                        .partThumbnailUrl("https://example.com/parts/drone/1-10-xyz.png")
                        .partGlbUrl("https://example.com/parts/drone/1-10-xyz.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(-1).explodedY(-1).explodedZ(0)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                // ===================== Leaf Spring (2-*) =====================
                Part.builder()
                        .asset(leafSpring)
                        .partName("Clamp-Center")
                        .partDescription("리프 스프링의 중앙부를 고정하는 체결 부품. 스프링 적층을 일정 위치에 유지하며, 하중 전달 시 스프링의 정렬과 안정성을 확보.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/leaf-spring/2-1-clamp-center.png")
                        .partGlbUrl("https://example.com/parts/leaf-spring/2-1-clamp-center.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(1).explodedY(0).explodedZ(0)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                Part.builder()
                        .asset(leafSpring)
                        .partName("Clamp-Primary")
                        .partDescription("리프 스프링을 프레임에 고정하는 체결 부품.\n스프링과 차체를 연결하며, 주행 중 발생하는 하중과 진동을 안정적으로 전달.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/leaf-spring/2-2-clamp-primary.png")
                        .partGlbUrl("https://example.com/parts/leaf-spring/2-2-clamp-primary.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(1).explodedZ(0)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                Part.builder()
                        .asset(leafSpring)
                        .partName("Clamp-Secondary")
                        .partDescription("리프 스프링 보조 고정을 위한 체결 부품. 주 클램프와 함께 스프링을 지지하며, 하중 분산과 결합 안정성을 보조.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/leaf-spring/2-3-clamp-secondary.png")
                        .partGlbUrl("https://example.com/parts/leaf-spring/2-3-clamp-secondary.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(0).explodedZ(1)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                Part.builder()
                        .asset(leafSpring)
                        .partName("Leaf-Layer")
                        .partDescription("리프 스프링을 구성하는 개별 판 스프링 부품.\n여러 장이 적층되어 하중을 지지하며, 탄성 변형을 통해 충격과 진동을 흡수.")
                        .material("플라스틱")
                        .partThumbnailUrl("https://example.com/parts/leaf-spring/2-4-leaf-layer.png")
                        .partGlbUrl("https://example.com/parts/leaf-spring/2-4-leaf-layer.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(-1).explodedY(0).explodedZ(0)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                Part.builder()
                        .asset(leafSpring)
                        .partName("Support")
                        .partDescription("구조물을 지지하고 결합을 보조하는 보강 부품.\n다른 부품과의 연결부에서 하중을 분산하며, 전체 구조의 안정성과 정렬을 유지.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/leaf-spring/2-5-support.png")
                        .partGlbUrl("https://example.com/parts/leaf-spring/2-5-support.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(-1).explodedZ(0)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                Part.builder()
                        .asset(leafSpring)
                        .partName("Support-Chassis Rigid")
                        .partDescription("차체와 연결되는 구조를 지지하는 강성 보강 부품.\n주요 하중을 직접 지지하며, 프레임 결합부의 강성과 구조적 안정성을 확보.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/leaf-spring/2-6-support-chassis-rigid.png")
                        .partGlbUrl("https://example.com/parts/leaf-spring/2-6-support-chassis-rigid.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(0).explodedZ(-1)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                Part.builder()
                        .asset(leafSpring)
                        .partName("Support-Chassis")
                        .partDescription("차체와 서스펜션 부품을 연결하는 지지 구조 부품.\n리프 스프링 및 연결 요소를 지지하며, 주행 시 발생하는 하중을 차체로 전달.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/leaf-spring/2-7-support-chassis.png")
                        .partGlbUrl("https://example.com/parts/leaf-spring/2-7-support-chassis.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(1).explodedY(1).explodedZ(0)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                Part.builder()
                        .asset(leafSpring)
                        .partName("Support-Rubber 60mm")
                        .partDescription("진동과 충격을 완화하는 탄성 지지 부품.\n금속 부품 사이에 삽입되어 소음을 저감하며, 하중 전달 시 충격 흡수와 구조 보호를 보조. (60mm)")
                        .material("고무")
                        .partThumbnailUrl("https://example.com/parts/leaf-spring/2-8-support-rubber-60mm.png")
                        .partGlbUrl("https://example.com/parts/leaf-spring/2-8-support-rubber-60mm.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(1).explodedY(0).explodedZ(1)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                Part.builder()
                        .asset(leafSpring)
                        .partName("Support-Rubber")
                        .partDescription("진동과 충격을 완화하는 탄성 지지 부품.\n금속 부품 사이에 삽입되어 소음을 저감하며, 하중 전달 시 충격 흡수와 구조 보호를 보조.")
                        .material("고무")
                        .partThumbnailUrl("https://example.com/parts/leaf-spring/2-9-support-rubber.png")
                        .partGlbUrl("https://example.com/parts/leaf-spring/2-9-support-rubber.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(1).explodedZ(1)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                // ===================== Machine Vice (3-*) =====================
                Part.builder()
                        .asset(machineVice)
                        .partName("Part1 Fuhrung")
                        .partDescription("머신 바이스에서 이동 죠의 직선 운동을 안내하는 가이드 부품.\n이동 시 정렬을 유지하며, 마찰을 최소화해 안정적인 클램핑 동작을 지원.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/machine-vice/3-1-part1-fuhrung.png")
                        .partGlbUrl("https://example.com/parts/machine-vice/3-1-part1-fuhrung.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(1).explodedY(0).explodedZ(0)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                Part.builder()
                        .asset(machineVice)
                        .partName("Part1")
                        .partDescription("나사 구동에 따라 전후로 이동하며 공작물을 가압하는 죠 부품.\n고정 죠와 함께 공작물을 클램핑하고, 가공 중 위치 안정성과 고정력을 확보.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/machine-vice/3-2-part1.png")
                        .partGlbUrl("https://example.com/parts/machine-vice/3-2-part1.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(1).explodedZ(0)
                        .rotation(new Vec3(0, 0, 0))
                        .build(),

                Part.builder()
                        .asset(machineVice)
                        .partName("Part2 Feste Backe")
                        .partDescription("머신 바이스에 고정된 죠로, 이동 죠와 함께 공작물을 지지하며, 가공 중 위치 기준과 고정 안정성을 제공.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/machine-vice/3-3-part2-feste-backe.png")
                        .partGlbUrl("https://example.com/parts/machine-vice/3-3-part2-feste-backe.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(0).explodedZ(1)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(machineVice)
                        .partName("Part3-lose backe")
                        .partDescription("머신 바이스에서 이동하는 죠 부품. 나사 구동에 따라 전후로 이동하며, 공작물을 고정하고 클램핑력을 전달.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/machine-vice/3-4-part3-lose-backe.png")
                        .partGlbUrl("https://example.com/parts/machine-vice/3-4-part3-lose-backe.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(-1).explodedY(0).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(machineVice)
                        .partName("Part4 spindelsockel")
                        .partDescription("스핀들(리드 스크류)을 지지하는 베이스 부품.\n스핀들의 회전 축을 고정하며, 나사 구동 시 안정적인 동력 전달과 정렬을 유지.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/machine-vice/3-5-part4-spindelsockel.png")
                        .partGlbUrl("https://example.com/parts/machine-vice/3-5-part4-spindelsockel.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(-1).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(machineVice)
                        .partName("Part5-Spannbacke")
                        .partDescription("공작물과 직접 접촉하여 고정하는 클램핑 죠 부품.\n고정·이동 죠에 장착되어 공작물을 안정적으로 가압하며,\n가공 중 미끄럼을 방지하고 고정력을 전달.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/machine-vice/3-6-part5-spannbacke.png")
                        .partGlbUrl("https://example.com/parts/machine-vice/3-6-part5-spannbacke.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(0).explodedZ(-1)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(machineVice)
                        .partName("Part6-fuhrungschiene")
                        .partDescription("이동 죠의 직선 운동을 안내하는 가이드 레일 부품.\n이동 경로를 안정적으로 유지하며,\n클램핑 동작 시 정렬 정확도와 작동 안정성을 확보.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/machine-vice/3-7-part6-fuhrungschiene.png")
                        .partGlbUrl("https://example.com/parts/machine-vice/3-7-part6-fuhrungschiene.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(1).explodedY(1).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(machineVice)
                        .partName("Part7-TrapezSpindel")
                        .partDescription("회전 운동을 직선 운동으로 변환하는 나사 구동 부품.\n회전 시 이동 죠를 전후로 이동시키며,\n머신 바이스의 클램핑 힘을 생성하고 전달.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/machine-vice/3-8-part7-trapezspindel.png")
                        .partGlbUrl("https://example.com/parts/machine-vice/3-8-part7-trapezspindel.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(1).explodedY(0).explodedZ(1)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(machineVice)
                        .partName("Part8-grundplatte")
                        .partDescription("머신 바이스의 하부를 구성하는 베이스 플레이트 부품.\n각 구성 요소를 지지하고 결합하며,\n장비 설치 시 전체 구조의 강성과 안정성을 확보.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/machine-vice/3-9-part8-grundplatte.png")
                        .partGlbUrl("https://example.com/parts/machine-vice/3-9-part8-grundplatte.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(1).explodedZ(1)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(machineVice)
                        .partName("Part9-Druckhulse")
                        .partDescription("스핀들 구동 시 발생하는 축방향 하중을 지지하는 슬리브 부품.\n하중을 균일하게 전달하며,\n회전 부품의 마찰 감소와 구조 보호를 보조.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/machine-vice/3-10-part9-druckhulse.png")
                        .partGlbUrl("https://example.com/parts/machine-vice/3-10-part9-druckhulse.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(-1).explodedY(-1).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                // ===================== Robot Arm (4-*) =====================
                Part.builder()
                        .asset(robotArm)
                        .partName("base")
                        .partDescription("로봇 암 전체를 지지하는 하부 구조 부품.\n로봇 암을 작업면에 고정하며,\n하중을 분산해 전체 시스템의 안정성을 유지.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/robot-arm/4-1-base.png")
                        .partGlbUrl("https://example.com/parts/robot-arm/4-1-base.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(1).explodedY(0).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(robotArm)
                        .partName("Part2")
                        .partDescription("로봇 암의 하부 관절을 구성하는 회전 구동 부품.\n베이스와 상부 링크를 연결하며,\n관절 회전을 통해 로봇 암의 초기 동작 범위와 방향 제어를 담당.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/robot-arm/4-2-part2.png")
                        .partGlbUrl("https://example.com/parts/robot-arm/4-2-part2.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(1).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(robotArm)
                        .partName("Part3")
                        .partDescription("로봇 암의 하부 링크를 구성하는 연결 부품.\n두 관절을 연결해 동작 범위를 확장하며,\n회전 운동에 따른 하중과 토크를 안정적으로 전달.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/robot-arm/4-3-part3.png")
                        .partGlbUrl("https://example.com/parts/robot-arm/4-3-part3.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(0).explodedZ(1)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(robotArm)
                        .partName("Part4")
                        .partDescription("로봇 암의 중간 관절 구동부를 구성하는 회전 유닛.\n상부 링크와 하부 링크를 연결하며,\n관절 회전을 통해 로봇 암의 자세 변화와 정밀한 위치 제어를 담당.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/robot-arm/4-4-part4.png")
                        .partGlbUrl("https://example.com/parts/robot-arm/4-4-part4.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(-1).explodedY(0).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(robotArm)
                        .partName("Part5")
                        .partDescription("로봇 암의 상부 링크 연결부를 구성하는 관절 하우징 부품.\n인접한 링크를 결합해 회전 축을 형성하며,\n관절 구동 시 발생하는 하중과 토크를 안정적으로 지지.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/robot-arm/4-5-part5.png")
                        .partGlbUrl("https://example.com/parts/robot-arm/4-5-part5.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(-1).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(robotArm)
                        .partName("Part6")
                        .partDescription("로봇 암의 말단 관절로, 상부 링크와 그리퍼 부분을 연결하는 연결 부품.\n회전 관절 구조를 통해 그리퍼의 각도 조절을 가능하게 하며,\n작업 대상에 대한 정밀한 위치 제어와 안정적인 동작을 지원.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/robot-arm/4-6-part6.png")
                        .partGlbUrl("https://example.com/parts/robot-arm/4-6-part6.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(0).explodedZ(-1)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(robotArm)
                        .partName("Part7")
                        .partDescription("로봇 암의 말단에 장착되는 그리퍼 결합 부품으로,\n구동부의 회전력을 그리퍼로 전달하는 인터페이스 역할을 수행.\n원형 플랜지 구조를 통해 정밀한 정렬이 가능하며,\n그리퍼의 안정적인 체결과 반복 작업 시 신뢰성 있는 동작을 확보.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/robot-arm/4-7-part7.png")
                        .partGlbUrl("https://example.com/parts/robot-arm/4-7-part7.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(1).explodedY(1).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(robotArm)
                        .partName("Part8")
                        .partDescription("로봇 암의 말단에 장착되는 그리퍼 구성 부품으로,\n링크 구조를 통해 개폐 동작을 수행하며 대상물을 직접 파지.\n관절부 회전을 통해 파지 각도를 조절할 수 있고,\n접촉 면의 형상으로 안정적인 고정력과 작업 정밀도를 확보.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/robot-arm/4-8-part8.png")
                        .partGlbUrl("https://example.com/parts/robot-arm/4-8-part8.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(1).explodedY(0).explodedZ(1)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                // ===================== Robot Gripper (5-*) =====================
                Part.builder()
                        .asset(robotGripper)
                        .partName("Base Gear")
                        .partDescription("구동 모터의 회전력을 전달하는 기어 부품.\n맞물린 기어 및 링크 구조와 연동되어 그리퍼의 개폐 동작을 안정적으로 제어.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/robot-gripper/5-1-base-gear.png")
                        .partGlbUrl("https://example.com/parts/robot-gripper/5-1-base-gear.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(1).explodedY(0).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(robotGripper)
                        .partName("Base Mounting bracket")
                        .partDescription("그리퍼 베이스를 프레임 또는 장착면에 고정하는 브래킷 부품.\n체결 홀을 통해 안정적인 설치를 지원하며, 구동 시 발생하는 하중과 진동을 견고하게 지지.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/robot-gripper/5-2-base-mounting-bracket.png")
                        .partGlbUrl("https://example.com/parts/robot-gripper/5-2-base-mounting-bracket.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(1).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(robotGripper)
                        .partName("Base Plate")
                        .partDescription("그리퍼 내부 부품들을 지지하고 정렬하는 기본 구조 부품.\n장착 홀과 개구부를 통해 기어·샤프트 등의 위치를 정확히 고정하며, 전체 구조의 강성과 안정성을 확보.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/robot-gripper/5-3-base-plate.png")
                        .partGlbUrl("https://example.com/parts/robot-gripper/5-3-base-plate.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(0).explodedZ(1)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(robotGripper)
                        .partName("Gear link 1")
                        .partDescription("기어의 회전 운동을 링크 메커니즘으로 전달하는 연결 부품.\n기어와 그리퍼 암을 연결해 회전을 직선·각도 운동으로 변환하며, 구동력 전달과 동작 타이밍의 정합성을 유지.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/robot-gripper/5-4-gear-link-1.png")
                        .partGlbUrl("https://example.com/parts/robot-gripper/5-4-gear-link-1.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(-1).explodedY(0).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(robotGripper)
                        .partName("Gear link 2")
                        .partDescription("보조 기어와 연결되어 회전 운동을 링크 구조로 전달하는 부품.\n그리퍼 양쪽 암의 동기화를 보조하며, 구동 범위 조절과 안정적인 힘 분배를 담당.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/robot-gripper/5-5-gear-link-2.png")
                        .partGlbUrl("https://example.com/parts/robot-gripper/5-5-gear-link-2.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(-1).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(robotGripper)
                        .partName("Gripper")
                        .partDescription("물체를 직접 파지하는 핵심 부품.\n기어 및 링크 메커니즘과 연동되어 개폐 동작을 수행하며, 톱니 형상을 통해 물체를 안정적으로 잡고 미끄러짐을 방지.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/robot-gripper/5-6-gripper.png")
                        .partGlbUrl("https://example.com/parts/robot-gripper/5-6-gripper.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(0).explodedZ(-1)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(robotGripper)
                        .partName("Link")
                        .partDescription("기어와 그리퍼를 연결하는 링크 부품.\n회전 운동을 직선 또는 각도 변화로 전달하여 그리퍼의 개폐 동작을 정밀하게 제어.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/robot-gripper/5-7-link.png")
                        .partGlbUrl("https://example.com/parts/robot-gripper/5-7-link.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(1).explodedY(1).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(robotGripper)
                        .partName("Pin")
                        .partDescription("링크와 기어, 그리퍼 부품을 연결하는 회전 축 역할의 체결 부품.\n각 부품 간의 원활한 회전과 정확한 위치 정렬을 유지하며, 동작 중 발생하는 하중을 안정적으로 지지.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/robot-gripper/5-8-pin.png")
                        .partGlbUrl("https://example.com/parts/robot-gripper/5-8-pin.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(1).explodedY(0).explodedZ(1)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                // ===================== Suspension (6-*) =====================
                Part.builder()
                        .asset(suspension)
                        .partName("BASE")
                        .partDescription("서스펜션 시스템의 하부를 지지하는 기본 구조 부품으로, 차체 또는 프레임과 연결되어 전체 하중을 안정적으로 전달.\n상부 샤프트와 완충 부품을 고정하며, 서스펜션 동작 시 발생하는 힘을 분산시켜 구조적 안정성을 확보.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/suspension/6-1-base.png")
                        .partGlbUrl("https://example.com/parts/suspension/6-1-base.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(1).explodedY(0).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(suspension)
                        .partName("NIT")
                        .partDescription("서스펜션 구성 부품을 축 방향으로 단단히 고정하는 체결 부품.\n샤프트 및 스프링과 결합되어 풀림을 방지하며, 하중과 진동이 반복되는 환경에서도 안정적인 고정력을 유지.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/suspension/6-2-nit.png")
                        .partGlbUrl("https://example.com/parts/suspension/6-2-nit.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(1).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(suspension)
                        .partName("NUT")
                        .partDescription("서스펜션 샤프트에 체결되어 스프링과 내부 부품을 고정하는 너트.\n외곽 톱니 형상으로 손쉬운 조립·분해가 가능하며, 체결 시 발생하는 하중을 균일하게 분산해 구조적 안정성을 확보.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/suspension/6-3-nut.png")
                        .partGlbUrl("https://example.com/parts/suspension/6-3-nut.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(0).explodedZ(1)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(suspension)
                        .partName("ROD")
                        .partDescription("서스펜션 내부에서 상·하 움직임을 전달하는 핵심 연결 부품.\n스프링의 압축·복원 동작을 가이드하며, 하중과 진동을 안정적으로 전달해 서스펜션의 직선 운동과 정렬을 유지.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/suspension/6-4-rod.png")
                        .partGlbUrl("https://example.com/parts/suspension/6-4-rod.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(-1).explodedY(0).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(suspension)
                        .partName("SPRING")
                        .partDescription("하중을 흡수하고 복원력을 제공하는 서스펜션의 탄성 부품.\n외부 충격과 진동을 완화하며, 압축·복원 과정에서 시스템의 안정적인 움직임과 승차감을 유지.")
                        .material("금속")
                        .partThumbnailUrl("https://example.com/parts/suspension/6-5-spring.png")
                        .partGlbUrl("https://example.com/parts/suspension/6-5-spring.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(-1).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                // ===================== V4_Engine (7-*) =====================
                Part.builder()
                        .asset(v4Engine)
                        .partName("Connecting Rod Cap")
                        .partDescription("커넥팅 로드 하부에서 크랭크샤프트를 감싸 고정하는 부품.\n볼트 체결을 통해 로드와 결합되며, 회전 운동 중 발생하는 하중을 안정적으로 지지해 엔진 동작의 정밀도와 내구성을 유지.")
                        .material("강철")
                        .partThumbnailUrl("https://example.com/parts/v4-engine/7-1-connecting-rod-cap.png")
                        .partGlbUrl("https://example.com/parts/v4-engine/7-1-connecting-rod-cap.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(1).explodedY(0).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(v4Engine)
                        .partName("Connecting Rod")
                        .partDescription("피스톤과 크랭크샤프트를 연결해\n직선 운동을 회전 운동으로 전달하는 핵심 부품.")
                        .material("강철")
                        .partThumbnailUrl("https://example.com/parts/v4-engine/7-2-connecting-rod.png")
                        .partGlbUrl("https://example.com/parts/v4-engine/7-2-connecting-rod.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(1).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(v4Engine)
                        .partName("Conrod Bolt")
                        .partDescription("커넥팅 로드와 로드 캡을 체결하는 고강도 볼트로,\n엔진 작동 시 발생하는 반복 하중을 견디며 결합부의 안정성을 유지하는 부품.")
                        .material("고강도 강철")
                        .partThumbnailUrl("https://example.com/parts/v4-engine/7-3-conrod-bolt.png")
                        .partGlbUrl("https://example.com/parts/v4-engine/7-3-conrod-bolt.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(0).explodedZ(1)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(v4Engine)
                        .partName("Crankshaft")
                        .partDescription("피스톤의 직선 운동을 회전 운동으로 변환하는 엔진의 핵심 축으로,\n커넥팅 로드를 통해 동력을 받아 엔진 출력으로 전달하는 역할을 수행한다.")
                        .material("단조강")
                        .partThumbnailUrl("https://example.com/parts/v4-engine/7-4-crankshaft.png")
                        .partGlbUrl("https://example.com/parts/v4-engine/7-4-crankshaft.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(-1).explodedY(0).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(v4Engine)
                        .partName("Piston Pin")
                        .partDescription("피스톤과 커넥팅 로드를 연결하는 부품으로,\n피스톤의 왕복 운동을 커넥팅 로드로 전달해 원활한 동력 전달을 돕는다.")
                        .material("강철")
                        .partThumbnailUrl("https://example.com/parts/v4-engine/7-5-piston-pin.png")
                        .partGlbUrl("https://example.com/parts/v4-engine/7-5-piston-pin.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(-1).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(v4Engine)
                        .partName("Piston Ring")
                        .partDescription("피스톤 외주에 장착되는 링 형태의 부품으로,\n실린더와의 기밀을 유지해 압축 손실을 방지하고 오일 조절을 통해 엔진 효율을 높인다.")
                        .material("강철")
                        .partThumbnailUrl("https://example.com/parts/v4-engine/7-6-piston-ring.png")
                        .partGlbUrl("https://example.com/parts/v4-engine/7-6-piston-ring.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(0).explodedY(0).explodedZ(-1)
                        .rotation(new Vec3(0, 90, 0))
                        .build(),

                Part.builder()
                        .asset(v4Engine)
                        .partName("Piston")
                        .partDescription("실린더 내부에서 상하 운동하며 연소 압력을 받아 동력을 생성하는 핵심 부품이다.\n피스톤 링과 함께 기밀을 유지하고, 힘을 커넥팅 로드로 전달한다.")
                        .material("알리미늄")
                        .partThumbnailUrl("https://example.com/parts/v4-engine/7-7-piston.png")
                        .partGlbUrl("https://example.com/parts/v4-engine/7-7-piston.glb")
                        .assembledX(0).assembledY(0).assembledZ(0)
                        .explodedX(1).explodedY(1).explodedZ(0)
                        .rotation(new Vec3(0, 90, 0))
                        .build()
        ));
    }
    private Asset getAsset(String assetName) {
        return assetRepository.findByAssetName(assetName)
                .orElseThrow(() -> new IllegalStateException("Asset not found: " + assetName));
    }

}
