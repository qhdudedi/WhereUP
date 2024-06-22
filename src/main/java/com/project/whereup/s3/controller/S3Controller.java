package com.project.whereup.s3.controller;

import com.project.whereup.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/presigned-url")
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;

    @GetMapping
    public Map<String, Object> generatePresignedUrl(@RequestParam String key) {
        Map<String, Object> response = new HashMap<>();
        key = "post/" + System.currentTimeMillis() + "_" + key;
        response.put("url", s3Service.generatePresignedUrl(key));
        return response;
    }
}
