package com.example.smakersbe.workflow.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class DummyNodeFileStorage implements NodeFileStorage {

    @Override
    public String upload(MultipartFile file, Long userId, Long nodeId) {
        // TODO: 나중에 S3 업로드로 교체
        String original = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename();
        String safe = URLEncoder.encode(original, StandardCharsets.UTF_8);
        return "https://example.com/workflow-files/"
                + userId + "/" + nodeId + "/"
                + UUID.randomUUID() + "-" + safe;
    }

    @Override
    public void deleteByUrl(String url) {
        // TODO: 나중에 S3 delete로 교체
    }
}