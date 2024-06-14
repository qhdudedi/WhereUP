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
        List<BoardSummary> allSummaries = new ArrayList<>();
        if (keyword.equals("")) {
            allSummaries = sortSummaries(boardRepository.findSummery());
        } else {
            allSummaries = sortSummaries(search(keyword));
        }
        int totalSummeryCount = allSummaries.size();
        int end = Math.min(page * howManyOnePage, totalSummeryCount);
        List<BoardSummary> summeryList = allSummaries.subList((page - 1) * howManyOnePage, end);

        Map<BoardSummary, String> map = new LinkedHashMap<>();
        for (BoardSummary summary : summeryList) {
            BoardImage boardImage = boardImageRepository.findByBoardIdAndImageOrder(summary.getId(), 1);
            String imageName = (boardImage != null) ? boardImage.getImageName() : "whereup.png";
            map.put(summary, imageName);
        }

        return map;
    }

    public int pageCount(String keyword) {
        int howManyOnePage = 6;
        List<BoardSummary> boards = new ArrayList<>();
        if (keyword.equals("")) {
            boards = boardRepository.findSummery();
        } else {
            boards = search(keyword);
        }
        return boards.size() % howManyOnePage == 0 ? boards.size() / howManyOnePage : boards.size() / howManyOnePage + 1;
    }

    public List<BoardSummary> search(String keyword) {
        List<BoardSummary> boards = sortSummaries(boardRepository.findSummery());
        List<BoardSummary> summaries = new ArrayList<>();
        for (BoardSummary board : boards) {
            if (board.getSubject().toUpperCase().contains(keyword.toUpperCase())) {
                summaries.add(board);
            }
        }
        return summaries;
    }

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
        List<BoardSummary> summaries = sortSummaries(boardRepository.findBoardSummariesWithinDateRange(startDate, endDate));
        Map<BoardSummary, String> map = new LinkedHashMap<>();
        for (BoardSummary summary : summaries) {
            BoardImage boardImage = boardImageRepository.findByBoardIdAndImageOrder(summary.getId(), 1);
            String imageName = (boardImage != null) ? boardImage.getImageName() : "whereup.png";
            map.put(summary, imageName);
        }
        return map;
    }
    public List<BoardSummary> sortSummaries(List<BoardSummary> summaries) {
        summaries.sort(Comparator.comparing(BoardSummary::getStart_date)
                .thenComparing(BoardSummary::getEnd_date));
        return summaries;
    }

}