package com.example.smakersbe.report.service;

import com.example.smakersbe.report.dto.request.ReportRequestDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ReportService {

    byte[] createReport(Long userId, Long assetId, boolean onlyImportant, MultipartFile captureImage);

}
