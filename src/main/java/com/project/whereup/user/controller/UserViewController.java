package com.project.whereup.user.controller;

import com.project.whereup.oauth.kakao.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserViewController {

    private final KakaoService kakaoService;

    @GetMapping("/")
    public String main() {
        return "index";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(String code) {
        kakaoService.kakaoLogin(code);
        return "index";
    }

}