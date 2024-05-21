package com.project.whereup.board.repository;

import com.project.whereup.board.domain.Board;
import com.project.whereup.board.dto.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<BoardList> findAllSummery();
}