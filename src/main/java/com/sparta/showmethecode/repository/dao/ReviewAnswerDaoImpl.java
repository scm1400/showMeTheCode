package com.sparta.showmethecode.repository.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.showmethecode.domain.ReviewRequest;
import com.sparta.showmethecode.dto.response.ReviewAnswerResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.showmethecode.domain.QReviewRequest.reviewRequest;
import static com.sparta.showmethecode.domain.QUser.user;

@Slf4j
@RequiredArgsConstructor
public class ReviewAnswerDaoImpl {

    private final JPAQueryFactory query;

//    @Override
//    public List<ReviewAnswerResponseDto> findMyReceivedRequestList(Long id) {
//        List<ReviewRequest> result = query.select(reviewRequest)
//                .from(reviewRequest)
//                .join(reviewRequest.requestUser, user).fetchJoin()
//                .where(user.id.eq(id))
//                .fetch();
//        return result.stream().map(
//                r -> new ReviewAnswerResponseDto(
//                        r.getId(),
//                        r.getRequestUser().getUsername(),
//                        r.getTitle(),
//                        r.getComment(),
//                        r.getCreatedAt()
//                )
//        ).collect(Collectors.toList());
//    }
}
