package com.sparta.showmethecode.repository.dao;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.showmethecode.domain.*;
import com.sparta.showmethecode.dto.response.*;
import com.sparta.showmethecode.repository.querydslutil.OrderByNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.sparta.showmethecode.domain.QReviewRequest.*;
import static com.sparta.showmethecode.domain.QReviewRequest.reviewRequest;
import static com.sparta.showmethecode.domain.QReviewRequestComment.*;
import static com.sparta.showmethecode.domain.QUser.*;

@Slf4j
@RequiredArgsConstructor
public class ReviewRequestDaoImpl implements ReviewRequestDao {

    private final JPAQueryFactory query;

    @Override
    public Page<ReviewRequestResponseDto> findSearchByTitleOrCommentAdvanced(String keyword, Pageable pageable, boolean isAsc) {

        List<ReviewRequestResponseDto> results = query
                .select(new QReviewRequestResponseDto(
                        reviewRequest.id,
                        user.username,
                        reviewRequest.title,
                        reviewRequest.comment,
                        reviewRequest.languageName,
                        reviewRequest.status.stringValue(),
                        reviewRequest.createdAt)
                )
                .from(reviewRequest)
                .join(reviewRequest.requestUser, user)
                .where(containingTitleOrComment(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(isAsc ? reviewRequest.createdAt.desc() : reviewRequest.createdAt.asc())
                .fetch();

        JPAQuery<ReviewRequestResponseDto> jpaQuery = query
                .select(new QReviewRequestResponseDto(
                        reviewRequest.id,
                        user.username,
                        reviewRequest.title,
                        reviewRequest.comment,
                        reviewRequest.languageName,
                        reviewRequest.status.stringValue(),
                        reviewRequest.createdAt)
                )
                .from(reviewRequest)
                .join(reviewRequest.requestUser, user)
                .where(containingTitleOrComment(keyword));

        return PageableExecutionUtils.getPage(results, pageable, jpaQuery::fetchCount);
    }

    private BooleanExpression containingTitleOrComment(String keyword) {
        return Objects.isNull(keyword) || keyword.isEmpty() ? null : reviewRequest.title.contains(keyword).or(reviewRequest.comment.contains(keyword));
    }

    @Override
    public Page<ReviewRequestResponseDto> findSearchByTitleOrComment(String keyword, Pageable pageable) {

        QueryResults<ReviewRequestResponseDto> results
                = query.select(new QReviewRequestResponseDto(
                        reviewRequest.id,
                        user.username,
                        reviewRequest.title,
                        reviewRequest.comment,
                        reviewRequest.languageName,
                        reviewRequest.status.stringValue(),
                        reviewRequest.createdAt)
                )
                .from(reviewRequest)
                .join(reviewRequest.requestUser, user)
                .where(containingTitleOrComment(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ReviewRequestResponseDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public ReviewRequestDetailResponseDto getReviewRequestDetailWithComment(Long id) {

        ReviewRequest result = query.select(reviewRequest).distinct()
                .from(reviewRequest)
                .join(reviewRequest.reviewRequestComments, reviewRequestComment).fetchJoin()
                .join(reviewRequest.requestUser, user).fetchJoin()
                .where(reviewRequest.id.eq(id))
                .fetchFirst();

        List<CommentResponseDto> comments = result.getReviewRequestComments().stream().map(
                c -> new CommentResponseDto(c.getId(), c.getUser().getId(), c.getUser().getUsername(), c.getContent(), c.getCreatedAt())
        ).collect(Collectors.toList());


        return new ReviewRequestDetailResponseDto(
                result.getId(), result.getRequestUser().getUsername(), result.getTitle(), result.getCode(), result.getComment(),
                result.getStatus().toString(), result.getCreatedAt(), comments
        );
    }

    @Override
    public List<ReviewRequestLanguageCount> getReviewRequestLanguageCountGroupByLanguage() {
        List<Tuple> result = query.select(reviewRequest.languageName, reviewRequest.id.count())
                .from(reviewRequest)
                .groupBy(reviewRequest.languageName)
                .orderBy(OrderByNull.DEFAULT)
                .fetch();

        return result.stream().map(
                r -> new ReviewRequestLanguageCount(r.get(0, String.class), r.get(1, Long.class))
        ).collect(Collectors.toList());
    }

    @Override
    public List<ReviewRequestResponseDto> findMyReviewRequestList(Long id) {
        List<ReviewRequest> result = query.select(reviewRequest)
                .from(reviewRequest)
                .join(reviewRequest.requestUser, user).fetchJoin()
                .where(user.id.eq(id))
                .fetch();
        return result.stream().map(
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
    }

    @Override
    public boolean isMyReviewRequest(Long reviewId, User user) {
        Integer exist = query.selectOne()
                .from(reviewRequest)
                .where(reviewRequest.id.eq(reviewId).and(reviewRequest.requestUser.eq(user)))
                .fetchFirst();

        return exist != null;
    }

    @Override
    public boolean isRequestedToMe(Long reviewId, User reviewer) {
        Integer exist = query.selectOne()
                .from(reviewRequest)
                .where(reviewRequest.id.eq(reviewId).and(reviewRequest.answerUser.eq(reviewer)))
                .fetchFirst();

        return exist != null;
    }
}
