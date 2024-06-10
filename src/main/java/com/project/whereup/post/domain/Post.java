package com.project.whereup.post.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;
    @Column(name = "author", nullable = false)
    private String author;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "created_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate created_date;
    @Column(name = "updated_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate updated_date;
    @Column(name = "thumbnail")
    private String thumbnail;

    @Builder
    public Post(String author, String title, String content, LocalDate created_date, LocalDate updated_date, String thumbnail) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.created_date = created_date;
        this.updated_date = updated_date;
        this.thumbnail = thumbnail;
    }
}