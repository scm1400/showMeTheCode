package com.sparta.showmethecode.service;

import com.sparta.showmethecode.domain.*;
import com.sparta.showmethecode.dto.request.AddReviewDto;
import com.sparta.showmethecode.dto.request.EvaluateAnswerDto;
import com.sparta.showmethecode.repository.ReviewAnswerRepository;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import com.sparta.showmethecode.repository.UserRepository;
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
public class UserServiceTest {

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
    @Autowired
    UserService userService;

    @BeforeEach
    void init() {
        User user1 = new User("user1", "user1", UserRole.ROLE_USER, 0, 0, 0);
        User reviewer1 = new User("reviewer1", "reviewer1", UserRole.ROLE_REVIEWER, 0, 0, 0, Arrays.asList(new Language("Java")));
        userRepository.saveAll(Arrays.asList(user1, reviewer1));

        ReviewRequest reviewRequest = new ReviewRequest(user1, reviewer1, "title1", "code1", "comment1", ReviewRequestStatus.REQUESTED, "Java");
        reviewRequestRepository.save(reviewRequest);
    }

    @DisplayName("1. 평가하기")
    @Test
    void 답변_평가하기() {
        User reviewer1 = userRepository.findByUsername("reviewer1").get();
        ReviewRequest reviewRequest = reviewRequestRepository.findByTitle("title1").get(0);
        AddReviewDto addReviewDto = new AddReviewDto("답변제목", "답변코드", "답변설명");
        reviewerService.addReviewAndComment(reviewer1, reviewRequest.getId(), addReviewDto);

        em.flush();
        em.clear();

        ReviewAnswer reviewAnswer = reviewAnswerRepository.findAll().get(0);

        System.out.println("==================평가전================");
        System.out.println(reviewAnswer.getTitle());
        System.out.println(reviewAnswer.getComment());
        System.out.println(reviewAnswer.getComment());
        System.out.println(reviewAnswer.getAnswerUser().getUsername());
        System.out.println(reviewAnswer.getPoint());
        System.out.println("=======================================");

//        em.flush();
//        em.clear();

        User user1 = userRepository.findByUsername("user1").get();
        EvaluateAnswerDto evaluateAnswerDto = new EvaluateAnswerDto(4.5);
        userService.evaluateAnswer(user1, reviewAnswer.getId(), evaluateAnswerDto);

        System.out.println("==================평가후================");
        System.out.println(reviewAnswer.getTitle());
        System.out.println(reviewAnswer.getComment());
        System.out.println(reviewAnswer.getComment());
        System.out.println(reviewAnswer.getAnswerUser().getUsername());
        System.out.println(reviewAnswer.getPoint());
        System.out.println("=======================================");
    }
}
