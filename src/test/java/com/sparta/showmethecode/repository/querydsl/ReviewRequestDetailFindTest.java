package com.sparta.showmethecode.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.showmethecode.domain.*;
import com.sparta.showmethecode.repository.LanguageRepository;
import com.sparta.showmethecode.repository.ReviewRequestCommentRepository;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import com.sparta.showmethecode.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static com.sparta.showmethecode.domain.QReviewRequest.reviewRequest;
import static com.sparta.showmethecode.domain.QReviewRequestComment.*;

@SpringBootTest
public class ReviewRequestDetailFindTest {

    @Autowired
    JPAQueryFactory query;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LanguageRepository languageRepository;
    @Autowired
    ReviewRequestRepository reviewRequestRepository;
    @Autowired
    ReviewRequestCommentRepository reviewRequestCommentRepository;


    @BeforeEach
    void init() {

    }

    @Test
    void test() {
        ReviewRequest review = reviewRequestRepository.findAll().stream().findFirst().get();

        List<ReviewRequestComment> fetch = query.select(reviewRequestComment)
                .from(reviewRequestComment)
                .join(reviewRequestComment.reviewRequest, reviewRequest).fetchJoin()
                .fetch();

        for (ReviewRequestComment reviewRequestComment : fetch) {
            System.out.println(reviewRequestComment.getReviewRequest().getTitle() + ": " + reviewRequestComment.getContent());
        }

    }

    private User createNormalUser(String username, String password) {
        User user = new User(username, password, UserRole.ROLE_USER, 0, 0, 0);
        return userRepository.save(user);
    }

    private User createReviewer(String username, String password, String language) {
        Language savedLanguage = languageRepository.save(new Language(language));
        User user = new User(username, password, UserRole.ROLE_REVIEWER, 0, 0, 0, Arrays.asList(savedLanguage));
        return userRepository.save(user);
    }
}
