package com.sparta.showmethecode.repository.dao;

import com.sparta.showmethecode.dto.response.ReviewRequestListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

public interface ReviewRequestDao {

    ReviewRequestListResponseDto findSearchByTitleOrComment(String keyword, Pageable pageable);
}
