package com.sparta.showmethecode.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewRequestResponseDto {

    private Long reviewRequestId;
    private String username;
    private String title;
    private String comment;

    private String languageName;
    private String status;

    private LocalDateTime createdAt;

    @QueryProjection
    public ReviewRequestResponseDto(Long reviewRequestId, String username, String title, String comment, String languageName, String status, LocalDateTime createdAt) {
        this.reviewRequestId = reviewRequestId;
        this.username = username;
        this.title = title;
        this.comment = comment;
        this.languageName = languageName;
        this.status = status;
        this.createdAt = createdAt;
    }
}
