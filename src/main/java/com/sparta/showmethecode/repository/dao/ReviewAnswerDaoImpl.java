package com.sparta.showmethecode.repository.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.showmethecode.domain.QReviewAnswer;
import lombok.RequiredArgsConstructor;

import static com.sparta.showmethecode.domain.QReviewAnswer.*;

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
