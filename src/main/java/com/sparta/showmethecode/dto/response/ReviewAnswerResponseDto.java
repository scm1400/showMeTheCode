package com.sparta.showmethecode.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Builder
@Data
public class ReviewAnswerResponseDto {

    private Long reviewAnswerId;
    private Long reviewRequestId;

    private String answerTitle;
    private String answerCode;
    private String answerComment;

    private double point;

    private LocalDateTime createdAt;

    @QueryProjection
    public ReviewAnswerResponseDto(Long reviewAnswerId, Long reviewRequestId, String answerTitle, String answerCode, String answerComment, double point, LocalDateTime createdAt) {
        this.reviewAnswerId = reviewAnswerId;
        this.reviewRequestId = reviewRequestId;
        this.answerTitle = answerTitle;
        this.answerCode = answerCode;
        this.answerComment = answerComment;
        this.point = point;
        this.createdAt = createdAt;
    }
}
