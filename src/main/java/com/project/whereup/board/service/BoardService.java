package com.project.whereup.board.service;

import com.project.whereup.board.domain.Board;
import com.project.whereup.board.domain.BoardImage;
import com.project.whereup.board.dto.BoardSummary;
import com.project.whereup.board.repository.BoardImageRepository;
import com.project.whereup.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
            allSummaries = boardRepository.findSummery();
        } else {
            allSummaries = search(keyword);
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
        List<BoardSummary> boards = boardRepository.findSummery();
        List<BoardSummary> summaries = new ArrayList<>();
        for (BoardSummary board : boards) {
            if (board.getSubject().toUpperCase().contains(keyword.toUpperCase())) {
                summaries.add(board);
            }
        }
        return summaries;
    }
}