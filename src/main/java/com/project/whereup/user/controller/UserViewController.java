package com.project.whereup.user.controller;

import com.project.whereup.board.dto.BoardDesc;
import com.project.whereup.board.dto.BoardRequestDto;
import com.project.whereup.board.dto.BoardSummary;
import com.project.whereup.board.service.BoardLikeService;
import com.project.whereup.board.service.BoardService;
import com.project.whereup.post.dto.PostSummary;
import com.project.whereup.post.service.PostService;
import com.project.whereup.oauth.kakao.KakaoService;
import com.project.whereup.telegram.domain.TelegramUserInfo;
import com.project.whereup.telegram.service.TelegramService;
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

    private final BoardLikeService boardLikeService;
    private final KakaoService kakaoService;
    private final UserService userService;
    private final PostService postService;
    private final BoardService boardService;
    private final TelegramService telegramService;

    @GetMapping("/")
    public String main(Model model) {
        List<BoardDesc> summariesTop = boardService.mainTopSlide(4);
        model.addAttribute("summariesTop", summariesTop);

        List<BoardSummary> summaries = boardService.dateRangeBoard();
        model.addAttribute("summaries", summaries);

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
        User loggedInUser = userService.getMyPage();                            //  유저 정보
        model.addAttribute("user",loggedInUser);

        List<PostSummary> list = postService.allSummariesMyPage(loggedInUser.getEmail());
        model.addAttribute("list",list);

        List<BoardRequestDto> likeBoards = boardLikeService.findBoardByUser();  // 관심 팝업 목록
        model.addAttribute("likeBoards", likeBoards);

        TelegramUserInfo telegramUserInfo = telegramService.findUserInfo(loggedInUser.getId());
        model.addAttribute("telegramUserInfo", telegramUserInfo);
        return "mypage";
    }
}