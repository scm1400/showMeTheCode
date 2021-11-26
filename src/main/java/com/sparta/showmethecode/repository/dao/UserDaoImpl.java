package com.sparta.showmethecode.repository.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.showmethecode.domain.*;
import com.sparta.showmethecode.dto.response.ReviewRequestResponseDto;
import lombok.RequiredArgsConstructor;

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
}
