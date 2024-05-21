package com.project.whereup.board.controller;

import com.project.whereup.board.domain.Board;
import com.project.whereup.board.service.BoardService;
import com.project.whereup.board.dto.BoardRequest;
import com.project.whereup.board.dto.BoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/boardApi")
public class BoardApi {
    private final BoardService boardService;
    //list
    @GetMapping(value = "/boards")
    public ResponseEntity<List<BoardResponse>> getAllBoards() {
        List<BoardResponse> list = boardService.listBoard().stream().map(BoardResponse::new).toList();
        return ResponseEntity.ok(list);
    }
    //detail
    @GetMapping(value = "/{boardId}")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable Long boardId) {
        Board board = boardService.getBoard(boardId);
        return ResponseEntity.ok().body(new BoardResponse(board));
    }
    //add
    @PostMapping(value = "/postBoard")
    public ResponseEntity<Board> postBoard(@RequestBody BoardRequest request) {
        Board postedBoard = boardService.postBoard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(postedBoard);
    }
    //update
    @PutMapping(value = "/{boardId}")
    public ResponseEntity<Board> updateBoard(@PathVariable Long boardId, @RequestBody BoardRequest request) {
        Board puttedboard = boardService.putBoard(boardId, request);
        return ResponseEntity.ok().body(puttedboard);

    }
    //delete
    @DeleteMapping("/{boardId}")
    ResponseEntity<Void> deleteBoard(@PathVariable long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok().build();
    }
}
