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
    private String content;
    private String language_name;
    private String status;

    private LocalDateTime createdAt;

    private ReviewAnswerResponseDto reviewAnswerResponseDto;

    private List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();


    @QueryProjection
    public ReviewRequestDetailResponseDto(
            Long reviewRequestId,
            String username, String title, String content, String status, LocalDateTime createdAt,
            List<CommentResponseDto> commentResponseDtoList,
            ReviewAnswerResponseDto reviewAnswerResponseDto
    ) {
        this.reviewRequestId = reviewRequestId;
        this.username = username;
        this.title = title;
        this.content = content;
        this.status = status;
        this.createdAt = createdAt;
        this.commentResponseDtoList = commentResponseDtoList;
        this.reviewAnswerResponseDto = reviewAnswerResponseDto;
    }

    public ReviewRequestDetailResponseDto(
            Long reviewRequestId,
            String username, String title, String content, String status, LocalDateTime createdAt,
            List<CommentResponseDto> commentResponseDtoList
    ) {
        this.reviewRequestId = reviewRequestId;
        this.username = username;
        this.title = title;
        this.content = content;
        this.language_name = language_name;
        this.status = status;
        this.createdAt = createdAt;
        this.commentResponseDtoList = commentResponseDtoList;
    }
}
