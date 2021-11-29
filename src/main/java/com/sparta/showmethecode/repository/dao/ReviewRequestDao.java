package com.sparta.showmethecode.repository.dao;

import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.dto.response.ReviewRequestDetailResponseDto;
import com.sparta.showmethecode.dto.response.ReviewRequestLanguageCount;
import com.sparta.showmethecode.dto.response.ReviewRequestResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRequestDao {

    Page<ReviewRequestResponseDto> findSearchByTitleOrComment(String keyword, Pageable pageable);
    // 코드리뷰요청 목록 제목+내용 검색쿼리
    Page<ReviewRequestResponseDto> findSearchByTitleOrCommentAdvanced(String keyword, Pageable pageable, boolean isAsc);
    // 코드리뷰요청 상세정보 조회 (댓글포함)
    ReviewRequestDetailResponseDto getReviewRequestDetailWithComment(Long id);
    // 언어별 코드리뷰요청 카운팅
    List<ReviewRequestLanguageCount> getReviewRequestLanguageCountGroupByLanguage();
    // 자신이 요청한 리뷰 조회
    List<ReviewRequestResponseDto> findMyReviewRequestList(Long userId);

    // 내가 요청한 리뷰가 맞는지 체크
    boolean isMyReviewRequest(Long reviewId, User user);
    // 나에게 요청된 리뷰가 맞는지 체크
    boolean isRequestedToMe(Long reviewId, User reviewer);
}
