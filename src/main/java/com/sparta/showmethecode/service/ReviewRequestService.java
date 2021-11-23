package com.sparta.showmethecode.service;

import com.sparta.showmethecode.domain.ReviewRequest;
import com.sparta.showmethecode.domain.ReviewRequestStatus;
import com.sparta.showmethecode.dto.request.ReviewRequestDto;
import com.sparta.showmethecode.dto.response.ReviewRequestDetailResponseDto;
import com.sparta.showmethecode.dto.response.ReviewRequestListResponseDto;
import com.sparta.showmethecode.dto.response.ReviewRequestResponseDto;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
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

@RequiredArgsConstructor
@Slf4j
@Service
public class ReviewRequestService {

    private final ReviewRequestRepository reviewRequestRepository;

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
                                r.getStatus().toString(),
                                r.getCreatedAt()
                        )
                ).collect(Collectors.toList());

        return new ReviewRequestListResponseDto(reviewRequestResponseDtos, reviewRequests.getTotalPages(), (int) reviewRequests.getTotalElements(), page, size);
    }

    /**
     * 코드리뷰 요청 API
     */
    @Transactional(readOnly = true)
    public void addReviewRequest(ReviewRequestDto requestDto) {
        ReviewRequest reviewRequest
                = new ReviewRequest(null, requestDto.getTitle(), requestDto.getCode(), requestDto.getComment(), ReviewRequestStatus.REQUESTED, requestDto.getLanguage().toUpperCase());

        reviewRequestRepository.save(reviewRequest);
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
        ReviewRequest review = reviewRequestRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 코드리뷰입니다.")
        );

        return new ReviewRequestDetailResponseDto(
                review.getId(),
                review.getRequestUser().getUsername(),
                review.getTitle(),
                review.getCode(),
                review.getComment(),
                review.getStatus().toString(),
                review.getCreatedAt()
        );
    }
}
