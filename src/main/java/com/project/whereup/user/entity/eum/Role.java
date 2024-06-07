package com.project.whereup.user.entity.eum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String key;

    //권한
    public String getAuthority() {
        return key;
    }
}