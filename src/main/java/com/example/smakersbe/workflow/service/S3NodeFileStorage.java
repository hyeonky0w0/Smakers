package com.example.smakersbe.workflow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3NodeFileStorage implements NodeFileStorage {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucket;

    @Value("${cloud.aws.s3.prefix:workflow-files}")
    private String prefix;

    @Value("${cloud.aws.cloudfront.domain}")
    private String cloudfrontDomain;

    @Override
    public String upload(MultipartFile file, Long userId, Long nodeId) {
        String original = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename();
        String safeName = URLEncoder.encode(original, StandardCharsets.UTF_8).replace("+", "%20");

        String key = prefix + "/" + userId + "/" + nodeId + "/" + UUID.randomUUID() + "-" + safeName;

        try {
            PutObjectRequest putReq = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putReq, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new IllegalStateException("S3 upload failed", e);
        }

        return toCloudFrontUrl(key); // ✅ DB에는 CloudFront URL 저장
    }

    @Override
    public void deleteByUrl(String url) {
        String key = extractKeyFromCloudFrontUrl(url);

        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build()
        );
    }

    private String toCloudFrontUrl(String key) {
        String d = cloudfrontDomain.endsWith("/") ? cloudfrontDomain.substring(0, cloudfrontDomain.length() - 1) : cloudfrontDomain;
        return d + "/" + key;
    }

    private String extractKeyFromCloudFrontUrl(String url) {
        String d = cloudfrontDomain.endsWith("/") ? cloudfrontDomain : cloudfrontDomain + "/";
        if (url.startsWith(d)) return url.substring(d.length());

        String d2 = cloudfrontDomain.endsWith("/") ? cloudfrontDomain.substring(0, cloudfrontDomain.length() - 1) : cloudfrontDomain;
        if (url.startsWith(d2 + "/")) return url.substring((d2 + "/").length());

        throw new IllegalArgumentException("Not a CloudFront url: " + url);
    }
}
