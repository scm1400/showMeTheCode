package com.sparta.showmethecode.controller;

import com.sparta.showmethecode.dto.response.ReviewerInfoDto;
import com.sparta.showmethecode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    /**
     * 해당 언어의 리뷰어 조회 API
     */
    @GetMapping("/user/language")
    public ResponseEntity<List<ReviewerInfoDto>> findReviewerByLanguage(@RequestParam String language) {
        List<ReviewerInfoDto> reviewerInfoList = userService.findReviewerByLanguage(language);
        return ResponseEntity.ok(reviewerInfoList);
    }
}
