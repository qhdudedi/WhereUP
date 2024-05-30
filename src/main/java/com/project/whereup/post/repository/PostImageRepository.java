package com.project.whereup.post.repository;

import com.project.whereup.post.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    // 후기 전체 보기 페이지에서 띄울 이미지 찾기...
//    List<PostImage> findByImageOrder(int imageOrder);
}
