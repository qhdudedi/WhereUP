package com.project.whereup.board.service;

import com.project.whereup.board.domain.Board;
import com.project.whereup.board.domain.BoardImage;
import com.project.whereup.board.dto.BoardSummary;
import com.project.whereup.board.repository.BoardImageRepository;
import com.project.whereup.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;

    public Board getBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("wrong boardId"));
    }

    public List<BoardSummary> summeryListBoard() {
        return boardRepository.findSummery();
    }

    public List<BoardImage> listImage(Long boardId) {
        return boardImageRepository.findByBoardId(boardId);
    }

    public List<BoardImage> orderOneImage() {
        return boardImageRepository.findByImageOrder(1);
    }
}
