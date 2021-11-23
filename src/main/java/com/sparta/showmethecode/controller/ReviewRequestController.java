package com.sparta.showmethecode.controller;

import com.sparta.showmethecode.dto.request.ReviewRequestDto;
import com.sparta.showmethecode.dto.response.ReviewRequestDetailResponseDto;
import com.sparta.showmethecode.dto.response.ReviewRequestListResponseDto;
import com.sparta.showmethecode.service.ReviewRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReviewRequestController {

    private final ReviewRequestService reviewRequestService;

    /**
     * 코드리뷰 요청목록 API
     */
    @GetMapping("/questions")
    public ResponseEntity<ReviewRequestListResponseDto> getReviewRequestList(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy, @RequestParam(defaultValue = "true") Boolean isAsc,
            @RequestParam(required = false) String query
    ) {

        --page;

        if (!Objects.isNull(query)) {
            return ResponseEntity.ok(reviewRequestService.searchByTitleOrComment(query, page, size, sortBy, isAsc));
        }

        ReviewRequestListResponseDto reviewRequestList = reviewRequestService.getReviewRequestList(page, size, sortBy, isAsc);

        return ResponseEntity.ok(reviewRequestList);
    }

    /**
     * 코드리뷰 요청 API
     */
    @PostMapping("/question")
    public ResponseEntity<String> addReviewRequest(@RequestBody ReviewRequestDto requestDto) {
        reviewRequestService.addReviewRequest(requestDto);

        return ResponseEntity.ok("ok");
    }

    /**
     * 코드리뷰 단건조회 API (코드리뷰 요청 상세정보)
     */
    @GetMapping("/question")
    public ResponseEntity<ReviewRequestDetailResponseDto> getReviewRequest(@RequestParam Long id) {
        ReviewRequestDetailResponseDto reviewRequest = reviewRequestService.getReviewRequest(id);

        return ResponseEntity.ok(reviewRequest);
    }
}
