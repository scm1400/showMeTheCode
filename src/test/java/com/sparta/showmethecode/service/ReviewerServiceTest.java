package com.sparta.showmethecode.service;

import com.sparta.showmethecode.domain.*;
import com.sparta.showmethecode.dto.request.AddReviewDto;
import com.sparta.showmethecode.repository.ReviewAnswerRepository;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import com.sparta.showmethecode.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;

@Transactional
@SpringBootTest
public class ReviewerServiceTest {

    @Autowired
    ReviewerService reviewerService;
    @Autowired
    ReviewRequestRepository reviewRequestRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;
    @Autowired
    ReviewAnswerRepository reviewAnswerRepository;

    @BeforeEach
    void init() {
        User user1 = new User("user1", "user1", UserRole.ROLE_USER, 0, 0);
        User reviewer1 = new User("reviewer1", "reviewer1", UserRole.ROLE_REVIEWER, 0, 0, Arrays.asList(new Language("Java")));
        userRepository.saveAll(Arrays.asList(user1, reviewer1));

        ReviewRequest reviewRequest = new ReviewRequest(user1, reviewer1, "title1", "code1", "comment1", ReviewRequestStatus.REQUESTED, "Java");
        reviewRequestRepository.save(reviewRequest);
    }

    @DisplayName("1. 나에게 요청된 리뷰에 리뷰하기")
    @Test
    void 요청리뷰_답변() {
        User reviewer = userRepository.findByUsername("reviewer1").get();
        ReviewRequest reviewRequest = reviewRequestRepository.findByTitle("title1").get(0);
        em.flush();
        em.clear();

        final String title = "답변제목";
        final String code = "답변코드";
        final String comment = "답변코멘트";
        AddReviewDto addReviewDto = new AddReviewDto("답변제목", "답변코드", "답변코멘트");

        reviewerService.addReviewAndComment(reviewer, reviewRequest.getId(), addReviewDto);
        ReviewRequest reviewRequest1 = reviewRequestRepository.findByTitle("title1").get(0);
        ReviewAnswer reviewAnswer = reviewRequest1.getReviewAnswer();

        Assertions.assertEquals(title, reviewAnswer.getTitle());
        Assertions.assertEquals(code, reviewAnswer.getCode());
        Assertions.assertEquals(comment, reviewAnswer.getComment());

//        System.out.println("=========================================");
//        System.out.println(reviewAnswer.getTitle());
//        System.out.println(reviewAnswer.getComment());
//        System.out.println(reviewAnswer.getAnswerUser().getUsername());
//        System.out.println("=========================================");
//        ReviewRequest reviewRequest1 = reviewRequestRepository.findAll().get(0);
//        System.out.println(reviewRequest1.getTitle());
//        System.out.println(reviewRequest1.getAnswerUser().getUsername());
//        System.out.println(reviewRequest1.getReviewAnswer().getTitle());
    }

    @DisplayName("2. 나에게 요청된 리뷰에 거절하기")
    @Test
    void 리뷰거절() {
        User reviewer = userRepository.findByUsername("reviewer1").get();
        ReviewRequest reviewRequest = reviewRequestRepository.findByTitle("title1").get(0);
        em.flush();
        em.clear();

        reviewerService.rejectRequestedReview(reviewer, reviewRequest.getId());

        ReviewRequest reviewRequest1 = reviewRequestRepository.findById(reviewRequest.getId()).get();

        Assertions.assertEquals(ReviewRequestStatus.REJECTED, reviewRequest1.getStatus());
    }
}
