package com.project.whereup.post.repository;

import com.project.whereup.post.domain.Post;
import com.project.whereup.post.dto.PostSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "select new com.project.whereup.post.dto.PostSummary(id, author, title, created_date, thumbnail) from Post")
    List<PostSummary> findSummary();

}
