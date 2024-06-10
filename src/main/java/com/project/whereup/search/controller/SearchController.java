package com.project.whereup.search.controller;

import com.project.whereup.board.domain.BoardImage;
import com.project.whereup.board.dto.BoardSummary;
import com.project.whereup.board.service.BoardService;
import com.project.whereup.post.dto.PostSummary;
import com.project.whereup.post.service.PostService;
import com.project.whereup.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {
    private final BoardService boardService;
    private final PostService postService;
    private final S3Service s3Service;

    @GetMapping(value = "/search")
    public String test() {
        return "search.html";
    }

    @PostMapping(value = "/search")
    public String search(@RequestParam String what, @RequestParam String keyword) {
        keyword = keyword.replaceAll(" ", "_____");
        try {
            keyword = URLEncoder.encode(keyword, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/" + what.toLowerCase() + "/search/" + keyword;
    }
    @GetMapping(value = "/board/search/{keyword}")
    public String searchBoard(@PathVariable String keyword, Model model) {
        keyword = keyword.replaceAll("_____", " ");
        List<BoardSummary> allList = boardService.summeryListBoard();
        List<BoardImage> allImgOrderOne = boardService.orderOneImage();
        List<BoardSummary> list = new ArrayList<>();
        List<String> imgList = new ArrayList<>();
        for (int i = 0; i < allList.size(); i++) {
            if(allList.get(i).getSubject().toUpperCase().contains(keyword.toUpperCase())) {
                list.add(allList.get(i));
                try{
                    String key = allImgOrderOne.get(i).getImageName();
                    imgList.add(s3Service.getPresignedUrl(key));
                } catch (Exception e) {
                    imgList.add("https://via.placeholder.com/100x100.jpg");
                }
            }
        }
        model.addAttribute("list", list);
        model.addAttribute("imgList", imgList);
        model.addAttribute("detail", "search(" + keyword + ")");

        return "boardList.html";
    }
    @GetMapping(value = "/post/search/{keyword}")
    public String searchPost(@PathVariable String keyword, Model model) {
        keyword = keyword.replaceAll("_____", " ");
        List<PostSummary> allList = postService.summeryListPost();
        List<PostSummary> list = new ArrayList<>();
        for (int i = 0; i < allList.size(); i++) {
            if(allList.get(i).getTitle().toUpperCase().contains(keyword.toUpperCase())) {
                list.add(allList.get(i));
            }
        }
        model.addAttribute("list", list);
        model.addAttribute("detail", "search(" + keyword + ")");
        return "postList.html";
    }
}
