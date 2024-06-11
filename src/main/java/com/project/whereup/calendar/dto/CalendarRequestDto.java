package com.project.whereup.calendar.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CalendarRequestDto {

    private String title;
    private String summary;
    private LocalDate startDate;
    private LocalDate endDate;

}
