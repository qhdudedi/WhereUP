package com.project.whereup.board.service;

import com.project.whereup.board.domain.Board;
import com.project.whereup.board.domain.BoardImage;
import com.project.whereup.board.dto.BoardSummary;
import com.project.whereup.board.dto.BoardSummaryLoc;
import com.project.whereup.board.repository.BoardImageRepository;
import com.project.whereup.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;

    public Board getBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("wrong boardId"));
    }

    public List<BoardImage> listImage(Long boardId) {
        return boardImageRepository.findByBoardId(boardId);
    }

    public Map<BoardSummary, String> summaryListPage(String keyword, int page) {
        int howManyOnePage = 6;
        List<BoardSummary> allSummaries = keyword.isEmpty() ?
                boardRepository.findSortedSummaries() :
                search(keyword);
        int totalSummeryCount = allSummaries.size();
        int end = Math.min(page * howManyOnePage, totalSummeryCount);
        List<BoardSummary> summeryList = allSummaries.subList((page - 1) * howManyOnePage, end);

        Map<BoardSummary, String> map = boardAndImgMap(summeryList);

        return map;
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
    public Map<BoardSummaryLoc, String> locSearch(String location) {
        List<BoardSummaryLoc> summaries = boardRepository.findSummeryLoc();
        summaries.sort(Comparator.comparing(BoardSummaryLoc::getStart_date)
                .thenComparing(BoardSummaryLoc::getEnd_date));

        Map<BoardSummaryLoc, String> map = new LinkedHashMap<>();
        for (BoardSummaryLoc loc : summaries) {
            if(loc.getLocation().toUpperCase().contains(location.toUpperCase())) {
                BoardImage boardImage = boardImageRepository.findByBoardIdAndImageOrder(loc.getId(), 1);
                String imageName = (boardImage != null) ? boardImage.getImageName() : "whereup.png";
                map.put(loc, imageName);
            }
        }
        return map;
    }
    /////일주일 전부터 일주일 후까지
    public Map<BoardSummary, String> dateRangeBoard() {
        LocalDate startDate = LocalDate.now().minusWeeks(1);
        LocalDate endDate = LocalDate.now().plusWeeks(1);
        List<BoardSummary> summaries = boardRepository.findBoardSummariesWithinDateRange(startDate, endDate);

        Map<BoardSummary, String> map = boardAndImgMap(summaries);
        return map;
    }
    // BoardSummary랑 이미지 url 같이 뭐 할거
    public Map<BoardSummary, String> boardAndImgMap(List<BoardSummary> summaries) {
        Map<BoardSummary, String> map = new LinkedHashMap<>();
        for (BoardSummary summary : summaries) {
            BoardImage boardImage = boardImageRepository.findByBoardIdAndImageOrder(summary.getId(), 1);
            String imageName = (boardImage != null) ? boardImage.getImageName() : "whereup.png";
            map.put(summary, imageName);
        }
        return map;
    }
}