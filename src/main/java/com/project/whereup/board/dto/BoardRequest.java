package com.project.whereup.board.dto;

import com.project.whereup.board.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequest {
    private Long id;
    private String subject;
    private String description;
    private String location;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start_date;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end_date;
    private String brand;
    private String image;
    private Boolean ticket;
    private String link;

    public Board toEntity() {
        return Board.builder()
                .subject(subject)
                .description(description)
                .location(location)
                .start_date(start_date)
                .end_date(end_date)
                .brand(brand)
                .image(image)
                .ticket(ticket)
                .link(link)
                .build();
    }
}
