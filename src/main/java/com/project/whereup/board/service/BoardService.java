package com.project.whereup.board.service;

import com.project.whereup.board.domain.Board;
import com.project.whereup.board.domain.BoardImage;
import com.project.whereup.board.domain.Category;
import com.project.whereup.board.dto.BoardDesc;
import com.project.whereup.board.dto.BoardSummary;
import com.project.whereup.board.repository.BoardImageRepository;
import com.project.whereup.board.repository.BoardRepository;
import com.project.whereup.s3.service.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final S3Service s3Service;

    private LocalDate today = LocalDate.now();

    public Board getBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("wrong boardId"));
    }

    public List<BoardImage> listImage(Long boardId) {
        return boardImageRepository.findByBoardId(boardId);
    }

    public List<BoardSummary> summaryListPage(String keyword, int page) {
        int howManyOnePage = 6;
        List<BoardSummary> allSummaries = keyword.isEmpty() ?
                boardRepository.findSortedSummaries() :
                search(keyword);
        int totalSummeryCount = allSummaries.size();
        int end = Math.min(page * howManyOnePage, totalSummeryCount);
        List<BoardSummary> summaries = allSummaries.subList((page - 1) * howManyOnePage, end);

        summaries = imgFromNameToUrl(summaries);
        return summaries;
    }

    public int pageCount(String keyword) {
        int howManyOnePage = 6;
        List<BoardSummary> boards = keyword.isEmpty() ? boardRepository.findSummery() : search(keyword);
        return (boards.size() + howManyOnePage - 1) / howManyOnePage;
    }

    // end_date가 오늘보다 전이면 제외
    public List<BoardSummary> search(String keyword) {
        return boardRepository.findBoardSummariesByKeywordAftetDate(keyword, today);
    }

    // 지역포함 된거, 나중에 BoardSummary로 바꾸는중
    public List<BoardSummary> locSearch(String location) {
        List<BoardSummary> summaries = boardRepository.findSummariesByLocation(location, today);
        summaries = imgFromNameToUrl(summaries);
        return summaries;
    }

    /////일주일 전부터 일주일 후까지
    public List<BoardSummary> dateRangeBoard() {
        LocalDate startDate = today.minusWeeks(1);
        LocalDate endDate = today.plusWeeks(1);
        List<BoardSummary> summaries = boardRepository.findBoardSummariesWithinDateRange(startDate, endDate);
        summaries = imgFromNameToUrl(summaries);
        return summaries;
    }

    // 이미지 이름인거 url로 바꿔주기
    public List<BoardSummary> imgFromNameToUrl(List<BoardSummary> summaries) {
        for (BoardSummary summary : summaries) {
            if (summary.getImageUrl() != null) {
                summary.setImageUrl(s3Service.getImageUrl("board/" + summary.getImageUrl()));
            } else {
                summary.setImageUrl(s3Service.getImageUrl("admin/whereup.png"));
            }
        }
        return summaries;
    }
    // Board 카테고리 검색
    @Transactional
    public List<Board> getBoardsByCategory(Category category) {
        List<Board> boards = boardRepository.findByCategory(category);
        // 지연 로딩된 컬렉션 초기화
        boards.forEach(board -> Hibernate.initialize(board.getBoardLikes()));
        return boards;
    }

    // Board 지역구 검색
    @Transactional
    public List<BoardSummary> getBoardByLocation(String locKeyword) {
        List<Board> boardData = boardRepository.findBoardByLocationContains(locKeyword);

        // 2. Board 객체를 BoardSummary 객체로 변환
        List<BoardSummary> boardSummaryList = boardData.stream()
                .map(board -> new BoardSummary(
                        board.getId(),
                        board.getSubject(),
                        board.getStart_date(),
                        board.getEnd_date(),
                        board.getBrand(),
                        board.getLocation(),
                        board.getLink()
                ))
                .collect(Collectors.toList());
        for (BoardSummary boardSummary : boardSummaryList) {
            if(boardImageRepository.findByBoardIdAndImageOrder(boardSummary.getId(), 1) != null) {
                String imgName = boardImageRepository.findByBoardIdAndImageOrder(boardSummary.getId(), 1).getImageName();
                boardSummary.setImageUrl(imgName);
            } else {
                boardSummary.setImageUrl(null);
            }
        }
        // imageUrl을 실제 URL로 변환
        return imgFromNameToUrl(boardSummaryList);
    }
    public List<BoardDesc> mainTopSlide(int howMany) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(20);
        LocalDate endDate = today.plusDays(20);
        Pageable pageable = PageRequest.of(0, howMany);
        Page<BoardDesc> pageSummaries = boardRepository.findDateRangeBoard(startDate, endDate, pageable);
        List<BoardDesc> summaries = pageSummaries.getContent();

        if (!summaries.isEmpty()) {
            System.out.println(summaries.get(0).getSubject());
        }

        for (BoardDesc summary : summaries) {
            if (summary.getImageUrl() != null) {
                summary.setImageUrl(s3Service.getImageUrl("board/" + summary.getImageUrl()));
            } else {
                summary.setImageUrl(s3Service.getImageUrl("admin/whereup.png"));
            }
        }
        return summaries;
    }

    public void save(Board board) {
        boardRepository.save(board);
    }

    public List<String> brandList() {
        return boardRepository.findDistinctBrands();
    }
}
