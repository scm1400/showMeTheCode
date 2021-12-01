package com.sparta.showmethecode.controller;

import com.sparta.showmethecode.config.security.UserDetailsImpl;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.dto.request.AddReviewDto;
import com.sparta.showmethecode.dto.request.UpdateAnswerDto;
import com.sparta.showmethecode.dto.response.PageResponseDto;
import com.sparta.showmethecode.dto.response.ReviewAnswerResponseDto;
import com.sparta.showmethecode.dto.response.ReviewRequestListResponseDto;
import com.sparta.showmethecode.dto.response.ReviewerInfoDto;
import com.sparta.showmethecode.service.ReviewerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.Page;
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
    @GetMapping("/reviewer/rank")
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
    @GetMapping("/reviewer/top")
    public ResponseEntity getReviewerTop5Ranking() {
        List<ReviewerInfoDto> reviewers = reviewerService.getReviewerTop5Ranking();

        return ResponseEntity.ok(reviewers);
    }

    /**
     * 리뷰요청 거절 API
     */
    @GetMapping("/reviewer/request/reject")
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
    @PostMapping("/reviewer/request")
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
     * 내가 답변한 리뷰목록 조회 API
     */
    @GetMapping("/reviewer/answers")
    public ResponseEntity getMyAnswerList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean isAsc,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        User user = userDetails.getUser();
        PageResponseDto<ReviewAnswerResponseDto> result = reviewerService.getMyAnswerList(user, page, size, isAsc, sortBy);

        return ResponseEntity.ok(result);
    }

    /**
     * 답변한 리뷰 수정 API
     */
    @PutMapping("/reviewer/answer/{answerId}")
    public ResponseEntity updateMyAnswer(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long answerId,
            @RequestBody UpdateAnswerDto updateAnswerDto
    ) {
        User reviewer = userDetails.getUser();

        reviewerService.updateAnswer(reviewer, answerId, updateAnswerDto);

        return ResponseEntity.ok("ok");
    }


    /**
     * 나에게 요청된 리뷰요청목록 조회 API
     */
    @GetMapping("/reviewer/requests")
    public ResponseEntity<ReviewRequestListResponseDto> getRequestedReviewList(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        return null;
    }
}
