package com.project.whereup.board.repository;

import com.project.whereup.board.domain.Board;
import com.project.whereup.board.dto.BoardSummary;
import com.project.whereup.board.dto.BoardSummaryLoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query(value = "select new com.project.whereup.board.dto.BoardSummary(id, subject, start_date, end_date, brand) from Board")
    List<BoardSummary> findSummery();

    @Query("SELECT new com.project.whereup.board.dto.BoardSummaryLoc(id, subject, start_date, end_date, brand, location)" +
            "FROM Board")
    List<BoardSummaryLoc> findSummeryLoc();

    @Query("SELECT new com.project.whereup.board.dto.BoardSummary(b.id, b.subject, b.start_date, b.end_date, b.brand) " +
            "FROM Board b " +
            "WHERE b.start_date BETWEEN :startDate AND :endDate")
    List<BoardSummary> findBoardSummariesWithinDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}