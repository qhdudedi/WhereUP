package com.project.whereup.post.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;
    @Column(name = "postId", nullable = false)
    private Long postId;
    @Column(name = "user", nullable = false)
    private String user;
    @Column(name = "text", nullable = false)
    private String text;
    @Column(name = "created_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate created_date;
    @Column(name = "updated_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate updated_date;

    @Builder
    public Comment(Long postId, String user, String text, LocalDate created_date, LocalDate updated_date) {
        this.postId = postId;
        this.user = user;
        this.text = text;
        this.created_date = created_date;
        this.updated_date = updated_date;
    }
}
