package com.project.whereup.telegram.service;

import com.project.whereup.board.domain.Board;
import com.project.whereup.board.domain.Category;
import com.project.whereup.board.service.BoardService;
import com.project.whereup.telegram.domain.TelegramUserInfo;
import com.project.whereup.telegram.repository.TelegramUserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramService {
    private final TelegramUserInfoRepository telegramUserInfoRepository;
    @Value("${telegram.send-url}")
    private String telegramURL;

    public void saveUserInfo(TelegramUserInfo userInfo) {
        telegramUserInfoRepository.save(userInfo);
    }

    public TelegramUserInfo findUserInfo(Long userId) {
        return telegramUserInfoRepository.findByUserId(userId);
    }

    public void send(Board board) {
        String categoryKor = board.getCategory().toString();
        List<String> chatIds = categoryKor.equals("문화") ? telegramUserInfoRepository.findChatIdByCulture() :
                                categoryKor.equals("패션") ? telegramUserInfoRepository.findChatIdByFashion() :
                                telegramUserInfoRepository.findChatIdByLife();

        String msg = "알림서비스 도착!\n\"" + board.getCategory() + "\"카테고리의 신규 팝업\n" +
                board.getSubject() + "\nwww.whereupp.com/board/" + board.getId() + "\n확인해 주세요!";

        for (String chatId : chatIds) {
            try {
                String encodedString = URLEncoder.encode(msg, StandardCharsets.UTF_8.toString());
                URL url = new URL(telegramURL + "?chat_id=" + chatId + "&text=" + encodedString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.getResponseCode();
                conn.disconnect();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
