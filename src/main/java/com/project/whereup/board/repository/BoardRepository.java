package com.project.whereup.board.repository;

import com.project.whereup.board.domain.Board;
import com.project.whereup.board.domain.Category;
import com.project.whereup.board.dto.BoardDesc;
import com.project.whereup.board.dto.BoardSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    //카테고리에 따른 보드 검색
    List<Board> findByCategory(Category category);

    // Location 검색으로 팝업 검색
    List<Board> findBoardByLocationContains(String locKeyword);

    // 전부 찾는거
    @Query("SELECT new com.project.whereup.board.dto.BoardSummary(b.id, b.subject, b.start_date, b.end_date, b.brand, b.location, bi.imageName) " +
            "FROM Board b " +
            "LEFT JOIN BoardImage bi ON b.id = bi.boardId AND bi.imageOrder = 1")
    List<BoardSummary> findSummery();

    // 팝업의 시작날짜가 입력한 날짜들 범위에 있는거, 날짜 정렬
    @Query("SELECT new com.project.whereup.board.dto.BoardSummary(b.id, b.subject, b.start_date, b.end_date, b.brand, b.location, bi.imageName) " +
            "FROM Board b " +
            "LEFT JOIN BoardImage bi ON b.id = bi.boardId AND bi.imageOrder = 1 " +
            "WHERE b.start_date BETWEEN :startDate AND :endDate " +
            "ORDER BY b.start_date ASC, b.end_date ASC")
    List<BoardSummary> findBoardSummariesWithinDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 키워드 검색 전부 찾는거, 날짜 정렬
    @Query("SELECT new com.project.whereup.board.dto.BoardSummary(b.id, b.subject, b.start_date, b.end_date, b.brand, b.location, bi.imageName) " +
            "FROM Board b " +
            "LEFT JOIN BoardImage bi ON b.id = bi.boardId AND bi.imageOrder = 1 " +
            "WHERE b.subject LIKE %:keyword% " +
            "ORDER BY b.start_date ASC, b.end_date ASC")
    List<BoardSummary> findBoardSummariesByKeyword(@Param("keyword") String keyword);

    // 키워드 검색 끝날짜가 입력한 날짜보다 전인거 제외, 날짜 정렬
    @Query("SELECT new com.project.whereup.board.dto.BoardSummary(b.id, b.subject, b.start_date, b.end_date, b.brand, b.location, bi.imageName) " +
            "FROM Board b " +
            "LEFT JOIN BoardImage bi ON b.id = bi.boardId AND bi.imageOrder = 1 " +
            "WHERE b.subject LIKE %:keyword% " +
            "AND b.end_date >= :today " +
            "ORDER BY b.start_date ASC, b.end_date ASC")
    List<BoardSummary> findBoardSummariesByKeywordAftetDate(@Param("keyword") String keyword, @Param("today") LocalDate today);

    // 전부 찾는거, 날짜로 정렬
    @Query("SELECT new com.project.whereup.board.dto.BoardSummary(b.id, b.subject, b.start_date, b.end_date, b.brand, b.location, bi.imageName) " +
            "FROM Board b " +
            "LEFT JOIN BoardImage bi ON b.id = bi.boardId AND bi.imageOrder = 1 " +
            "ORDER BY b.start_date ASC, b.end_date ASC")
    List<BoardSummary> findSortedSummaries();

    // 지역이름으로 찾는거, 날짜로 정렬, 끝날짜가 입력한 날짜보다 전인거 제외
    @Query("SELECT new com.project.whereup.board.dto.BoardSummary(b.id, b.subject, b.start_date, b.end_date, b.brand, b.location, bi.imageName) " +
            "FROM Board b " +
            "LEFT JOIN BoardImage bi ON b.id = bi.boardId AND bi.imageOrder = 1 " +
            "WHERE b.location LIKE %:location% " +
            "AND b.end_date >= :today " +
            "ORDER BY b.start_date ASC, b.end_date ASC")
    List<BoardSummary> findSummariesByLocation(@Param("location") String location, @Param("today") LocalDate today);

    @Query("SELECT new com.project.whereup.board.dto.BoardDesc(b.id, b.subject, b.description, b.start_date, b.end_date, bi.imageName) " +
            "FROM Board b " +
            "LEFT JOIN BoardImage bi ON b.id = bi.boardId AND bi.imageOrder = 1 " +
            "WHERE b.start_date BETWEEN :startDate AND :endDate " +
            "ORDER BY b.start_date ASC, b.end_date ASC")
    Page<BoardDesc> findDateRangeBoard(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
    // brand만 뽑기
    @Query("SELECT DISTINCT b.brand FROM Board b")
    List<String> findDistinctBrands();
}