package com.example.smakersbe.workflow.dto.response;

public record NodeFileResponse(
        Long nodeFileId,
        String nodeFileName,
        String nodeFileUrl
) {}