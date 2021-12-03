package com.sparta.showmethecode.service;

import com.sparta.showmethecode.domain.ReviewAnswer;
import com.sparta.showmethecode.domain.ReviewRequest;
import com.sparta.showmethecode.domain.ReviewRequestStatus;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.dto.request.AddAnswerDto;
import com.sparta.showmethecode.dto.request.EvaluateAnswerDto;
import com.sparta.showmethecode.dto.request.UpdateAnswerDto;
import com.sparta.showmethecode.dto.response.*;
import com.sparta.showmethecode.repository.ReviewAnswerRepository;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import com.sparta.showmethecode.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public void addReviewAndComment(User reviewer, Long reviewId, AddAnswerDto addAnswerDto) {
        if (isRequestedToMe(reviewId, reviewer)) {
            ReviewAnswer reviewAnswer = ReviewAnswer.builder()
                    .title(addAnswerDto.getTitle())
                    .content(addAnswerDto.getContent())
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
     * 리뷰어 랭킹 조회 API (전체랭킹 조회)
     */
    @Transactional(readOnly = true)
    public PageResponseDto<ReviewerInfoDto> getReviewerRanking(
            int page, int size, boolean isAsc
    ) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "evalTotal");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> result = userRepository.getReviewerRanking(pageable, isAsc);

        List<ReviewerInfoDto> reviewerInfo = result.getContent().stream().map(
                u -> new ReviewerInfoDto(
                        u.getId(),
                        u.getUsername(),
                        u.getLanguages().stream().map(l -> new String(l.getName())).collect(Collectors.toList()),
                        u.getAnswerCount(),
                        u.getEvalTotal() / u.getEvalCount()
                )
        ).collect(Collectors.toList());

        return new PageResponseDto<ReviewerInfoDto>(
                reviewerInfo,
                result.getTotalPages(),
                result.getTotalElements(),
                page, size
        );
    }

    /**
     * 리뷰어 랭킹 조회 API (상위 5명)
     */
    @Transactional(readOnly = true)
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

    /**
     * 내가 답변한 리뷰목록 조회 API
     */
    @Transactional(readOnly = true)
    public PageResponseDto<ReviewAnswerResponseDto> getMyAnswerList(User reviewer, int page, int size, boolean isAsc, String sortBy) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ReviewAnswerResponseDto> myAnswer = reviewRequestRepository.findMyAnswer(reviewer.getId(), pageable);

        return new PageResponseDto<ReviewAnswerResponseDto>(
                myAnswer.getContent(),
                myAnswer.getTotalPages(),
                myAnswer.getTotalElements(),
                page, size
        );
    }

    /**
     * 답변한 리뷰 수정 API
     */
    @Transactional
    public void updateAnswer(User reviewer, Long answerId, UpdateAnswerDto updateAnswerDto) {
        if (isMyAnswer(reviewer.getId(), answerId)) {
            ReviewAnswer reviewAnswer = reviewAnswerRepository.findById(answerId).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 답변입니다.")
            );

            reviewAnswer.update(updateAnswerDto);
        }
    }

    private boolean isMyAnswer(Long reviewerId, Long answerId) {
        return reviewAnswerRepository.isMyAnswer(reviewerId, answerId);
    }

    /**
     * 나에게 요청온 리뷰 조회
     */
    public List<ReviewRequestResponseDto> getMyReceivedRequestList(User user) {
        return reviewRequestRepository.findMyReceivedRequestList(user.getId());
    }

    /**
     * 답변에 대한 평가 API
     *
     * 평가하고자 하는 답변이 내가 요청한 코드리뷰에 대한 답변인지 확인해야 함
     */
    @Transactional
    public void evaluateAnswer(User user, Long answerId, EvaluateAnswerDto evaluateAnswerDto) {
        if(reviewRequestRepository.isAnswerToMe(answerId, user)) {
            ReviewAnswer reviewAnswer = reviewAnswerRepository.findById(answerId).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 답변입니다.")
            );

            reviewAnswer.evaluate(evaluateAnswerDto.getPoint());
            reviewAnswer.getAnswerUser().evaluate(evaluateAnswerDto.getPoint());
        }
    }
}
