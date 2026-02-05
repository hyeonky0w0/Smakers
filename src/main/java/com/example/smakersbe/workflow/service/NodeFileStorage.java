package com.example.smakersbe.workflow.service;

import org.springframework.web.multipart.MultipartFile;

public interface NodeFileStorage {
    String upload(MultipartFile file, Long userId, Long nodeId);
    void deleteByUrl(String url);
}
