package com.sparta.showmethecode.repository.dao;

import antlr.StringUtils;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.showmethecode.domain.QReviewRequest;
import com.sparta.showmethecode.domain.QUser;
import com.sparta.showmethecode.domain.ReviewRequest;
import com.sparta.showmethecode.dto.response.QReviewRequestResponseDto;
import com.sparta.showmethecode.dto.response.ReviewRequestListResponseDto;
import com.sparta.showmethecode.dto.response.ReviewRequestResponseDto;
import com.sparta.showmethecode.util.QueryDslUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sparta.showmethecode.domain.QReviewRequest.*;
import static com.sparta.showmethecode.domain.QUser.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@RequiredArgsConstructor
public class ReviewRequestDaoImpl implements ReviewRequestDao {

    private final JPAQueryFactory query;

    @Override
    public ReviewRequestListResponseDto findSearchByTitleOrComment(String keyword, Pageable pageable) {

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

        int totalElements = (int) results.getTotal();
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        int totalPage = totalElements / size;
        totalPage = totalElements % size >= 1 ? totalPage+1 : totalPage;

        return new ReviewRequestListResponseDto(results.getResults(), totalPage,(int) results.getTotal(), pageable.getPageNumber(), pageable.getPageSize());
    }

    private BooleanExpression containingTitleOrComment(String keyword) {
        return Objects.isNull(keyword) || keyword.isEmpty() ? null : reviewRequest.title.contains(keyword).or(reviewRequest.comment.contains(keyword));
    }
}
