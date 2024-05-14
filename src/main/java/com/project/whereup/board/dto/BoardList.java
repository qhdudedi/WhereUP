package com.project.whereup.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//전체 목록에서 띄울거
//아이디, 이름, 뭐......
//추가하면 repository 도 수정할 것
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardList {
    private Long boardId;
    private String subject;

}
