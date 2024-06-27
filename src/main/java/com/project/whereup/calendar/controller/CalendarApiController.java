package com.project.whereup.calendar.controller;

import com.project.whereup.calendar.dto.CalendarRequestDto;
import com.project.whereup.calendar.entity.Calendar;
import com.project.whereup.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CalendarApiController {

    private final CalendarService calendarService;

    @PostMapping("/calendar")
    public ResponseEntity<?> create(@RequestBody CalendarRequestDto requestDto, Principal principal) {
        calendarService.createEvent(requestDto, principal.getName());
        return ResponseEntity.ok("success");
    }

    @PatchMapping("/calendar/edit/{eventId}")
    public ResponseEntity<?> update(@PathVariable Long eventId, @RequestBody CalendarRequestDto requestDto) {
        Calendar event = calendarService.edit(eventId, requestDto);
        return ResponseEntity.ok().body(event);
    }

    @DeleteMapping("/calendar/{eventId}")
    public ResponseEntity<Void> delete(@PathVariable Long eventId) {
        calendarService.delete(eventId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/calendar/events")
    public List<Calendar> getEventsByUser() {
        return calendarService.findEventByUser();
    }

}

