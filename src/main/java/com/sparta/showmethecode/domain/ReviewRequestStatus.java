package com.sparta.showmethecode.domain;

import lombok.Getter;

@Getter
public enum ReviewRequestStatus {

    REQUESTED("미해결"),
    CHECKED("확인됨"),
    REJECTED("거절됨"),
    COMPLETED("완료됨");

    private final String description;

    ReviewRequestStatus(String description) {
        this.description = description;
    }
}
