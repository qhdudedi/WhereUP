package com.project.whereup.calendar.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CalendarRequestDto {

    private String title;
    private String summary;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
