package com.sparta.showmethecode.service;

import com.sparta.showmethecode.domain.ReviewRequest;
import com.sparta.showmethecode.domain.ReviewRequestComment;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.dto.request.AddCommentDto;
import com.sparta.showmethecode.dto.request.ReviewRequestUpdateDto;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommentService {

    private final ReviewRequestRepository reviewRequestRepository;

    /**
     * 댓글추가 API
     */
    @Transactional
    public void addComment(User user, Long reviewId, AddCommentDto addCommentDto) {
        ReviewRequest reviewRequest = reviewRequestRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰요청입니다."));
        ReviewRequestComment reviewRequestComment = new ReviewRequestComment(addCommentDto.getContent(), user);
        reviewRequest.addComment(reviewRequestComment);
    }

    /**
     * 댓글삭제 API
     */
    @Transactional
    public void removeComment(User user, Long reviewId, Long commentId) {

    }
}
