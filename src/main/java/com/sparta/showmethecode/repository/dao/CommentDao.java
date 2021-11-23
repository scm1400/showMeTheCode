package com.sparta.showmethecode.repository.dao;

import com.sparta.showmethecode.dto.response.ReviewRequestDetailResponseDto;

public interface CommentDao {

    ReviewRequestDetailResponseDto getReviewRequestDetailWithComment(Long id);
    ReviewRequestDetailResponseDto getReviewRequestDetailWithCommentAdvanced(Long id);

}
