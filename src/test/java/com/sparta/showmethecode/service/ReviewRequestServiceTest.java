package com.sparta.showmethecode.service;

import com.sparta.showmethecode.domain.*;
import com.sparta.showmethecode.dto.response.PageResponseDto;
import com.sparta.showmethecode.dto.response.ReviewRequestResponseDto;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import com.sparta.showmethecode.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Transactional
@SpringBootTest
public class ReviewRequestServiceTest {


    @Autowired
    UserRepository userRepository;
    @Autowired
    ReviewRequestRepository reviewRequestRepository;
    @Autowired
    ReviewRequestService reviewRequestService;


    @BeforeEach
    void init() {
        User user1 = createUser("user1", "user1", UserRole.ROLE_USER);
        User reviewer1 = createUser("reviewer1", "reviewer1", UserRole.ROLE_REVIEWER);
        userRepository.saveAll(Arrays.asList(user1, reviewer1));

        ReviewRequest reviewRequest1 = createReviewRequest(user1, reviewer1, "title1", "code1", "comment1", "Java");
        ReviewRequest reviewRequest2 = createReviewRequest(user1, reviewer1, "title2", "code2", "comment2", "Python");
        ReviewRequest reviewRequest3 = createReviewRequest(user1, reviewer1, "title3", "code3", "comment3", "Java");
        reviewRequestRepository.saveAll(Arrays.asList(reviewRequest1, reviewRequest2, reviewRequest3));
    }

    @DisplayName("1. 언어이름으로 검색 테스트")
    @Test
    void 언어이름_검색() {
        PageResponseDto<ReviewRequestResponseDto> result = reviewRequestService.searchRequestByLanguageName("Java", 0, 10, true);
        List<ReviewRequestResponseDto> data = result.getData();

        data.forEach(System.out::println);

        Assertions.assertEquals(2, data.size());
    }




    private ReviewRequest createReviewRequest(User requestUser, User answerUser, String title, String code, String comment, String language) {
        return new ReviewRequest(
                requestUser, answerUser,
                title, code, comment, ReviewRequestStatus.REQUESTED, language
        );
    }
    private User createUser(String username, String password, UserRole userRole) {
        return User.builder()
                .username(username)
                .password(password)
                .answerCount(0)
                .evalCount(0)
                .evalTotal(0)
                .role(userRole)
                .languages(Arrays.asList(new Language("Java"))).build();
    }
}
