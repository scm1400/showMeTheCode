package com.sparta.showmethecode.service;

import com.sparta.showmethecode.domain.ReviewRequest;
import com.sparta.showmethecode.domain.ReviewRequestStatus;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.dto.request.ReviewRequestDto;
import com.sparta.showmethecode.dto.request.ReviewRequestUpdateDto;
import com.sparta.showmethecode.dto.response.*;
import com.sparta.showmethecode.repository.ReviewRequestCommentRepository;
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
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReviewRequestService {

    private final ReviewRequestRepository reviewRequestRepository;
    private final UserRepository userRepository;

    /**
     * 코드리뷰 요청목록 API
     */
    @Transactional(readOnly = true)
    public ReviewRequestListResponseDto getReviewRequestList(int page, int size, String sortBy, boolean isAsc) {
        Pageable pageable = makePageable(page, size, sortBy, isAsc);

        Page<ReviewRequest> reviewRequests = reviewRequestRepository.findAll(pageable);
        List<ReviewRequestResponseDto> reviewRequestResponseDtos = reviewRequests.getContent().stream()
                .map(r -> new ReviewRequestResponseDto(
                                r.getId(),
                                r.getRequestUser().getUsername(),
                                r.getTitle(),
                                r.getComment(),
                                r.getLanguageName(),
                                r.getStatus().toString(),
                                r.getCreatedAt()
                        )
                ).collect(Collectors.toList());

        return new ReviewRequestListResponseDto(reviewRequestResponseDtos, reviewRequests.getTotalPages(), (int) reviewRequests.getTotalElements(), page, size);
    }

    /**
     * 코드리뷰 요청 API
     */
    @Transactional
    public void addReviewRequest(ReviewRequestDto requestDto, User user) {
        ReviewRequest reviewRequest
                = new ReviewRequest(user, requestDto.getTitle(), requestDto.getCode(), requestDto.getComment(), ReviewRequestStatus.REQUESTED, requestDto.getLanguage().toUpperCase());

        reviewRequestRepository.save(reviewRequest);
    }

    /**
     * 코드리뷰 수정 API
     */
    @Transactional
    public void updateReviewRequest(ReviewRequestUpdateDto updateDto, Long reviewId, User user) {
        boolean isMyRequest = reviewRequestRepository.isMyReviewRequest(reviewId, user);
        if (isMyRequest) {
            User newAnswerUser = null;
            if (!Objects.isNull(updateDto.getReviewerId())) {
                newAnswerUser = userRepository.findById(updateDto.getReviewerId()).orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 리뷰어입니다.")
                );
            }
            ReviewRequest reviewRequest = reviewRequestRepository.findById(reviewId).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 요청입니다.")
            );

            reviewRequest.update(updateDto, newAnswerUser);
        }
    }

    /**
     * 코드리뷰 삭제 API
     */
    @Transactional
    public void deleteReviewRequest(Long reviewId, User user){
        boolean isMyRequest = reviewRequestRepository.isMyReviewRequest(reviewId, user);
        if (isMyRequest) {
            reviewRequestRepository.deleteById(reviewId);
        }
    }

    /**
     * 코드리뷰 검색 API
     */
    @Transactional(readOnly = true)
    public ReviewRequestListResponseDto searchByTitleOrComment(
            String keyword,
            int page, int size, String sortBy, boolean isAsc
    ) {
        Pageable pageable = makePageable(page, size, sortBy, isAsc);
        Page<ReviewRequestResponseDto> results = reviewRequestRepository.findSearchByTitleOrCommentAdvanced(keyword, pageable, isAsc);

        return new ReviewRequestListResponseDto(results.getContent(), results.getTotalPages(), (int) results.getTotalElements(), page, size);
    }


    private Pageable makePageable(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        return PageRequest.of(page, size, sort);
    }

    /**
     * 코드리뷰 단건조회 API (코드리뷰 요청 상세정보)
     */
    @Transactional(readOnly = true)
    public ReviewRequestDetailResponseDto getReviewRequest(Long id) {
        ReviewRequestDetailResponseDto reviewRequestDetailWithComment = reviewRequestRepository.getReviewRequestDetailWithComment(id);
        return reviewRequestDetailWithComment;
    }

    /**
     * 코드리뷰 요청 언어별 카운팅 API
     */
    @Transactional(readOnly = true)
    public List<ReviewRequestLanguageCount> getCountGroupByLanguageName() {
        return reviewRequestRepository.getReviewRequestLanguageCountGroupByLanguage();
    }

    /**
     * 코드리뷰 요청 언어이름 검색 API
     */
    public PageResponseDto<ReviewRequestResponseDto> searchRequestByLanguageName(String language, int page, int size, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ReviewRequest> reviewRequests = reviewRequestRepository.searchRequestByLanguageName(language, pageable, isAsc);

        List<ReviewRequestResponseDto> collect = reviewRequests.stream().map(
                r -> new ReviewRequestResponseDto(
                        r.getId(),
                        r.getRequestUser().getUsername(),
                        r.getTitle(),
                        r.getComment(),
                        r.getLanguageName(),
                        r.getStatus().toString(),
                        r.getCreatedAt()
                )
        ).collect(Collectors.toList());

        return new PageResponseDto<ReviewRequestResponseDto>(
                collect,
                reviewRequests.getTotalPages(),
                reviewRequests.getTotalElements(),
                page, size
        );
    }
}
