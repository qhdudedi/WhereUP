package com.project.whereup.board.dto;

import com.project.whereup.board.domain.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class BoardResponse {
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

    public BoardResponse(Board board) {
        this.subject = board.getSubject();
        this.description = board.getDescription();
        this.location = board.getLocation();
        this.start_date = board.getStart_date();
        this.end_date = board.getEnd_date();
        this.brand = board.getBrand();
        this.image = board.getImage();
        this.ticket = board.getTicket();
        this.link = board.getLink();
    }
}
