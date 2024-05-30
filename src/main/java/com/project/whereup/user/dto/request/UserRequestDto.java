package com.project.whereup.user.dto.request;

import com.project.whereup.user.entity.User;
import com.project.whereup.user.entity.eum.Role;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@Getter
@Setter
public class UserRequestDto {
    private String name;

    private String email;

    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    private String nickname;

    public User toEntity(BCryptPasswordEncoder encoder){
        return User.builder()
                .name(name)
                .email(email)
                .password(encoder.encode(password))
                .birth(birth)
                .nickname(nickname)
                .role(Role.USER)
                .build();

    }
}
