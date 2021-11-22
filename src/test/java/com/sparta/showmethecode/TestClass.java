package com.sparta.showmethecode;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.showmethecode.domain.QUser;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.domain.UserRole;
import com.sparta.showmethecode.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static com.sparta.showmethecode.domain.QUser.*;

@SpringBootTest
public class TestClass {

    @Autowired
    UserRepository userRepository;
    @Autowired
    JPAQueryFactory query;

    @BeforeEach
    void init() {
        User user = new User("test", "test", UserRole.ROLE_USER, 0, 0);
        userRepository.save(user);
    }

    @Test
    void test() {
        List<User> users = query.selectFrom(user)
                .fetch();

        users.forEach(u -> System.out.println(u.getUsername()));
        Assertions.assertEquals(1, users.size());
    }
}
