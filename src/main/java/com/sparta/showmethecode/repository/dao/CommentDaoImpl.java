package com.sparta.showmethecode.repository.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.showmethecode.domain.QUser;
import com.sparta.showmethecode.domain.ReviewRequest;
import com.sparta.showmethecode.domain.ReviewRequestComment;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.dto.response.CommentResponseDto;
import com.sparta.showmethecode.dto.response.ReviewRequestDetailResponseDto;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.sparta.showmethecode.domain.QReviewRequest.reviewRequest;
import static com.sparta.showmethecode.domain.QReviewRequestComment.reviewRequestComment;
import static com.sparta.showmethecode.domain.QUser.*;

@RequiredArgsConstructor
public class CommentDaoImpl implements CommentDao {

    private final JPAQueryFactory query;

    @Override
    public ReviewRequestDetailResponseDto getReviewRequestDetailWithCommentAdvanced(Long id) {

        List<ReviewRequestComment> reviewRequestComments = query.select(reviewRequestComment)
                .from(reviewRequestComment)
                .join(reviewRequestComment.user, user).fetchJoin()
                .join(reviewRequestComment.reviewRequest, reviewRequest).fetchJoin()
                .where(reviewRequest.id.eq(id))
                .fetch();

        ReviewRequest reviewRequest = reviewRequestComments.get(0).getReviewRequest();
        List<CommentResponseDto> comments = new ArrayList<>();

        for (ReviewRequestComment comment : reviewRequestComments) {
            User user = comment.getUser();
            CommentResponseDto commentResponseDto = new CommentResponseDto(
                    comment.getId(),
                    user.getId(),
                    user.getUsername(),
                    comment.getContent(),
                    comment.getCreatedAt()
            );
            comments.add(commentResponseDto);
        }

        return new ReviewRequestDetailResponseDto(
                id,
                reviewRequest.getRequestUser().getUsername(),
                reviewRequest.getTitle(),
                reviewRequest.getCode(),
                reviewRequest.getComment(),
                reviewRequest.getStatus().toString(),
                reviewRequest.getCreatedAt(),
                comments
        );
    }

    @Override
    public ReviewRequestDetailResponseDto getReviewRequestDetailWithComment(Long id) {
        List<ReviewRequestComment> reviewRequestComments = query.select(reviewRequestComment)
                .from(reviewRequestComment)
                .join(reviewRequestComment.user, user)
                .join(reviewRequestComment.reviewRequest, reviewRequest)
                .where(reviewRequestComment.reviewRequest.id.eq(id))
                .fetch();

        ReviewRequest reviewRequest = reviewRequestComments.get(0).getReviewRequest();
        List<CommentResponseDto> comments = new ArrayList<>();

        for (ReviewRequestComment comment : reviewRequestComments) {
            User user = comment.getUser();
            CommentResponseDto commentResponseDto = new CommentResponseDto(
                    comment.getId(),
                    user.getId(),
                    user.getUsername(),
                    comment.getContent(),
                    comment.getCreatedAt()
            );
            comments.add(commentResponseDto);
        }

        return new ReviewRequestDetailResponseDto(
                id,
                reviewRequest.getRequestUser().getUsername(),
                reviewRequest.getTitle(),
                reviewRequest.getCode(),
                reviewRequest.getComment(),
                reviewRequest.getStatus().toString(),
                reviewRequest.getCreatedAt(),
                comments
        );
    }
}
