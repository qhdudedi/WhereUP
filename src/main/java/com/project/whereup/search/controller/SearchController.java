package com.project.whereup.search.controller;

import com.project.whereup.board.dto.BoardSummary;
import com.project.whereup.board.service.BoardService;
import com.project.whereup.post.dto.PostSummary;
import com.project.whereup.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {
    private final BoardService boardService;
    private final PostService postService;
    // front header 검색창에서도 메소드 get으로 바꿔야됨, 검색창이 공백인데 검색한다면?
    @GetMapping(value = "/search")
    public String search(@RequestParam String keyword, Model model) {
        if (keyword == null || keyword.isEmpty()) {
            return "redirect:/";
        }
        List<BoardSummary> summaries = boardService.summaryListPage(keyword, 1);
        List<PostSummary> list = postService.summaryListPage(keyword, 1, 12);

        model.addAttribute("keyword", keyword);
        model.addAttribute("summaries", summaries);
        model.addAttribute("list", list);
        return "search";
    }
    @GetMapping(value = "/board/search/{keyword}")
    public String searchBoard(@PathVariable String keyword, Model model, @RequestParam int page) {
        List<BoardSummary> summaries = boardService.summaryListPage(keyword, page);
        int pageCount = boardService.pageCount(keyword);

        model.addAttribute("summaries", summaries);
        model.addAttribute("detail", "Search");
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("pageCount", pageCount);

        return "boardList";
    }


    @GetMapping(value = "/post/search/{keyword}")
    public String searchPost(@PathVariable String keyword, Model model, Principal principal, @RequestParam int page) {
        model.addAttribute("user", principal != null ? principal.getName() : null);
        int howManyOnePage = 18;
        List<PostSummary> list = postService.summaryListPage(keyword, page, howManyOnePage);
        int pageCount = postService.pageCount(keyword, howManyOnePage);
        model.addAttribute("list", list);
        model.addAttribute("detail", "Search");
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("pageCount", pageCount);
        return "postList";
    }
}
