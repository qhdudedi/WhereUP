package com.project.whereup.member.dto.request;

import com.project.whereup.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {
    private String name;

    private String email;

    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    private String nickname;

    public Member toEntity(){
        return Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .birth(birth)
                .nickname(nickname)
                .build();

    }
}
