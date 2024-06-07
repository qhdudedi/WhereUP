package com.project.whereup.post.controller;

import com.project.whereup.post.domain.Comment;
import com.project.whereup.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/post/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping(value = "/add")
    public String addComment(@RequestParam("postId") Long postId,
                             @RequestParam("text") String text,
                             Principal principal) {
        Comment comment = Comment.builder()
                            .postId(postId)
                            .user(principal.getName())
                            .text(text)
                            .created_date(LocalDate.now())
                            .updated_date(LocalDate.now()).build();
        commentService.add(comment);
        return "redirect:/post/" + postId;
    }
    @PostMapping(value = "/edit")
    public String editComment(@RequestParam("commentId") Long id,
                              @RequestParam("commentText") String text) {
        Comment comment = commentService.getComment(id);
        comment.setText(text);
        comment.setUpdated_date(LocalDate.now());
        commentService.edit(comment);
        return "redirect:/post/" + comment.getPostId();
    }
    @GetMapping(value = "/delete/{commentId}")
    public String deleteComment(@PathVariable Long commentId) {
        Long postId = commentService.getComment(commentId).getPostId();
        commentService.delete(commentId);
        return "redirect:/post/" + postId;
    }
}
