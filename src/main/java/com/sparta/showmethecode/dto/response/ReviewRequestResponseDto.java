package com.sparta.showmethecode.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.showmethecode.domain.ReviewRequestStatus;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewRequestResponseDto {

    private String username;
    private String title;
    private String comment;

    private String status;

    private LocalDateTime createdAt;

    @QueryProjection
    public ReviewRequestResponseDto(String username, String title, String comment, String status, LocalDateTime createdAt) {
        this.username = username;
        this.title = title;
        this.comment = comment;
        this.status = status;
        this.createdAt = createdAt;
    }
}
