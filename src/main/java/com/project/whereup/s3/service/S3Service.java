package com.project.whereup.s3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    //파일이름으로 presignedurl 얻기
    public String getPresignedUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofSeconds(5)) // url 유효기간
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);

        return presignedGetObjectRequest.url().toString();
    }
    // S3에 파일올리고 파일 url 리턴, 파일명 공백 _로 교체
    public String upload(MultipartFile file) {
        String key = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll(" ", "_");
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return "https://" + bucketName + ".s3.amazonaws.com/" + key; // S3에서 파일 기본 url
        } catch (IOException e) {
            return "https://via.placeholder.com/100x100.jpg";
        }
    }
}