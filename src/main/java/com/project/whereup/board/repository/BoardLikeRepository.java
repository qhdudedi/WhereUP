package com.project.whereup.board.repository;

import com.project.whereup.board.domain.Board;
import com.project.whereup.board.domain.BoardLike;
import com.project.whereup.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    List<BoardLike> findByUserId(Long userId);

    BoardLike findOneByUserAndBoard(User user, Board board);

}
