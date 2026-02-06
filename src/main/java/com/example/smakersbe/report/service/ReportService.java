package com.example.smakersbe.report.service;

import com.example.smakersbe.report.dto.request.ReportRequestDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ReportService {

    byte[] createReport(String uuid, Long assetId, ReportRequestDTO requestDTO, MultipartFile captureImage);

}
