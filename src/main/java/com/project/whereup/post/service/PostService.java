package com.project.whereup.post.service;

import com.project.whereup.post.domain.Post;
import com.project.whereup.post.domain.PostImage;
import com.project.whereup.post.repository.PostImageRepository;
import com.project.whereup.post.repository.PostRepository;
import com.project.whereup.post.dto.PostSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    // 후기 하나 아이디로 찾기
    public Post getPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("wrong postId"));
    }
    // 후기 전체 보기 페이지에서 띄울것들
    public List<PostSummary> summeryListPost() {
        return postRepository.findSummary();
    }
    // 신규 후기 저장
    public Post insert(Post post) {
        return postRepository.save(post);
    }
    // 후기 수정
    public void update(Long id, Post post) {
        postRepository.update(id, post);
    }
    // 후기 삭제
    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }
    // 후기 전체 보기 페이지에서 띄울 이미지...
//    public List<PostImage> orderOneImage() {
//        return postImageRepository.findByImageOrder(1);
//    }
}
