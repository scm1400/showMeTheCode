package com.sparta.showmethecode.service;

import com.sparta.showmethecode.domain.ReviewAnswer;
import com.sparta.showmethecode.domain.ReviewRequest;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.dto.request.AddReviewDto;
import com.sparta.showmethecode.dto.response.ReviewRequestListResponseDto;
import com.sparta.showmethecode.repository.ReviewAnswerRepository;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import com.sparta.showmethecode.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewerService {

    private final ReviewAnswerRepository reviewAnswerRepository;
    private final ReviewRequestRepository reviewRequestRepository;

    /**
     * 리뷰요청에 대한 리뷰등록 API
     */
    @Transactional
    public void addReviewAndComment(User reviewer, Long reviewId, AddReviewDto addReviewDto) {
        ReviewAnswer reviewAnswer = ReviewAnswer.builder()
                .title(addReviewDto.getTitle())
                .code(addReviewDto.getCode())
                .comment(addReviewDto.getComment())
                .answerUser(reviewer)
                .build();
        ReviewAnswer savedReviewAnswer = reviewAnswerRepository.save(reviewAnswer);

        ReviewRequest reviewRequest = reviewRequestRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 리뷰요청입니다.")
        );

        reviewRequest.setReviewAnswer(savedReviewAnswer);
    }
}
