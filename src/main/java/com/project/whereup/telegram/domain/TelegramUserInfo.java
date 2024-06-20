package com.project.whereup.telegram.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TelegramUserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private long id;
    @Column(name = "userId", nullable = false, updatable = false, unique = true)
    private Long userId;
    @Column(name = "chatId", unique = true)
    private String chatId;
    @Column(name = "culture")
    private boolean culture;
    @Column(name = "fashion")
    private boolean fashion;
    @Column(name = "life")
    private boolean life;

    @Builder
    public TelegramUserInfo(Long userId, String chatId, boolean culture, boolean fashion, boolean life) {
        this.userId = userId;
        this.chatId = chatId;
        this.culture = culture;
        this.fashion = fashion;
        this.life = life;
    }
}
