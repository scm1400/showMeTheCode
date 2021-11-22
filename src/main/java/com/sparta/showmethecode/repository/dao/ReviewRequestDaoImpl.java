package com.sparta.showmethecode.repository.dao;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.showmethecode.dto.response.QReviewRequestResponseDto;
import com.sparta.showmethecode.dto.response.ReviewRequestResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Objects;

import static com.sparta.showmethecode.domain.QReviewRequest.reviewRequest;
import static com.sparta.showmethecode.domain.QUser.*;

@Slf4j
@RequiredArgsConstructor
public class ReviewRequestDaoImpl implements ReviewRequestDao {

    private final JPAQueryFactory query;

    @Override
    public Page<ReviewRequestResponseDto> findSearchByTitleOrComment(String keyword, Pageable pageable) {

        log.info("findSearchByTitleOrComment page = {}", pageable.getPageNumber());
        log.info("findSearchByTitleOrComment size = {}", pageable.getPageSize());
        log.info("findSearchByTitleOrComment {}", pageable.getSort());

        QueryResults<ReviewRequestResponseDto> results
                = query.select(new QReviewRequestResponseDto(
                        user.username,
                        reviewRequest.title,
                        reviewRequest.comment,
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
    public Page<ReviewRequestResponseDto> findSearchByTitleOrCommentAdvanced(String keyword, Pageable pageable, boolean isAsc) {

        List<ReviewRequestResponseDto> results = query
                .select(new QReviewRequestResponseDto(
                        user.username,
                        reviewRequest.title,
                        reviewRequest.comment,
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
                        user.username,
                        reviewRequest.title,
                        reviewRequest.comment,
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
}
