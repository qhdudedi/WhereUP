package com.project.whereup.post.controller;

import com.project.whereup.board.service.BoardService;
import com.project.whereup.post.domain.Comment;
import com.project.whereup.post.domain.Post;
import com.project.whereup.post.dto.PostSummary;
import com.project.whereup.post.service.CommentService;
import com.project.whereup.post.service.MarkdownService;
import com.project.whereup.post.service.PostService;
import com.project.whereup.user.entity.User;
import com.project.whereup.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final MarkdownService markdownService;
    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;
    private final BoardService boardService;

    // 후기 전체 보기 페이지
    @GetMapping(value = "/postList")
    public String post(Model model, Principal principal, @RequestParam int page) {
        if(principal != null) {
            User loggedInUser = userService.getMyPage();
            model.addAttribute("user", loggedInUser != null ? loggedInUser.getNickname() : null);
        }
        int howManyOnePage = 18;
        List<PostSummary> list = postService.summaryListPage("", page, howManyOnePage);
        //// 썸네일 없는 애들은?
        int pageCount = postService.pageCount("",howManyOnePage);
        model.addAttribute("list", list);
        model.addAttribute("detail", "All");
        model.addAttribute("page", page);
        model.addAttribute("pageCount", pageCount);
        return "postList";
    }
    // 후기 상세 페이지
    @GetMapping(value = "/post/{postId}")
    public String viewPost(@PathVariable Long postId, Model model, Principal principal) {
        if(principal != null) {
            User loggedInUser = userService.getMyPage();
            model.addAttribute("user", loggedInUser != null ? loggedInUser.getNickname() : null);
        }
        Post post = postService.getPost(postId);
        post.setContent(markdownService.renderMarkdownToHtml(post.getContent()));
        model.addAttribute("post", post);
        List<Comment> comments = commentService.getComments(postId);
        model.addAttribute("comments", comments);
        return "post";
    }
    // 신규 후기 작성 페이지
    @GetMapping(value = "/post/newPost")
    public String newPost(Model model, Principal principal) {
        User loggedInUser = userService.getMyPage();
        if (principal == null) {
            return "redirect:/postList"; // 로그인 해야만 신규후기 작성 가능
        }
        model.addAttribute("author", loggedInUser.getNickname());

        model.addAttribute("brandList", boardService.brandList());
        return "newPost";
    }
    // 신규 후기 작성 후 제출하고 나서 처리하는 거
    @PostMapping(value = "/post/newPost")
    public String newPostGo(@RequestParam String title,
                            @RequestParam String content,
                            @RequestParam String author,
                            @RequestParam String brand) {
        // content 뒤져보기 -> 이미지가 있다? -> 썸네일
        String thumbnail = markdownService.isImageInContent(content);
        if(thumbnail == null) {
            /////////////////썸네일 없을 때 뭐해야됨?
        }
        Post post = Post.builder()
                .title(title)
                .content(markdownService.domainChange(content))
                .author(author)
                .created_date(LocalDate.now())
                .updated_date(LocalDate.now())
                .thumbnail(thumbnail)
                .brand(brand)
                .build();
        Post savedPost = postService.insert(post);
        Long postId = savedPost.getId();
        return "redirect:/post/" + postId;
    }
    // 후기 수정 페이지
    @PostMapping(value = "/post/updatePost/{postId}")
    public String updatePost(@PathVariable Long postId, Model model) {
        Post post = postService.getPost(postId);
        User loggingedInUser = userService.getMyPage();
        if(!loggingedInUser.getNickname().equals(post.getAuthor())) {
            return "redirect:/post/" + postId; // 작성자와 현재 로그인 유저가 같지 않으면 수정불가
        }
        model.addAttribute("post", post);
        model.addAttribute("brandList", boardService.brandList());
        return "updatePost";
    }
    // 후기 수정 후 처리하는 거
    @PostMapping(value = "/post/updatePost")
    public String updatePostGo(@RequestParam Long id,
                               @RequestParam String title,
                               @RequestParam String content,
                               @RequestParam String author,
                               @RequestParam LocalDate created_date,
                               @RequestParam String brand) {

        Post post = postService.getPost(id);
        post.setTitle(title);
        post.setContent(markdownService.domainChange(content));
        post.setAuthor(author);
        post.setCreated_date(created_date);
        post.setUpdated_date(LocalDate.now());
        post.setBrand(brand);
        // content 뒤져보기 -> 이미지가 있다? -> 썸네일
        String tmp = markdownService.isImageInContent(content);
        if(tmp == null) {
            /////////////////썸네일 없을 때 뭐해야됨?
        } else {
            post.setThumbnail(tmp);
        }
        postService.update(post);
        return "redirect:/post/"+id;
    }
    // 후기 삭제
    @PostMapping("/post/deletePost/{postId}")
    public String deletePost(@PathVariable Long postId) {
        Post post = postService.getPost(postId);
        User loggingedInUser = userService.getMyPage();
        if(!loggingedInUser.getNickname().equals(post.getAuthor())) {
            return "redirect:/post/" + postId; // 작성자와 현재 로그인 유저가 같지 않으면 삭제불가
        }
        postService.delete(postId);
        commentService.deleteByPostId(postId);
        //////////////////// S3 삭제

        return "redirect:/postList?page=1";
    }
}