package com.sparta.showmethecode.service;

import com.sparta.showmethecode.domain.ReviewRequest;
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

    @Transactional(readOnly = true)
    public ReviewRequestListResponseDto getReviewRequestList(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ReviewRequest> reviewRequests = reviewRequestRepository.findAll(pageable);
        List<ReviewRequestResponseDto> reviewRequestResponseDtos = reviewRequests.getContent().stream()
                .map(r -> new ReviewRequestResponseDto(
                                r.getRequestUser().getUsername(),
                                r.getTitle(),
                                r.getComment(),
                                r.getStatus().toString(),
                                r.getCreatedAt()
                        )
                ).collect(Collectors.toList());

        return new ReviewRequestListResponseDto(reviewRequestResponseDtos, reviewRequests.getTotalPages(), reviewRequests.getNumberOfElements(), page, size);
    }
}
