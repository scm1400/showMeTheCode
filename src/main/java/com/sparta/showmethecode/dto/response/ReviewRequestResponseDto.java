package com.sparta.showmethecode.dto.response;

import com.sparta.showmethecode.domain.ReviewRequestStatus;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ReviewRequestResponseDto {

    private String username;
    private String title;
    private String comment;

    private String status;

    private LocalDateTime createdAt;
}
