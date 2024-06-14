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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/board")
public class BoardController {
    private final BoardService boardService;
    private final S3Service s3Service;
    private final BoardLikeService boardLikeService;

    //전체보기 페이지, 필요한 것만 추려서 리스트로 뽑는용도
    @GetMapping(value = "/boardList")
    public String board(Model model) {
        List<BoardSummary> list = boardService.summeryListBoard();
        model.addAttribute("list", list);
        List<BoardImage> imgOrderOne = boardService.orderOneImage();
        List<String> imgList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            try{
                String key = imgOrderOne.get(i).getImageName();
//                imgList.add(s3Service.getPresignedUrl(key));
                imgList.add(s3Service.getImageUrl(key));
            } catch(Exception e) {
                imgList.add("https://via.placeholder.com/100x100.jpg");
            }
        }
        model.addAttribute("imgList", imgList);
        model.addAttribute("detail", "All");
        return "boardList";
    }
    //한 개 보기 페이지 -> 관심 팝업 정보 추가
    @GetMapping("/{boardId}")
    public String board(@PathVariable Long boardId, Model model, Principal principal) {
        Board board = boardService.getBoard(boardId);
        model.addAttribute("board", board);

        List<BoardImage> imgList = boardService.listImage(boardId);
        String img_url[] = new String[imgList.size()];
        for (int i = 0; i < img_url.length; i++) {
            String key = imgList.get(i).getImageName();
            img_url[i] = s3Service.getImageUrl(key);
        }
        model.addAttribute("img_url", img_url);

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