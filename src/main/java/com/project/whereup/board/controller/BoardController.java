package com.project.whereup.board.controller;

import com.project.whereup.board.domain.Board;
import com.project.whereup.board.domain.BoardImage;
import com.project.whereup.board.dto.BoardSummary;
import com.project.whereup.board.service.BoardService;
import com.project.whereup.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/board")
public class BoardController {
    private final BoardService boardService;
    private final S3Service s3Service;

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
        return "boardList.html";
    }
    //한개보기 페이지
    @GetMapping(value = "/{boardId}")
    public String board(@PathVariable Long boardId, Model model) {
        Board board = boardService.getBoard(boardId);
        model.addAttribute("board", board);
        List<BoardImage> imgList = boardService.listImage(boardId);

        String img_url[] = new String[imgList.size()];
        for (int i = 0; i < img_url.length; i++) {
            String key = imgList.get(i).getImageName();
            img_url[i] = s3Service.getImageUrl(key);
        }
        model.addAttribute("img_url", img_url);
        return "board.html";
    }
}