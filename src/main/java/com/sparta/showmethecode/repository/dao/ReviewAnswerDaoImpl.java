package com.sparta.showmethecode.repository.dao;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.showmethecode.domain.QReviewAnswer;
import com.sparta.showmethecode.domain.QReviewRequest;
import com.sparta.showmethecode.domain.ReviewAnswer;
import com.sparta.showmethecode.domain.UserRole;
import com.sparta.showmethecode.dto.response.QReviewAnswerResponseDto;
import com.sparta.showmethecode.dto.response.ReviewAnswerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sparta.showmethecode.domain.QReviewAnswer.*;
import static com.sparta.showmethecode.domain.QReviewRequest.reviewRequest;

@RequiredArgsConstructor
public class ReviewAnswerDaoImpl implements ReviewAnswerDao{

    private final JPAQueryFactory query;

    @Override
    public boolean isEvaluated(Long answerId) {
        Integer exist = query.selectOne()
                .from(reviewAnswer)
                .where(reviewAnswer.point.gt(0))
                .fetchFirst();

        return exist != null;
    }
}
