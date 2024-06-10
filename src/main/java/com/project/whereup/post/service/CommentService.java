package com.project.whereup.post.service;

import com.project.whereup.post.domain.Comment;
import com.project.whereup.post.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> getComments(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public void add(Comment comment) {
        commentRepository.save(comment);
    }
    public Comment getComment(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public void edit(Comment comment) {
        commentRepository.save(comment);
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    public void deleteByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        for (Comment comment : comments) {
            commentRepository.delete(comment);
        }
    }
}
