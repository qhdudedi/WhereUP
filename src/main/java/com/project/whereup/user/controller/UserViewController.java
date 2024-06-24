package com.project.whereup.user.controller;

import com.project.whereup.board.dto.BoardDesc;
import com.project.whereup.board.dto.BoardRequestDto;
import com.project.whereup.board.dto.BoardSummary;
import com.project.whereup.board.service.BoardLikeService;
import com.project.whereup.board.service.BoardService;
import com.project.whereup.post.dto.PostSummary;
import com.project.whereup.post.service.PostService;
import com.project.whereup.oauth.kakao.KakaoService;
import com.project.whereup.user.dto.request.UserRequestDto;
import com.project.whereup.telegram.domain.TelegramUserInfo;
import com.project.whereup.telegram.service.TelegramService;
import com.project.whereup.user.entity.User;
import com.project.whereup.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    //마이페이지 정보 조회
    @GetMapping("/mypage")
    public String getMypage(Model model){
        User loggedInUser = userService.getMyPage();                            //  유저 정보
        model.addAttribute("user",loggedInUser);

        List<PostSummary> list = postService.allSummariesMyPage(loggedInUser.getNickname());
        model.addAttribute("list",list);

        List<BoardRequestDto> likeBoards = boardLikeService.findBoardByUser();  // 관심 팝업 목록
        model.addAttribute("likeBoards", likeBoards);

        TelegramUserInfo telegramUserInfo = telegramService.findUserInfo(loggedInUser.getId());
        model.addAttribute("telegramUserInfo", telegramUserInfo);
        return "mypage";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
    @PostMapping("/signup")
    public ModelAndView signup(@Valid @ModelAttribute UserRequestDto requestDto) {
        userService.save(requestDto);
        return new ModelAndView("redirect:/login");
    }
    @GetMapping("/login")
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/"; // 이미 로그인된 사용자는 홈 페이지로 리다이렉트
        }
        return "login";
    }

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(String code) {
        kakaoService.kakaoLogin(code);
        return "index";
    }


    @PatchMapping("/edit/mypage")
    public String updateMyPage(@RequestBody UserRequestDto userRequestDto, RedirectAttributes redirectAttributes) {
        User updatedUser = userService.updateMyPage(userRequestDto);
        redirectAttributes.addFlashAttribute("user", updatedUser);
        return "redirect:/mypage#myaccount";
    }
    @PostMapping("/edit/mypage")
    public String updateMyPagePost(@ModelAttribute UserRequestDto userRequestDto, RedirectAttributes redirectAttributes) {
        return updateMyPage(userRequestDto, redirectAttributes);
    }

}