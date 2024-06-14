package com.project.whereup.board.controller;

import com.project.whereup.board.domain.Board;
import com.project.whereup.board.domain.BoardImage;
import com.project.whereup.board.dto.BoardSummary;
import com.project.whereup.board.service.BoardLikeService;
import com.project.whereup.board.service.BoardService;
import com.project.whereup.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/board")
public class BoardController {
    private final BoardService boardService;
    private final S3Service s3Service;
    private final BoardLikeService boardLikeService;

    //전체보기 페이지, 필요한 것만 추려서 리스트로 뽑는용도
    @GetMapping(value = "/boardList")
    public String board(Model model, @RequestParam int page) {
        Map<BoardSummary, String> map = boardService.summaryListPage("", page);
        List<BoardSummary> summaries = new ArrayList<>(map.keySet());
        List<String> images = new ArrayList<>();

        for(String imageName : map.values()){
            images.add(s3Service.getImageUrl(imageName));
        }

        int pageCount = boardService.pageCount("");
        model.addAttribute("summaries", summaries);
        model.addAttribute("images", images);
        model.addAttribute("detail", "All");
        model.addAttribute("page", page);
        model.addAttribute("pageCount", pageCount);
        return "boardList";
    }
    //한 개 보기 페이지 -> 관심 팝업 정보 추가
    @GetMapping("/{boardId}")
    public String board(@PathVariable Long boardId, Model model, Principal principal) {
        Board board = boardService.getBoard(boardId);

        List<BoardImage> imgList = boardService.listImage(boardId);
        String[] img_url = new String[imgList.size()];
        for (int i = 0; i < img_url.length; i++) {
            String key = imgList.get(i).getImageName();
            img_url[i] = s3Service.getImageUrl(key);
        }
        model.addAttribute("img_url", img_url);
        model.addAttribute("board", board);

        boolean isLiked = false;
        if (principal != null) {
            String email = principal.getName();
            isLiked = boardLikeService.isBoardLikedByUser(email, boardId);
        }
        model.addAttribute("isLiked", isLiked);

        return "board";
    }
    // 관심팝업 등록
    @PostMapping("/{boardId}/like")
    public String likeBoard(@PathVariable Long boardId, Principal principal) {
        String email = principal.getName();
        boardLikeService.createBoardLike(email, boardId);
        return "redirect:/board/" + boardId; // 좋아요 후 현재 페이지로 리다이렉트
    }

}