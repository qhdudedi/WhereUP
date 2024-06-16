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
    // 전부 찾는거
    @Query(value = "select new com.project.whereup.board.dto.BoardSummary(id, subject, start_date, end_date, brand) from Board")
    List<BoardSummary> findSummery();
    // 전부찾는거
    @Query("SELECT new com.project.whereup.board.dto.BoardSummaryLoc(id, subject, start_date, end_date, brand, location)" +
            "FROM Board")
    List<BoardSummaryLoc> findSummeryLoc();
    // 팝업의 시작날짜가 입력한 날짜들 범위에 있는거
    @Query("SELECT new com.project.whereup.board.dto.BoardSummary(b.id, b.subject, b.start_date, b.end_date, b.brand) " +
            "FROM Board b " +
            "WHERE b.start_date BETWEEN :startDate AND :endDate")
    List<BoardSummary> findBoardSummariesWithinDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    // 키워드 검색 전부 찾는거
    @Query("SELECT new com.project.whereup.board.dto.BoardSummary(b.id, b.subject, b.start_date, b.end_date, b.brand) " +
            "FROM Board b " +
            "WHERE b.subject LIKE %:keyword%")
    List<BoardSummary> findBoardSummariesByKeyword(@Param("keyword") String keyword);
    // 키워드 검색 끝날짜가 입력한 날짜보다 전인거 제외
    @Query("SELECT new com.project.whereup.board.dto.BoardSummary(b.id, b.subject, b.start_date, b.end_date, b.brand) " +
            "FROM Board b " +
            "WHERE b.subject LIKE %:keyword% " +
            "AND b.end_date >= :today")
    List<BoardSummary> findBoardSummariesByKeywordAftetDate(@Param("keyword") String keyword, @Param("today") LocalDate today);
}