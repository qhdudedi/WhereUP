package com.project.whereup.board.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;
    @Column(name = "boardId", nullable = false)
    private Long boardId;
    @Column(name = "imageName", nullable = false, unique = true)
    private String imageName;
    @Column(name = "imageOrder", nullable = false)
    private int imageOrder;
}
