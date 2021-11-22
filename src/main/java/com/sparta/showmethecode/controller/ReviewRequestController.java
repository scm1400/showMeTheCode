package com.sparta.showmethecode.controller;

import com.sparta.showmethecode.dto.response.ReviewRequestListResponseDto;
import com.sparta.showmethecode.service.ReviewRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReviewRequestController {

    private final ReviewRequestService reviewRequestService;

    /**
     * 리뷰요청 목록
     */
    @GetMapping("/requests")
    public ResponseEntity<ReviewRequestListResponseDto> getReviewRequestList(
            @RequestParam int page, @RequestParam int size, @RequestParam String sortBy, @RequestParam boolean isAsc
    ) {
        ReviewRequestListResponseDto reviewRequestList = reviewRequestService.getReviewRequestList(page, size, sortBy, isAsc);

        return ResponseEntity.ok(reviewRequestList);
    }
}
