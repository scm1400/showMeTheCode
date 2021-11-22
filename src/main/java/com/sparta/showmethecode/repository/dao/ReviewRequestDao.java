package com.sparta.showmethecode.repository.dao;

import com.sparta.showmethecode.dto.response.ReviewRequestResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRequestDao {

    Page<ReviewRequestResponseDto> findSearchByTitleOrComment(String keyword, Pageable pageable);

    Page<ReviewRequestResponseDto> findSearchByTitleOrCommentAdvanced(String keyword, Pageable pageable);
}
