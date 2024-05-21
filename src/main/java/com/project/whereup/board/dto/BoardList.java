package com.project.whereup.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

//전체 목록에서 띄울거
//아이디, 이름, 뭐......
//추가하면 repository 도 수정할 것
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardList {
    private Long boardId;
    private String subject;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start_date;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end_date;
    private String brand;
    private String image;
}
