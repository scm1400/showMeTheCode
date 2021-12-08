package com.sparta.showmethecode.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewRequestDetailResponseDto {

    private Long reviewRequestId;
    private Long answerUserId;
    private String username;
    private String title;
    private String content;
    private String languageName;
    private String status;

    private LocalDateTime createdAt;

    private ReviewAnswerResponseDto reviewAnswer;

    private List<CommentResponseDto> comments = new ArrayList<>();


    @QueryProjection
    public ReviewRequestDetailResponseDto(
            Long reviewRequestId, Long answerUserId,
            String username, String title, String content, String status, LocalDateTime createdAt,
            List<CommentResponseDto> comments,
            ReviewAnswerResponseDto reviewAnswer
    ) {
        this.reviewRequestId = reviewRequestId;
        this.answerUserId = answerUserId;
        this.username = username;
        this.title = title;
        this.content = content;
        this.status = status;
        this.createdAt = createdAt;
        this.comments = comments;
        this.reviewAnswer = reviewAnswer;
    }

    public ReviewRequestDetailResponseDto(
            Long reviewRequestId, Long answerUserId,
            String username, String title, String content, String status, LocalDateTime createdAt,
            List<CommentResponseDto> comments
    ) {
        this.reviewRequestId = reviewRequestId;
        this.answerUserId = answerUserId;
        this.username = username;
        this.title = title;
        this.content = content;
        this.languageName = languageName;
        this.status = status;
        this.createdAt = createdAt;
        this.comments = comments;
    }
}
