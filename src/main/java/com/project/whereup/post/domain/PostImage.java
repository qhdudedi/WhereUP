package com.project.whereup.post.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage { // 후기 이미지 처리에서 필요할 거 같았는데 안쓰임..
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;
    @Column(name = "postId", nullable = false)
    private Long postId;
    @Column(name = "imageName", nullable = false, unique = true)
    private String imageName;
    @Column(name = "imageOrder", nullable = false)
    private int imageOrder;
}
