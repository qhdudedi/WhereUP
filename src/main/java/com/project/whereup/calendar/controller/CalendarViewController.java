package com.project.whereup.calendar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CalendarViewController {

    @GetMapping("/calendar")
    public String createEvent() {
        return "calendar";
    }

}
