package com.example.smakersbe.report.controller;

import com.example.smakersbe.report.dto.request.ReportRequestDTO;
import com.example.smakersbe.report.service.ReportService;
import com.example.smakersbe.user.entity.User;
import com.example.smakersbe.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final UserRepository userRepository;

    @PostMapping("/{assetId}/pdf")
    public ResponseEntity<byte[]> generateReport(
            @RequestHeader("X-USER-UUID") String uuid,
            @PathVariable Long assetId,
            @RequestParam ("onlyImportant") boolean onlyImportant,
            @RequestPart("image")MultipartFile captureImage
    ) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("해당 UUID를 가진 유저가 없습니다: " + uuid));
        Long userId = user.getUserId();

        byte[] pdfResult = reportService.createReport(userId, assetId, onlyImportant, captureImage);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.pdf\"")
                .body(pdfResult);
    }
}
