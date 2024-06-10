package com.project.whereup.post.controller;

import com.project.whereup.post.domain.Comment;
import com.project.whereup.post.domain.Post;
import com.project.whereup.post.dto.PostSummary;
import com.project.whereup.post.service.CommentService;
import com.project.whereup.post.service.MarkdownService;
import com.project.whereup.post.service.PostService;
import com.project.whereup.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/post")
public class PostController {
    private final MarkdownService markdownService;
    private final S3Service s3Service;
    private final PostService postService;
    private final CommentService commentService;

    // 후기 전체 보기 페이지
    @GetMapping(value = "/postList")
    public String post(Model model, Principal principal) {
        model.addAttribute("user", principal != null ? principal.getName() : null);
        System.out.println(principal != null ? principal.getName() : null);
        List<PostSummary> list = postService.summeryListPost();
        model.addAttribute("list", list);
        return "postList";
    }
    // 후기 상세 페이지
    @GetMapping(value = "/{postId}")
    public String viewPost(@PathVariable Long postId, Model model, Principal principal) {
        model.addAttribute("user", principal != null ? principal.getName() : null);
        Post post = postService.getPost(postId);
        post.setContent(markdownService.renderMarkdownToHtml(post.getContent())); // content 마크다운 바꿔줘야됨
        model.addAttribute("post", post);
        List<Comment> comments = commentService.getComments(postId);
        model.addAttribute("comments", comments);
        return "post";
    }
    // 신규 후기 작성 페이지
    @GetMapping(value = "/newPost")
    public String newPost(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/post/postList"; // 로그인 해야만 신규후기 작성 가능
        }
        model.addAttribute("author", principal.getName());
        return "newPost";
    }
    // 이미지 파일 업로드
    @PostMapping("/upload")
    @ResponseBody
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        if (file.isEmpty()) {
            response.put("success", false);
            response.put("url", "https://via.placeholder.com/100x100.jpg");
        } else {
            response.put("success", true);
            response.put("url", s3Service.upload(file));
        }
        return response;
    }
    // 신규 후기 작성 후 제출하고 나서 처리하는 거
    @PostMapping(value = "/newPost")
    public String newPostGo(@RequestParam String title,
                            @RequestParam String content,
                            @RequestParam String author) {
        // content 뒤져보기 -> 이미지가 있다? -> 썸네일
        String thumbnail = markdownService.isImageInContent(content);
        if(thumbnail == null) {
            /////////////////썸네일 없을 때 뭐해야됨?
        }
        Post post = Post.builder()
                .title(title)
                .content(content)
                .author(author)
                .created_date(LocalDate.now())
                .updated_date(LocalDate.now())
                .thumbnail(thumbnail)
                .build();
        Post savedPost = postService.insert(post);
        Long postId = savedPost.getId();
        return "redirect:/post/" + postId;
    }
    // 후기 수정 페이지
    @PostMapping(value = "/updatePost/{postId}")
    public String updatePost(@PathVariable Long postId, Model model, Principal principal) {
        Post post = postService.getPost(postId);
        if(!principal.getName().equals(post.getAuthor())) {
            return "redirect:/post/" + postId; // 작성자와 현재 로그인 유저가 같지 않으면 수정불가
        }
        model.addAttribute("post", post);
        return "updatePost";
    }
    // 후기 수정 후 처리하는 거
    @PostMapping(value = "/updatePost")
    public String updatePostGo(@RequestParam Long id,
                               @RequestParam String title,
                               @RequestParam String content,
                               @RequestParam String author,
                               @RequestParam LocalDate created_date) {

        Post post = postService.getPost(id);
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        post.setCreated_date(created_date);
        post.setUpdated_date(LocalDate.now());
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
    @PostMapping("/deletePost/{postId}")
    public String deletePost(@PathVariable Long postId, Principal principal) {
        Post post = postService.getPost(postId);
        if(!principal.getName().equals(post.getAuthor())) {
            return "redirect:/post/" + postId; // 작성자와 현재 로그인 유저가 같지 않으면 삭제불가
        }
        postService.delete(postId);
        commentService.deleteByPostId(postId);
        //////////////////// S3 삭제

        return "redirect:/post/postList";
    }
}