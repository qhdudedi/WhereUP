package com.project.whereup.telegram.controller;

import com.project.whereup.board.domain.Board;
import com.project.whereup.board.domain.Category;
import com.project.whereup.board.service.BoardService;
import com.project.whereup.telegram.domain.TelegramUserInfo;
import com.project.whereup.telegram.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class TelegramController {
    private final TelegramService telegramService;
    private final BoardService boardService;

    @PostMapping(value = "/telegram/getUserInfo")
    public String getUserInfo(@RequestParam Long userId,
                              @RequestParam String chatId,
                              @RequestParam(required = false, defaultValue = "false") boolean culture,
                              @RequestParam(required = false, defaultValue = "false") boolean fashion,
                              @RequestParam(required = false, defaultValue = "false") boolean life) {
        if(telegramService.findUserInfo(userId) == null) {
            TelegramUserInfo userInfo = TelegramUserInfo.builder()
                    .userId(userId)
                    .chatId(chatId)
                    .culture(culture)
                    .fashion(fashion)
                    .life(life)
                    .build();

            telegramService.saveUserInfo(userInfo);
        } else {
            TelegramUserInfo old = telegramService.findUserInfo(userId);
            old.setChatId(chatId);
            old.setCulture(culture);
            old.setFashion(fashion);
            old.setLife(life);
            telegramService.saveUserInfo(old);
        }
        return "redirect:/mypage";
    }

    @GetMapping(value = "/adminInsertBoard")
    public String adminInsertBoard() {
        return "adminInsertBoard";
    }
    @PostMapping(value = "/adminInsertBoard")
    public String adminInsertBoard2(@RequestParam String subject,
                                    @RequestParam String description,
                                    @RequestParam String location,
                                    @RequestParam LocalDate start_date,
                                    @RequestParam LocalDate end_date,
                                    @RequestParam String brand,
                                    @RequestParam String link,
                                    @RequestParam(required = false, defaultValue = "false") boolean ticket,
                                    @RequestParam Category category) {
        Board board = Board.builder()
                .subject(subject)
                .description(description)
                .ticket(ticket)
                .start_date(start_date)
                .category(category)
                .link(link)
                .location(location)
                .end_date(end_date)
                .brand(brand)
                .build();
        boardService.save(board);

        ////텔레그램 알림 전송하기
        telegramService.send(board);
        return "redirect:/adminInsertBoard";
    }
}
