package com.sparta.showmethecode.controller;

import com.sparta.showmethecode.config.security.UserDetailsImpl;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.dto.request.AddReviewDto;
import com.sparta.showmethecode.dto.request.SigninRequestDto;
import com.sparta.showmethecode.dto.request.SignupRequestDto;
import com.sparta.showmethecode.dto.response.*;
import com.sparta.showmethecode.service.ReviewerService;
import com.sparta.showmethecode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final ReviewerService reviewerService;

    @PostMapping("/user/signup")
    public ResponseEntity<BasicResponseDto> signup(@RequestBody SignupRequestDto requestDto) {
        userService.saveUser(requestDto);
        BasicResponseDto responseDto = BasicResponseDto.builder()
                .result("success").httpStatus(HttpStatus.CREATED).message("회원가입에 성공했습니다.").build();

        return ResponseEntity.ok(responseDto);
    }


    @PostMapping("/user/signin")
    public SigninResponseDto signin(@RequestBody SigninRequestDto requestDto) {

        return userService.signin(requestDto);
    }

    /**
     * 해당 언어의 리뷰어 조회 API
     */
    @GetMapping("/user/language")
    public ResponseEntity<List<ReviewerInfoDto>> findReviewerByLanguage(@RequestParam String language) {
        List<ReviewerInfoDto> reviewerInfoList = userService.findReviewerByLanguage(language);
        return ResponseEntity.ok(reviewerInfoList);
    }


    /**
     * 로그아웃 API
     */
    @Secured({"ROLE_USER", "ROLE_REVIEWER"})
    @PostMapping("/user/logout")
    public BasicResponseDto logout() {
        SecurityContextHolder.clearContext();

        return new BasicResponseDto(null, "success", "로그아웃 완료", HttpStatus.OK);
    }

    /**
     * 내가 등록한 리뷰요청목록 조회 API
     */
    @GetMapping("/user/requests")
    public ResponseEntity<List<ReviewRequestResponseDto>> getMyRequestList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        List<ReviewRequestResponseDto> response = userService.getMyReviewRequestList(user);

        return ResponseEntity.ok(response);
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

    /**
     * 리뷰요청에 대한 리뷰등록 API
     */
    @PostMapping("/user/reviewer/request")
    public ResponseEntity addReviewAndComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long id,
            @RequestBody AddReviewDto addReviewDto
    ) {
        User reviewer = userDetails.getUser();
        reviewerService.addReviewAndComment(reviewer, id, addReviewDto);
        return ResponseEntity.ok("ok");
    }

    /**
     * 리뷰요청 거절 API
     */
    @GetMapping("/user/reviewer/request/reject")
    public ResponseEntity rejectRequestedReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long id
    ) {
        User user = userDetails.getUser();
        reviewerService.rejectRequestedReview(user, id);

        return ResponseEntity.ok("ok");
    }
}
