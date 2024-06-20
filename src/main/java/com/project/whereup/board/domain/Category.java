package com.project.whereup.board.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {

    문화("문화"),
    패션("패션"),
    생활("생활");

    private final String value;
}