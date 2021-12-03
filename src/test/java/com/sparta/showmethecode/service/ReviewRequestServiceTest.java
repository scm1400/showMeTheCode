package com.sparta.showmethecode.service;

import com.sparta.showmethecode.domain.*;
import com.sparta.showmethecode.dto.request.AddCommentDto;
import com.sparta.showmethecode.dto.request.AddAnswerDto;
import com.sparta.showmethecode.dto.response.PageResponseDto;
import com.sparta.showmethecode.dto.response.ReviewRequestDetailResponseDto;
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
    @Autowired
    ReviewerService reviewerService;
    @Autowired
    CommentService commentService;

    @BeforeEach
    void init() {
        User user1 = createUser("user1", "user1", UserRole.ROLE_USER);
        User user2 = createUser("user2", "user2", UserRole.ROLE_USER);
        User reviewer1 = createUser("reviewer1", "reviewer1", UserRole.ROLE_REVIEWER);
        userRepository.saveAll(Arrays.asList(user1, user2, reviewer1));

        ReviewRequest reviewRequest1 = createReviewRequest(user1, reviewer1, "title1", "comment1", "Java");
        ReviewRequest reviewRequest2 = createReviewRequest(user1, reviewer1, "title2", "comment2", "Python");
        ReviewRequest reviewRequest3 = createReviewRequest(user1, reviewer1, "title3", "comment3", "Java");
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

    @DisplayName("2. 코드리뷰요청 상세조회")
    @Test
    void 코드리뷰요청_상세조회() {
        User user1 = userRepository.findByUsername("user1").get();
        User user2 = userRepository.findByUsername("user2").get();
        User reviewer1 = userRepository.findByUsername("reviewer1").get();
        ReviewRequest reviewRequest = reviewRequestRepository.findByTitle("title1").get(0);

        AddCommentDto addCommentDto1 = new AddCommentDto("댓글댓글1");
        AddCommentDto addCommentDto2 = new AddCommentDto("댓글댓글2");
        commentService.addComment_Question(user1, reviewRequest.getId(), addCommentDto1);
        commentService.addComment_Question(user2, reviewRequest.getId(), addCommentDto2);

        AddAnswerDto addAnswerDto = new AddAnswerDto("답변제목", "답변내용");
        reviewerService.addReviewAndComment(reviewer1, reviewRequest.getId(), addAnswerDto);

        ReviewRequestDetailResponseDto result = reviewRequestService.getReviewRequest(reviewRequest.getId());

        System.out.println(result);
    }




    private ReviewRequest createReviewRequest(User requestUser, User answerUser, String title, String content, String language) {
        return new ReviewRequest(
                requestUser, answerUser,
                title, content, ReviewRequestStatus.REQUESTED, language
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
