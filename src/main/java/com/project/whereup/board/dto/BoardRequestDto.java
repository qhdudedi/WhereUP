package com.project.whereup.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import com.project.whereup.board.domain.Board;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequestDto {

    private Long id;
    private String subject;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start_date;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end_date;
    private String brand;

    public BoardRequestDto(Board board) {
        this.id = board.getId();
        this.subject = board.getSubject();
        this.start_date = board.getStart_date();
        this.end_date = board.getEnd_date();
        this.brand = board.getBrand();
    }
}
