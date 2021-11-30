package com.sparta.showmethecode.controller;

import com.sparta.showmethecode.config.security.UserDetailsImpl;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.dto.request.AddReviewDto;
import com.sparta.showmethecode.dto.response.PageResponseDto;
import com.sparta.showmethecode.dto.response.ReviewRequestListResponseDto;
import com.sparta.showmethecode.dto.response.ReviewerInfoDto;
import com.sparta.showmethecode.service.ReviewerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public class ReviewerController {

    private final ReviewerService reviewerService;

    /**
     * 리뷰어 랭킹 조회 API (전체랭킹 조회)
     */
    @GetMapping("/user/reviewer/rank")
    public ResponseEntity getReviewerRanking(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean isAsc
    ) {
        --page;

        PageResponseDto<ReviewerInfoDto> reviewerRanking = reviewerService.getReviewerRanking(page, size, isAsc);

        return ResponseEntity.ok(reviewerRanking);
    }

    /**
     * 리뷰어 랭킹 조회 API (상위 5위)
     */
    @GetMapping("/user/reviewer/top")
    public ResponseEntity getReviewerTop5Ranking() {
        List<ReviewerInfoDto> reviewers = reviewerService.getReviewerTop5Ranking();

        return ResponseEntity.ok(reviewers);
    }

    /**
     * 리뷰요청 거절 API
     */
    @GetMapping("/user/reviewer/request/reject")
    public ResponseEntity rejectRequestedReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long questionId
    ) {
        User user = userDetails.getUser();
        reviewerService.rejectRequestedReview(user, questionId);

        return ResponseEntity.ok("ok");
    }

    /**
     * 리뷰요청에 대한 리뷰등록 API
     */
    @PostMapping("/user/reviewer/request")
    public ResponseEntity addReviewAndComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long questionId,
            @RequestBody AddReviewDto addReviewDto
    ) {
        User reviewer = userDetails.getUser();
        reviewerService.addReviewAndComment(reviewer, questionId, addReviewDto);
        return ResponseEntity.ok("ok");
    }

    /**
     * 나에게 요청된 리뷰요청목록 조회 API
     */
    @GetMapping("/user/reviewer/requests")
    public ResponseEntity<ReviewRequestListResponseDto> getRequestedReviewList(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        return null;
    }
}
