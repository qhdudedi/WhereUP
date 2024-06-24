package com.project.whereup.telegram.repository;

import com.project.whereup.board.domain.Category;
import com.project.whereup.telegram.domain.TelegramUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelegramUserInfoRepository extends JpaRepository<TelegramUserInfo, Long> {
    TelegramUserInfo findByUserId(Long userId);

    @Query("SELECT t.chatId FROM TelegramUserInfo t WHERE t.culture is true")
    List<String> findChatIdByCulture();

    @Query("SELECT t.chatId FROM TelegramUserInfo t WHERE t.fashion is true")
    List<String> findChatIdByFashion();

    @Query("SELECT t.chatId FROM TelegramUserInfo t WHERE t.life is true")
    List<String> findChatIdByLife();

    TelegramUserInfo findByChatId(String chatId);
}
