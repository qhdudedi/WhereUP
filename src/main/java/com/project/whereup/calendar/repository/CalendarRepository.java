package com.project.whereup.calendar.repository;

import com.project.whereup.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    List<Calendar> findByUserId(Long userId);
}
