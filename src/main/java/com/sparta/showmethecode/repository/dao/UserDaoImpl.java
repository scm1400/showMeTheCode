package com.sparta.showmethecode.repository.dao;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.showmethecode.domain.*;
import com.sparta.showmethecode.dto.response.QReviewerInfoDto;
import com.sparta.showmethecode.dto.response.ReviewRequestListResponseDto;
import com.sparta.showmethecode.dto.response.ReviewRequestResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.showmethecode.domain.QLanguage.language;
import static com.sparta.showmethecode.domain.QReviewRequest.*;
import static com.sparta.showmethecode.domain.QUser.*;
import static com.sparta.showmethecode.domain.QUser.user;

@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JPAQueryFactory query;

    @Override
    public List<User> findReviewerByLanguage(String languageName) {

        List<User> result = query.select(user)
                .from(user)
                .join(user.languages, language)
                .where(user.role.eq(UserRole.ROLE_REVIEWER).and(language.name.eq(languageName)))
                .fetch();

        return result;
    }

    @Override
    public Page<User> getReviewerRanking(Pageable pageable, boolean isAsc) {

        List<User> content = query.select(user)
                .from(user)
                .where(user.role.eq(UserRole.ROLE_REVIEWER).and(user.evalTotal.gt(0)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(isAsc ? user.evalTotal.asc() : user.evalTotal.desc())
                .fetch();

        JPAQuery<User> jpaQuery = query.select(user)
                .from(user)
                .where(user.role.eq(UserRole.ROLE_REVIEWER).and(user.evalTotal.gt(0)));


        return PageableExecutionUtils.getPage(content, pageable, jpaQuery::fetchCount);
    }
}
