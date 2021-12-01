package com.sparta.showmethecode.repository.dao;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.showmethecode.domain.ReviewAnswer;
import com.sparta.showmethecode.domain.ReviewRequest;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.dto.response.*;
import com.sparta.showmethecode.repository.querydslutil.OrderByNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.sparta.showmethecode.domain.QReviewAnswer.reviewAnswer;
import static com.sparta.showmethecode.domain.QReviewRequest.reviewRequest;
import static com.sparta.showmethecode.domain.QReviewRequestComment.reviewRequestComment;
import static com.sparta.showmethecode.domain.QUser.user;

@Slf4j
public class ReviewRequestDaoImpl extends QuerydslRepositorySupport implements ReviewRequestDao {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public ReviewRequestDaoImpl(JPAQueryFactory jpaQueryFactory, EntityManager entityManager) {
        super(ReviewRequest.class);
        this.em = entityManager;
        this.query = jpaQueryFactory;
    }

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
    public List<ReviewRequestResponseDto> findMyReceivedRequestList(Long id) {
        List<ReviewRequest> result = query.select(reviewRequest)
                .from(reviewRequest)
                .join(reviewRequest.answerUser, user).fetchJoin()
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

    @Override
    public boolean isAnswerToMe(Long answerId, User user) {
        Integer exist = query.selectOne()
                .from(reviewRequest)
                .where(reviewRequest.reviewAnswer.id.eq(answerId).and(reviewRequest.requestUser.id.eq(user.getId())))
                .fetchFirst();

        return exist != null;
    }

    @Override
    public Page<ReviewRequest> searchRequestByLanguageName(String languageName, Pageable pageable, boolean isAsc) {

        List<ReviewRequest> result = query.select(reviewRequest)
                .from(reviewRequest)
                .join(reviewRequest.requestUser, user).fetchJoin()
                .where(reviewRequest.languageName.eq(languageName))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(isAsc ? reviewRequest.createdAt.asc() : reviewRequest.createdAt.desc())
                .fetch();

        JPAQuery<ReviewRequest> jpaQuery = query.select(reviewRequest)
                .from(reviewRequest)
                .where(reviewRequest.languageName.eq(languageName));

        return PageableExecutionUtils.getPage(result, pageable, jpaQuery::fetchCount);
    }

    @Override
    public Page<ReviewAnswerResponseDto> findMyAnswer(Long userId, Pageable pageable) {
        JPAQuery<ReviewAnswerResponseDto> jpaQuery = query.select(
                        new QReviewAnswerResponseDto(
                                reviewRequest.reviewAnswer.id,
                                reviewRequest.id,
                                reviewRequest.reviewAnswer.title,
                                reviewRequest.reviewAnswer.code,
                                reviewRequest.reviewAnswer.comment,
                                reviewRequest.reviewAnswer.point,
                                reviewRequest.reviewAnswer.createdAt
                        )
                ).from(reviewRequest)
                .join(reviewRequest.reviewAnswer, reviewAnswer)
                .where(reviewRequest.reviewAnswer.answerUser.id.eq(userId));

        List<ReviewAnswerResponseDto> result = getQuerydsl().applyPagination(pageable, jpaQuery).fetch();

        return PageableExecutionUtils.getPage(result, pageable, jpaQuery::fetchCount);
    }

    @Override
    public void deleteComment(Long reviewId, Long commentId, Long userId) {
        query.delete(reviewRequest)
                .where(reviewRequest.id.eq(reviewId))
                .where();
    }
}
