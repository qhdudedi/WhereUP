package com.project.whereup.board.service;

import com.project.whereup.board.domain.Board;
import com.project.whereup.board.dto.BoardList;
import com.project.whereup.board.repository.BoardRepository;
import com.project.whereup.board.dto.BoardRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public List<Board> listBoard() {
        return boardRepository.findAll();
    }

    public Board getBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("wrong boardId"));
    }

    public Board postBoard(BoardRequest request) {
        return boardRepository.save(request.toEntity());
    }

    @Transactional
    public Board putBoard(Long boardId, BoardRequest request) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("wrong boardId"));
        board.update(request);
        return board;
    }

    public void deleteBoard(long postId) {
        boardRepository.deleteById(postId);
    }

    public List<BoardList> summeryListBoard() {
        return boardRepository.findSummery();
    }
}
