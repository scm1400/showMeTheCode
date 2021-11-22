package com.sparta.showmethecode.controller;

import com.sparta.showmethecode.dto.request.ReviewRequestDto;
import com.sparta.showmethecode.dto.response.ReviewRequestListResponseDto;
import com.sparta.showmethecode.service.ReviewRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReviewRequestController {

    private final ReviewRequestService reviewRequestService;

    /**
     * 코드리뷰 요청목록 API
     */
    @GetMapping("/requests")
    public ResponseEntity<ReviewRequestListResponseDto> getReviewRequestList(
            @RequestParam int page, @RequestParam int size, @RequestParam String sortBy, @RequestParam boolean isAsc
    ) {
        ReviewRequestListResponseDto reviewRequestList = reviewRequestService.getReviewRequestList(page, size, sortBy, isAsc);

        return ResponseEntity.ok(reviewRequestList);
    }

    /**
     * 코드리뷰 요청 API
     */
    @PostMapping("/request")
    public ResponseEntity addReviewRequest(@RequestBody ReviewRequestDto requestDto) {
        reviewRequestService.addReviewRequest(requestDto);

        return ResponseEntity.ok("ok");
    }
}
