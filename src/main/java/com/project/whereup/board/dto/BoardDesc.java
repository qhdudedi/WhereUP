package com.project.whereup.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDesc {
    private Long id;
    private String subject;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start_date;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end_date;
    @Setter
    private String imageUrl;
}
