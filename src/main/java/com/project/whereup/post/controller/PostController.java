package com.project.whereup.post.controller;

import com.project.whereup.post.domain.Post;
import com.project.whereup.post.dto.PostSummary;
import com.project.whereup.post.repository.PostRepository;
import com.project.whereup.post.service.MarkdownService;
import com.project.whereup.post.service.PostService;
import com.project.whereup.s3.service.S3Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private final PostRepository postRepository;
    // 후기 전체 보기 페이지
    @GetMapping(value = "/postList")
    public String post(Model model) {
        List<PostSummary> list = postService.summeryListPost();
        model.addAttribute("list", list);
//        List<PostImage> imgOrderOne = postService.orderOneImage();
//        List<String> imgList = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            try{
//                String key = imgOrderOne.get(i).getImageName();
//                imgList.add(s3Service.getPresignedUrl(key));
//            } catch(Exception e) {
//                imgList.add("https://via.placeholder.com/100x100.jpg");
//            }
//        }
//        model.addAttribute("imgList", imgList);

        model.addAttribute("detail", "All");
        return "postList.html";
    }
    // 후기 상세 페이지
    @GetMapping(value = "/{postId}")
    public String viewPost(@PathVariable Long postId, Model model) {
        Post post = postService.getPost(postId);
        post.setContent(markdownService.renderMarkdownToHtml(post.getContent())); // content 마크다운 바꿔줘야됨
        model.addAttribute("post", post);
        return "post.html";
    }
    // 신규 후기 작성 페이지
    @GetMapping(value = "/newPost")
    public String newPost() {
        return "newPost.html";
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
    public String newPostGo(@RequestParam String title, @RequestParam String content, @RequestParam String author) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .author(author)
                .created_date(LocalDate.now())
                .updated_date(LocalDate.now())
                .build();
        Post savedPost = postService.insert(post);
        Long postId = savedPost.getId();
        return "redirect:/post/" + postId;
    }
    // 후기 수정 페이지
    @GetMapping(value = "/updatePost/{postId}")
    public String updatePost(@PathVariable Long postId, Model model) {
        Post post = postService.getPost(postId);
        model.addAttribute("post", post);
        return "updatePost.html";
    }
    // 후기 수정 후 처리하는 거
    @PostMapping(value = "/updatePost")
    public String updatePostGo(@RequestParam("id") Long id,
                               @RequestParam("title") String title,
                               @RequestParam("content") String content,
                               @RequestParam("author") String author,
                               @RequestParam("created_date") LocalDate created_date) {

        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post not found"));
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        post.setCreated_date(created_date);
        post.setUpdated_date(LocalDate.now());
        postRepository.save(post);
        return "redirect:/post/"+id;
    }
    // 후기 삭제
    @PostMapping("/deletePost")
    public String deletePost(@RequestParam("id") Long id) {
        System.out.println(id);
        postService.delete(id);
        return "redirect:/post/postList";
    }
}