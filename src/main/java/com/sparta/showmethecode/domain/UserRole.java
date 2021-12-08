package com.sparta.showmethecode.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    USER("ROLE_USER","일반 사용자"),
    REVIEWER("ROLE_REVIEWER","리뷰어");

    private final String key;
    private final String title;

}
