package com.project.whereup.post.service;

import com.project.whereup.post.domain.Post;
import com.project.whereup.post.repository.PostRepository;
import com.project.whereup.post.dto.PostSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
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
    public void update(Post post) {
        postRepository.save(post);
    }
    // 후기 삭제
    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }

    public List<PostSummary> summaryListPage(String keyword, int page, int howMany) {
        List<PostSummary> allSummaries = keyword.isEmpty() ? postRepository.findSummary() : search(keyword);

        int totalSummeryCount = allSummaries.size();
        int end = Math.min(page * howMany, totalSummeryCount);
        return allSummaries.subList((page - 1) * howMany, end);
    }

    public List<PostSummary> search(String keyword) {
        return postRepository.findPostSummariesByKeyword(keyword);
    }
    public int pageCount(String keyword, int howMany) {
        List<PostSummary> posts = keyword.equals("") ? postRepository.findSummary() : search(keyword);

        return (posts.size() + howMany - 1) / howMany;
    }
    public List<PostSummary> allSummariesMyPage(String author) {
        return postRepository.findSummaryByAuthor(author);
    }
}
