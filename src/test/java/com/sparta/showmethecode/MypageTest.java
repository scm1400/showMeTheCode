package com.sparta.showmethecode;

import com.sparta.showmethecode.domain.*;
import com.sparta.showmethecode.dto.request.AddAnswerDto;
import com.sparta.showmethecode.dto.response.RequestAndAnswerResponseDto;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import com.sparta.showmethecode.repository.UserRepository;
import com.sparta.showmethecode.service.ReviewRequestService;
import com.sparta.showmethecode.service.ReviewerService;
import com.sparta.showmethecode.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Transactional
@SpringBootTest
public class MypageTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ReviewRequestRepository reviewRequestRepository;
    @Autowired
    UserService userService;
    @Autowired
    ReviewerService reviewerService;
    @Autowired
    ReviewRequestService reviewRequestService;

    @BeforeEach
    void init() {
        User user = new User("user", "test1", UserRole.ROLE_USER, 0, 0, 0.0);
        User reviewer = new User("reviewer1", "test1", UserRole.ROLE_REVIEWER,  0, 0, 0, Arrays.asList(new Language("JAVA")));
        userRepository.saveAll(Arrays.asList(user, reviewer));

        ReviewRequest reviewRequest = new ReviewRequest(user, reviewer, "title", "content", ReviewRequestStatus.UNSOLVE, "JAVA");
        reviewRequestRepository.save(reviewRequest);
    }

    @Test
    void test() {
        User user1 = userRepository.findByUsername("user").get();
        User reviewer1 = userRepository.findByUsername("reviewer1").get();

        ReviewRequest reviewRequest = reviewRequestRepository.findByTitle("title").get(0);
        reviewerService.addAnswer(reviewer1, reviewRequest.getId(), new AddAnswerDto("답변입니다."));

        RequestAndAnswerResponseDto result = reviewRequestService.getReviewRequestWithAnswer(reviewRequest.getId());

        System.out.println(result);
    }
}
