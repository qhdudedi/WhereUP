package com.project.whereup.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostSummary { // 후기 전체 보기 페이지에서 띄울 내용들
    private Long id;
    private String author;
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate created_date;
}
