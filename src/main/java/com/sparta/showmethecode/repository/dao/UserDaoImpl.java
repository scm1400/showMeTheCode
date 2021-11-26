package com.sparta.showmethecode.repository.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.showmethecode.domain.QLanguage;
import com.sparta.showmethecode.domain.QUser;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.domain.UserRole;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sparta.showmethecode.domain.QLanguage.language;
import static com.sparta.showmethecode.domain.QUser.user;

@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JPAQueryFactory query;

    @Override
    public List<User> findReviewerByLanguage(String languageName) {

        List<User> result = query.select(QUser.user)
                .from(QUser.user)
                .join(QUser.user.languages, language)
                .where(QUser.user.role.eq(UserRole.ROLE_REVIEWER).and(language.name.eq(languageName)))
                .fetch();

        return result;
    }
}
