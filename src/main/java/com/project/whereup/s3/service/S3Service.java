package com.project.whereup.s3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.*;

import java.net.URL;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    @Value("${aws.cloudfront.url}")
    private String cloudfrontDomain;
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    // 파일이름으로 presignedurl 얻기, 이미지 보는 용도 + cloudefront
    public String getPresignedUrl(String key) {
        PresignedGetObjectRequest presignedGetObjectRequest =
                s3Presigner.presignGetObject(pgo -> pgo.signatureDuration(Duration.ofSeconds(5)) // url 만료 시간
                        .getObjectRequest(gor -> gor.bucket(bucketName).key(key)));
        String url = presignedGetObjectRequest.url().toString();
        String bucketurl = String.format("%s.s3.amazonaws.com", bucketName);
        return url.replace(bucketurl, cloudfrontDomain);
    }
    //파일명으로 url얻기 + cloudefront
    public String getImageUrl(String key) {
        String url = s3Client.utilities().getUrl(gu -> gu.bucket(bucketName).key(key)).toExternalForm();
        String bucketurl = String.format("%s.s3.amazonaws.com", bucketName);
        return url.replace(bucketurl, cloudfrontDomain);
    }
    // 파일이름으로 presignedurl 얻기, 이미지 업로드하는 용도, front -> s3, 이미지파일이 서버 경유x
    public URL generatePresignedUrl(String key) {
        PresignedPutObjectRequest putObjectRequest =
                s3Presigner.presignPutObject(ppo -> ppo.signatureDuration(Duration.ofMinutes(60)) // url 만료 시간
                        .putObjectRequest(por -> por.bucket(bucketName).key(key)));

        return putObjectRequest.url();
    }
}