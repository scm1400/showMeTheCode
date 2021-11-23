package com.sparta.showmethecode.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewRequestDetailResponseDto {

    private Long reviewRequestId;
    private String username;
    private String title;
    private String code;
    private String comment;

    private String status;

    private LocalDateTime createdAt;

    private List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

    @QueryProjection
    public ReviewRequestDetailResponseDto(Long reviewRequestId, String username, String title, String code, String comment, String status, LocalDateTime createdAt, List<CommentResponseDto> commentResponseDtoList) {
        this.reviewRequestId = reviewRequestId;
        this.username = username;
        this.title = title;
        this.code = code;
        this.comment = comment;
        this.status = status;
        this.createdAt = createdAt;
        this.commentResponseDtoList = commentResponseDtoList;
    }
}
