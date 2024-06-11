package com.project.whereup.calendar.service;

import com.project.whereup.calendar.dto.CalendarRequestDto;
import com.project.whereup.calendar.entity.Calendar;
import com.project.whereup.calendar.repository.CalendarRepository;
import com.project.whereup.user.entity.User;
import com.project.whereup.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;

    //일정 등록
    public Long createEvent(CalendarRequestDto requestDto, String email) {
        User user = userRepository.findByEmail(email);
        return calendarRepository.save(
                Calendar.builder()
                        .title(requestDto.getTitle())
                        .summary(requestDto.getSummary())
                        .startDate(requestDto.getStartDate())
                        .endDate(requestDto.getEndDate())
                        .user(user)
                        .build()
        ).getId();
    }

    public Calendar edit(Long id, CalendarRequestDto requestDto){
        Calendar event = calendarRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("not found your event"));
        event.edit(requestDto.getTitle(), requestDto.getSummary(), requestDto.getStartDate(), requestDto.getEndDate());
        return calendarRepository.save(event);
    }

    public void delete(Long id){
        calendarRepository.deleteById(id);
    }

    @Transactional
    public List<Calendar> findEventByUser() {
        // 현재 로그인한 유저의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;

        if (authentication.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            username = authentication.getPrincipal().toString();
        }

        // username으로 유저 찾기
        String finalUsername = username;
        User user = userRepository.findByEmail(username);

        // 유저의 아이디로 캘린더 이벤트 찾기
        return calendarRepository.findByUserId(user.getId());
    }
}
