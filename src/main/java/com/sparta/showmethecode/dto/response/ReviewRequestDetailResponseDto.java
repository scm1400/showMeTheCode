package com.sparta.showmethecode.dto.response;

import java.time.LocalDateTime;

public class ReviewRequestDetailResponseDto {

    private Long reviewRequestId;
    private String username;
    private String title;
    private String code;
    private String comment;

    private String status;

    private LocalDateTime createdAt;

    public ReviewRequestDetailResponseDto(Long reviewRequestId, String username, String title, String code, String comment, String status, LocalDateTime createdAt) {
        this.reviewRequestId = reviewRequestId;
        this.username = username;
        this.title = title;
        this.code = code;
        this.comment = comment;
        this.status = status;
        this.createdAt = createdAt;
    }
}
