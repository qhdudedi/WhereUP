package com.project.whereup.board.repository;

import com.project.whereup.board.domain.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    List<BoardImage> findByBoardId(Long boardId);
    BoardImage findByBoardIdAndImageOrder(Long boardId, int imageOrder);
}
