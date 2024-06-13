package com.project.whereup.user.controller;

import com.project.whereup.oauth.kakao.KakaoService;
import com.project.whereup.post.dto.PostSummary;
import com.project.whereup.post.service.PostService;
import com.project.whereup.user.entity.User;
import com.project.whereup.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserViewController {

    private final KakaoService kakaoService;
    private final UserService userService;
    private final PostService postService;

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
    //마이페이지 정보 조회
    @GetMapping("/mypage")
    public String getMypage(Model model){
        User loggedInUser = userService.getMyPage();
        model.addAttribute("user",loggedInUser);

        List<PostSummary> list = postService.allSummariesMyPage(loggedInUser.getEmail());
        model.addAttribute("list",list);
        return "mypage";
    }
}