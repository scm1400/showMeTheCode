package com.sparta.showmethecode.repository.dao;

import com.sparta.showmethecode.domain.Language;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.domain.UserRole;
import com.sparta.showmethecode.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class UserDaoTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        User user1 = new User("user1", "pass1", UserRole.ROLE_REVIEWER, 0, 0);
        User user2 = new User("user2", "pass2", UserRole.ROLE_REVIEWER, 0, 0);

        user1.addLanguage(new Language("JAVA"));
        user1.addLanguage(new Language("PYTHON"));

        user2.addLanguage(new Language("JAVASCRIPT"));
        user2.addLanguage(new Language("JAVA"));

        userRepository.saveAll(Arrays.asList(user1, user2));
    }

    @Test
    void 언어별_리뷰어_조회() {
        List<User> users = userRepository.findReviewerByLanguage("JAVA");

        users.forEach(

        );

        Assertions.assertEquals(2, users.size());
    }
}
