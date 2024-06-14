package com.project.whereup.board.repository;

import com.project.whereup.board.domain.Board;
import com.project.whereup.board.dto.BoardSummary;
import com.project.whereup.board.dto.BoardSummaryLoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query(value = "select new com.project.whereup.board.dto.BoardSummary(id, subject, start_date, end_date, brand) from Board")
    List<BoardSummary> findSummery();

    @Query("SELECT new com.project.whereup.board.dto.BoardSummaryLoc(id, subject, start_date, end_date, brand, location)" +
            "FROM Board")
    List<BoardSummaryLoc> findSummeryLoc();
}