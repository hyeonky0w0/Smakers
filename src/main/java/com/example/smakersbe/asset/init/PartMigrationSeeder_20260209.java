package com.example.smakersbe.asset.init;

import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.asset.entity.SeedMigration;
import com.example.smakersbe.asset.repository.AssetRepository;
import com.example.smakersbe.asset.repository.PartRepository;
import com.example.smakersbe.asset.repository.SeedMigrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Order(99) // init seeder들 다 돈 뒤에
public class PartMigrationSeeder_20260209  implements CommandLineRunner {

    private final AssetRepository assetRepository;
    private final PartRepository partRepository;
    private final SeedMigrationRepository seedMigrationRepository;

    @Override
    @Transactional
    public void run(String... args) {
        String MIGRATION_ID = "part-migration-20260209";
        if (seedMigrationRepository.existsById(MIGRATION_ID)) return; // ✅ 이미 적용했으면 스킵

        Asset drone = getAsset("Drone");
        Asset machineVice = getAsset("Machine Vice");
        Asset robotArm = getAsset("Robot Arm");
        Asset suspension = getAsset("Suspension");


        // ✅ 삭제
        // Drone
        deletePartByName(drone, "Main frame_MIR");
        //MachineVice: Part1
        deletePartByName(machineVice, "Part1");
        // Suspension
        deletePartByName(suspension, "NIT");

        // ✅ rename
        // xyz -> Gyroscope sensor (id 유지)
        renamePart(drone, "xyz", "Gyroscope sensor");

        // Robot Arm rename
        renamePart(robotArm, "Shouler Joint Housing", "Shoulder Joint Housing");
        renamePart(robotArm, "Upper Arm Link", "Upper Arm Link");
        renamePart(robotArm, "Elbow Joint Module", "Elbow Joint Module");
        renamePart(robotArm, "Lower Arm Link", "Lower Arm Link");
        renamePart(robotArm, "Wrist Joint Unit", "Wrist Joint Unit");
        renamePart(robotArm, "End Effector Mount", "End Effector Mount");
        renamePart(robotArm, "Gripper", "Gripper");


        // ✅ 3) MachineVice: 이름 정리
        // Robot Arm rename (Part2 ~ Part8 → 의미 있는 이름)
        renamePart(robotArm, "Part2", "Shoulder Joint Housing");
        renamePart(robotArm, "Part3", "Upper Arm Link");
        renamePart(robotArm, "Part4", "Elbow Joint Module");
        renamePart(robotArm, "Part5", "Lower Arm Link");
        renamePart(robotArm, "Part6", "Wrist Joint Unit");
        renamePart(robotArm, "Part7", "End Effector Mount");
        renamePart(robotArm, "Part8", "Gripper");;

        // ✅ 적용 기록 남기기 (이게 핵심)
        seedMigrationRepository.save(new SeedMigration(MIGRATION_ID, LocalDateTime.now()));
    }

    private Asset getAsset(String name) {
        return assetRepository.findByAssetName(name)
                .orElseThrow(() -> new IllegalStateException("Asset not found: " + name));
    }

    private void renamePart(Asset asset, String oldName, String newName) {
        partRepository.findByAssetAndPartName(asset, oldName).ifPresent(oldPart -> {

            // newName이 이미 있으면 충돌 -> oldName을 삭제로 처리(선택)
            if (partRepository.findByAssetAndPartName(asset, newName).isPresent()) {
                partRepository.delete(oldPart);
                return;
            }

            oldPart.setPartName(newName);
            partRepository.save(oldPart);
        });
    }

    private void deletePartByName(Asset asset, String partName) {
        partRepository.findByAssetAndPartName(asset, partName)
                .ifPresent(partRepository::delete);
    }
}
