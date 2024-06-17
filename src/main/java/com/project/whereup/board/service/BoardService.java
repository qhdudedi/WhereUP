package com.project.whereup.board.service;

import com.project.whereup.board.domain.Board;
import com.project.whereup.board.domain.BoardImage;
import com.project.whereup.board.dto.BoardSummary;
import com.project.whereup.board.dto.BoardSummaryLoc;
import com.project.whereup.board.repository.BoardImageRepository;
import com.project.whereup.board.repository.BoardRepository;
import com.project.whereup.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final S3Service s3Service;

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
        LocalDate today = LocalDate.now();
        return boardRepository.findBoardSummariesByKeywordAftetDate(keyword, today);
    }
    // 지역포함 된거, 나중에 BoardSummary랑 BoardSummaryLoc이랑 합치던지 해야됨
    public List<BoardSummaryLoc> locSearch(String location) {
        List<BoardSummaryLoc> summaries = boardRepository.findSummeryLoc();
        summaries.sort(Comparator.comparing(BoardSummaryLoc::getStart_date)
                .thenComparing(BoardSummaryLoc::getEnd_date));
        for (BoardSummaryLoc summary : summaries) {
            if (summary.getImgUrl() != null) {
                summary.setImgUrl(s3Service.getImageUrl(summary.getImgUrl()));
            } else {
                summary.setImgUrl(s3Service.getImageUrl("whereup.png"));
            }
        }

        return summaries;
    }
    /////일주일 전부터 일주일 후까지
    public List<BoardSummary> dateRangeBoard() {
        LocalDate startDate = LocalDate.now().minusWeeks(1);
        LocalDate endDate = LocalDate.now().plusWeeks(1);
        List<BoardSummary> summaries = boardRepository.findBoardSummariesWithinDateRange(startDate, endDate);
        summaries = imgFromNameToUrl(summaries);
        return summaries;
    }

    // 이미지 이름인거 url로 바꿔주기
    public List<BoardSummary> imgFromNameToUrl(List<BoardSummary> summaries) {
        for (BoardSummary summary : summaries) {
            if (summary.getImageUrl() != null) {
                summary.setImageUrl(s3Service.getImageUrl(summary.getImageUrl()));
            } else {
                summary.setImageUrl(s3Service.getImageUrl("whereup.png"));
            }
        }
        return summaries;
    }
}