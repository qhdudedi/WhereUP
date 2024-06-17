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
    @Query("SELECT new com.project.whereup.board.dto.BoardSummary(b.id, b.subject, b.start_date, b.end_date, b.brand, bi.imageName) " +
            "FROM Board b " +
            "LEFT JOIN BoardImage bi ON b.id = bi.boardId AND bi.imageOrder = 1")
    List<BoardSummary> findSummery();
    // 전부찾는거
    @Query("SELECT new com.project.whereup.board.dto.BoardSummaryLoc(b.id, b.subject, b.start_date, b.end_date, b.brand, b.location, bi.imageName) " +
            "FROM Board b " +
            "LEFT JOIN BoardImage bi ON b.id = bi.boardId AND bi.imageOrder = 1")
    List<BoardSummaryLoc> findSummeryLoc();
    // 팝업의 시작날짜가 입력한 날짜들 범위에 있는거, 날짜 정렬
    @Query("SELECT new com.project.whereup.board.dto.BoardSummary(b.id, b.subject, b.start_date, b.end_date, b.brand, bi.imageName) " +
            "FROM Board b " +
            "LEFT JOIN BoardImage bi ON b.id = bi.boardId AND bi.imageOrder = 1 " +
            "WHERE b.start_date BETWEEN :startDate AND :endDate " +
            "ORDER BY b.start_date ASC, b.end_date ASC")
    List<BoardSummary> findBoardSummariesWithinDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    // 키워드 검색 전부 찾는거, 날짜 정렬
    @Query("SELECT new com.project.whereup.board.dto.BoardSummary(b.id, b.subject, b.start_date, b.end_date, b.brand, bi.imageName) " +
            "FROM Board b " +
            "LEFT JOIN BoardImage bi ON b.id = bi.boardId AND bi.imageOrder = 1 " +
            "WHERE b.subject LIKE %:keyword% " +
            "ORDER BY b.start_date ASC, b.end_date ASC")
    List<BoardSummary> findBoardSummariesByKeyword(@Param("keyword") String keyword);
    // 키워드 검색 끝날짜가 입력한 날짜보다 전인거 제외, 날짜 정렬
    @Query("SELECT new com.project.whereup.board.dto.BoardSummary(b.id, b.subject, b.start_date, b.end_date, b.brand, bi.imageName) " +
            "FROM Board b " +
            "LEFT JOIN BoardImage bi ON b.id = bi.boardId AND bi.imageOrder = 1 " +
            "WHERE b.subject LIKE %:keyword% " +
            "AND b.end_date >= :today " +
            "ORDER BY b.start_date ASC, b.end_date ASC")
    List<BoardSummary> findBoardSummariesByKeywordAftetDate(@Param("keyword") String keyword, @Param("today") LocalDate today);
    // 전부 찾는거, 날짜로 정렬
    @Query("SELECT new com.project.whereup.board.dto.BoardSummary(b.id, b.subject, b.start_date, b.end_date, b.brand, bi.imageName) " +
            "FROM Board b " +
            "LEFT JOIN BoardImage bi ON b.id = bi.boardId AND bi.imageOrder = 1 " +
            "ORDER BY b.start_date ASC, b.end_date ASC")
    List<BoardSummary> findSortedSummaries();
}