package com.example.smakersbe.report.controller;

import com.example.smakersbe.report.dto.request.ReportRequestDTO;
import com.example.smakersbe.report.service.ReportService;
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

    @PostMapping("/{assetId}/pdf")
    public ResponseEntity<byte[]> generateReport(
            @RequestHeader("X-USER-UUID") String uuid,
            @PathVariable Long assetId,
            @RequestPart("data") ReportRequestDTO requestDTO,
            @RequestPart("image")MultipartFile captureImage
    ) {
        byte[] pdfResult = reportService.createReport(uuid, assetId, requestDTO, captureImage);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.pdf\"")
                .body(pdfResult);
    }
}
