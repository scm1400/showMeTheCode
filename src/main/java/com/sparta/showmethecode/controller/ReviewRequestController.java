package com.sparta.showmethecode.controller;

import com.sparta.showmethecode.dto.request.UpdateReviewDto;
import com.sparta.showmethecode.security.UserDetailsImpl;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.dto.request.AddCommentDto;
import com.sparta.showmethecode.dto.request.ReviewRequestDto;
import com.sparta.showmethecode.dto.request.ReviewRequestUpdateDto;
import com.sparta.showmethecode.dto.response.*;
import com.sparta.showmethecode.service.CommentService;
import com.sparta.showmethecode.service.ReviewRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReviewRequestController {

    private final ReviewRequestService reviewRequestService;
    private final CommentService commentService;

    /**
     * 코드리뷰 요청목록 API
     */
    @GetMapping("/questions")
    public ResponseEntity<ReviewRequestListResponseDto> getReviewRequestList(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
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
    public ResponseEntity<String> addReviewRequest(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ReviewRequestDto requestDto) {
        User user = userDetails.getUser();
        reviewRequestService.addReviewRequest(requestDto, user);

        log.info("addReviewRequest = {}", requestDto);
        
        return ResponseEntity.ok("ok");
    }

    /**
     * 코드리뷰 단건조회 API (코드리뷰 요청 상세정보)
     */
    @GetMapping("/details")
    public ResponseEntity<ReviewRequestDetailResponseDto> getReviewRequest(@RequestParam Long id) {
        System.out.println(id);
        ReviewRequestDetailResponseDto reviewRequest = reviewRequestService.getReviewRequest(id);

        return ResponseEntity.ok(reviewRequest);
    }

    /**
     * 코드리뷰 수정 API
     */
    @PutMapping("/question")
    public ResponseEntity updateReviewRequest(
            @RequestBody ReviewRequestUpdateDto updateDto,
            @RequestParam Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        reviewRequestService.updateReviewRequest(updateDto, id, user);

        return ResponseEntity.ok("ok");
    }

    /**
     * 코드리뷰 삭제 API
     */
    @DeleteMapping("/question")
    public ResponseEntity deleteReviewRequest(
            @RequestParam Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();
        reviewRequestService.deleteReviewRequest(id, user);
        return ResponseEntity.ok("ok");
    }

    /**
     * 코드리뷰 요청 언어별 카운팅 API
     */
    @GetMapping("/question/languages/count")
    public List<ReviewRequestLanguageCount> getCountGroupByLanguageName() {
        return reviewRequestService.getCountGroupByLanguageName();
    }

    /**
     * 코드리뷰 요청 언어이름 검색 API
     */
    @GetMapping("/question/language")
    public ResponseEntity searchRequestByLanguageName(
            @RequestParam String language,
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "true") boolean isAsc
    ) {
        --page;
        PageResponseDto<ReviewRequestResponseDto> result = reviewRequestService.searchRequestByLanguageName(language, page, size, isAsc);

        return ResponseEntity.ok(result);
    }

    /**
     * 댓글추가 API
     */
    @PostMapping("/question/{id}/comment")
    public ResponseEntity addComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(name = "id") Long reviewId,
            @RequestBody AddCommentDto addCommentDto
    ) {
        User user = userDetails.getUser();
        commentService.addComment(user, reviewId, addCommentDto);

        return ResponseEntity.ok().body("댓글작성 완료");
    }

    /**
     * 댓글삭제 API
     */
    @DeleteMapping("/question/{questionId}/comment/{commentId}")
    public ResponseEntity removeComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long questionId, @PathVariable Long commentId
    ) {
        User user = userDetails.getUser();
        long row = commentService.removeComment(user, questionId, commentId);

        return ResponseEntity.ok().body(row);
    }

    /**
     * 댓글수정 API
     */
    @PutMapping("/question/{questionId}/comment/{commentId}")
    public ResponseEntity updateComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long questionId, @PathVariable Long commentId,
            @RequestBody UpdateReviewDto updateReviewDto
    ) {
        User user = userDetails.getUser();
        commentService.updateComment(user, questionId, commentId, updateReviewDto);

        return ResponseEntity.ok("success");
    }
}
