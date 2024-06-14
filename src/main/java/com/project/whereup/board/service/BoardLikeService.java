package com.project.whereup.board.service;

import com.project.whereup.board.domain.Board;
import com.project.whereup.board.domain.BoardLike;
import com.project.whereup.board.dto.BoardRequestDto;
import com.project.whereup.board.repository.BoardLikeRepository;
import com.project.whereup.board.repository.BoardRepository;
import com.project.whereup.user.entity.User;
import com.project.whereup.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardLikeService {

    private final BoardLikeRepository boardLikeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    // 팝업 정보에 좋아요 등록하기
    @Transactional
    public Board createBoardLike(String email, Long boardId) {

        User user = userRepository.findByEmail(email);
        Optional<Board> oneBoard = boardRepository.findById(boardId);

        if (user == null || oneBoard.isEmpty()) {
            throw new IllegalArgumentException("User or Board not found");
        }
        Board board = oneBoard.get();
        BoardLike boardLike = boardLikeRepository.findOneByUserAndBoard(user, board);

        if (boardLike == null) {
            createBoardFavorite(user, board);
        } else {
            deleteBoardFavorite(user, board);
        }
        return board;
    }
    // board 좋아요 등록
    @Transactional
    public void createBoardFavorite(User user, Board board){
        BoardLike boardLike = BoardLike.builder()
                .user(user)
                .board(board)
                .build();
        boardLikeRepository.save(boardLike);
    }
    // board 좋아요 삭제
    @Transactional
    public void deleteBoardFavorite(User user,  Board board){
        BoardLike boardLike = boardLikeRepository.findOneByUserAndBoard(user,board);
        if(boardLike != null){
            boardLikeRepository.delete(boardLike);
        }
    }
    // 현재 로그인한 유저가 좋아요한 팝업인지 구분
    public boolean isBoardLikedByUser(String email, Long boardId) {
        User user = userRepository.findByEmail(email);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board Id"));

        BoardLike boardLike = boardLikeRepository.findOneByUserAndBoard(user, board);
        return boardLike != null;
    }

    // 유저가 관심 팝업으로 등록한 팝업 목록 가져오기 - mypage
    @Transactional
    public List<BoardRequestDto> findBoardByUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found with username:" + username);
        }
        // 유저의 아이디로 관심팝업  찾기
        List<BoardLike> boardLike = boardLikeRepository.findByUserId(user.getId());

        return boardLike.stream()
                .map(like -> new BoardRequestDto(like.getBoard()))
                .collect(Collectors.toList());
    }

}
