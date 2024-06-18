package com.project.whereup.post.repository;

import com.project.whereup.post.domain.Post;
import com.project.whereup.post.dto.PostSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "SELECT new com.project.whereup.post.dto.PostSummary(p.id, p.author, p.title, p.created_date, p.thumbnail) " +
            "FROM Post p " +
            "ORDER BY p.created_date DESC, p.title ASC")
    List<PostSummary> findSummary();

    @Query("SELECT new com.project.whereup.post.dto.PostSummary(p.id, p.author, p.title, p.created_date, p.thumbnail) " +
            "FROM Post p " +
            "WHERE p.author = :author " +
            "ORDER BY p.created_date DESC, p.title ASC")
    List<PostSummary> findSummaryByAuthor(@Param("author") String author);

    @Query("SELECT new com.project.whereup.post.dto.PostSummary(p.id, p.author, p.title, p.created_date, p.thumbnail) " +
            "FROM Post p " +
            "WHERE p.title LIKE %:keyword% " +
            "ORDER BY p.created_date DESC, p.title ASC")
    List<PostSummary> findPostSummariesByKeyword(@Param("keyword") String keyword);
}
