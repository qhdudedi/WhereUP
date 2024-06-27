package com.project.whereup.user.dto.request;

import com.project.whereup.user.entity.User;
import com.project.whereup.user.entity.eum.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@Getter
@Setter
public class UserRequestDto {
    private String name;

    @NotBlank(message = "이메일 주소를 입력해주세요.")
    @Email(message="올바른 이메일 주소를 입력해주세요.")
    private String email;

    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
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
