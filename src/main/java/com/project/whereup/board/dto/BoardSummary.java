package com.project.whereup.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

//전체 목록에서 띄울거
//아이디, 이름, 날짜, 브랜드
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardSummary {
    private Long id;
    private String subject;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start_date;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end_date;
    private String brand;
    private String location;
    @Setter
    private String imageUrl;
}
