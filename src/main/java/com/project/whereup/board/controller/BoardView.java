package com.project.whereup.board.controller;

import com.project.whereup.board.domain.Board;
import com.project.whereup.board.dto.BoardList;
import com.project.whereup.board.dto.BoardResponse;
import com.project.whereup.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/board")
public class BoardView {
    private final BoardService boardService;
    //전체보기 페이지 //수정 or 삭제 예정
    @GetMapping(value = "/boards")
    public String boards(Model model) {
        List<BoardResponse> boards = boardService.listBoard().stream().map(BoardResponse::new).toList();
        model.addAttribute("boards", boards);
        return "/boards";
    }
    //전체보기 페이지에서 필요한 것만 추려서 리스트로 뽑는용도
    @GetMapping(value = "/boardList")
    public String board(Model model) {
        List<BoardList> list = boardService.summeryListBoard();
        model.addAttribute("boardList", list);
        return "/boardList";
    }
    //한개보기 페이지
    @GetMapping(value = "/{boardId}")
    public String board(@PathVariable Long boardId, Model model) {
        Board board = boardService.getBoard(boardId);
        model.addAttribute("board", board);
        return "/board";
    }
    //작성(수정) 페이지
    @GetMapping(value = "/postBoard")
    public String postBoard(@RequestParam(required = false) Long boardId, Model model) {
        if (boardId == null) {
            model.addAttribute("board", new BoardResponse());
        } else {
            Board board = boardService.getBoard(boardId);
            model.addAttribute("board", new BoardResponse(board));
        }
        return "/postBoard";
    }
}
