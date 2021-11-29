package com.sparta.showmethecode.service;

import com.sparta.showmethecode.domain.ReviewAnswer;
import com.sparta.showmethecode.domain.ReviewRequest;
import com.sparta.showmethecode.domain.ReviewRequestStatus;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.dto.request.AddReviewDto;
import com.sparta.showmethecode.dto.response.ReviewRequestListResponseDto;
import com.sparta.showmethecode.dto.response.ReviewerInfoDto;
import com.sparta.showmethecode.repository.ReviewAnswerRepository;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import com.sparta.showmethecode.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewerService {

    private final ReviewAnswerRepository reviewAnswerRepository;
    private final ReviewRequestRepository reviewRequestRepository;
    private final UserRepository userRepository;

    /**
     * 리뷰요청에 대한 리뷰등록 API
     * 자신에게 요청된 리뷰가 아닌 경우에 대한 처리 필요
     */
    @Transactional
    public void addReviewAndComment(User reviewer, Long reviewId, AddReviewDto addReviewDto) {
        if (isRequestedToMe(reviewId, reviewer)) {
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

            reviewRequest.setStatus(ReviewRequestStatus.COMPLETED);
            reviewRequest.setReviewAnswer(savedReviewAnswer);
        }
    }

    /**
     * 리뷰요청 거절 API
     * 자신에게 요청된 리뷰가 아닌 경우에 대한 처리 필요
     */
    @Transactional
    public void rejectRequestedReview(User reviewer, Long questionId) {
        if (isRequestedToMe(questionId, reviewer)) {
            ReviewRequest reviewRequest = reviewRequestRepository.findById(questionId).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 리뷰요청입니다.")
            );

            reviewRequest.setStatus(ReviewRequestStatus.REJECTED);
        }
    }

    /**
     * 나에게 요청된 리뷰인지 확인
     */
    private boolean isRequestedToMe(Long questionId, User reviewer) {
        return reviewRequestRepository.isRequestedToMe(questionId, reviewer);
    }

    /**
     * 리뷰어 랭킹 조회 API (상위 5명)
     */
    public List<ReviewerInfoDto> getReviewerTop5Ranking() {
        return userRepository.findTop5ByOrderByEvalTotalDesc().stream().map(
                u -> new ReviewerInfoDto(
                        u.getId(),
                        u.getUsername(),
                        u.getLanguages().stream().map(
                                l -> new String(l.toString())
                        ).collect(Collectors.toList()),
                        u.getAnswerCount(),
                        u.getEvalTotal() / u.getEvalCount()
                )
        ).collect(Collectors.toList());
    }
}
