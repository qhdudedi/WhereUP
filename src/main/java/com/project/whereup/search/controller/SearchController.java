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
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SearchController {
    private final BoardService boardService;
    private final PostService postService;
    private final S3Service s3Service;

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
    public String searchBoard(@PathVariable String keyword, Model model, @RequestParam int page) {
        keyword = keyword.replaceAll("_____", " ");
        Map<BoardSummary, String> map = boardService.summaryListPage(keyword, page);

        List<BoardSummary> summaries = new ArrayList<>(map.keySet());
        List<String> images = new ArrayList<>();

        for (String imageName : map.values()) {
            images.add(s3Service.getImageUrl(imageName));
        }

        int pageCount = boardService.pageCount(keyword);
        model.addAttribute("summaries", summaries);
        model.addAttribute("images", images);
        model.addAttribute("detail", "Search");
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("pageCount", pageCount);

        return "boardList";
    }


    @GetMapping(value = "/post/search/{keyword}")
    public String searchPost(@PathVariable String keyword, Model model, Principal principal, @RequestParam int page) {
        model.addAttribute("user", principal != null ? principal.getName() : null);
        keyword = keyword.replaceAll("_____", " ");
        List<PostSummary> list = postService.summaryListPage(keyword, page);

        int pageCount = postService.pageCount(keyword);
        model.addAttribute("list", list);
        model.addAttribute("detail", "Search");
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("pageCount", pageCount);
        return "postList";
    }
}
