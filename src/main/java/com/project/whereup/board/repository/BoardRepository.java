package com.project.whereup.board.repository;

import com.project.whereup.board.domain.Board;
import com.project.whereup.board.dto.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
//    Board findById();

    @Query(value = "select new com.project.whereup.board.dto.BoardList(id, subject, start_date, end_date, brand, image) from Board")
    List<BoardList> findSummery();
}