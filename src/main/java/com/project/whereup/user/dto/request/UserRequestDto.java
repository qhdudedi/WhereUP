package com.project.whereup.user.dto.request;

import com.project.whereup.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private String name;

    private String email;

    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    private String nickname;

    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .birth(birth)
                .nickname(nickname)
                .build();

    }
}
