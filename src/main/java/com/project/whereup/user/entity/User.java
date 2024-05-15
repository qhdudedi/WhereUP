package com.project.whereup.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="email",nullable = false)
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="birth")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    @Column(name="nickname",nullable = false)
    private String nickname;

    @Builder
    public User(Long id, String name, String email, String password, LocalDate birth, String nickname){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.birth = birth;
        this.nickname = nickname;
    }

    public void update(String name, String email, String password, LocalDate birth, String nickname){
        this.name = name;
        this.email = email;
        this.password = password;
        this.birth = birth;
        this.nickname = nickname;
    }
}
