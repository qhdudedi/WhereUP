package com.project.whereup.board.domain;

import com.project.whereup.board.dto.BoardRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
    //구별용 아이디, 이름, 팝업소개, 위치, 날짜, 주관, 이미지, 티켓필요유무, sns 링크
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;
    @Column(name = "subject", nullable = false)
    private String subject;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "location", nullable = false)
    private String location;
    @Column(name = "start_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start_date;
    @Column(name = "end_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end_date;
    @Column(name = "brand", nullable = false)
    private String brand;
    @Column(name = "ticket", nullable = false)
    private Boolean ticket;
    @Column(name = "link", nullable = false)
    private String link;

    @Builder
    public Board(String subject, String description, String location, LocalDate start_date, LocalDate end_date, String brand, Boolean ticket, String link) {
        this.subject = subject;
        this.description = description;
        this.location = location;
        this.start_date = start_date;
        this.end_date = end_date;
        this.brand = brand;
        this.ticket = ticket;
        this.link = link;
    }

    public void update(BoardRequest request) {
        this.subject = request.getSubject();
        this.description = request.getDescription();
        this.location = request.getLocation();
        this.start_date = request.getStart_date();
        this.end_date = request.getEnd_date();
        this.brand = request.getBrand();
        this.ticket = request.getTicket();
        this.link = request.getLink();
    }
}