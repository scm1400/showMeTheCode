package com.sparta.showmethecode.service;

import com.sparta.showmethecode.domain.ReviewRequest;
import com.sparta.showmethecode.domain.ReviewRequestComment;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.dto.request.AddCommentDto;
import com.sparta.showmethecode.dto.request.ReviewRequestUpdateDto;
import com.sparta.showmethecode.repository.ReviewRequestCommentRepository;
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
    private final ReviewRequestCommentRepository reviewRequestCommentRepository;

    /**
     * 코드리뷰요청 - 댓글추가 API
     */
    @Transactional
    public void addComment(User user, Long questionId, AddCommentDto addCommentDto) {
        ReviewRequest reviewRequest = reviewRequestRepository.findById(questionId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰요청입니다."));
        ReviewRequestComment reviewRequestComment = new ReviewRequestComment(addCommentDto.getContent(), user);
        reviewRequest.addComment(reviewRequestComment);
    }

    /**
     * 코드리뷰요청 - 댓글삭제 API
     */
    @Transactional
    public long removeComment(User user, Long questionId, Long commentId) {
//        return 1l;
        return reviewRequestCommentRepository.deleteComment(user.getId(), questionId, commentId);
    }
}
