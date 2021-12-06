package com.sparta.showmethecode;

import com.sparta.showmethecode.domain.*;
import com.sparta.showmethecode.dto.response.PageResponseDto;
import com.sparta.showmethecode.dto.response.ReviewRequestResponseDto;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import com.sparta.showmethecode.repository.UserRepository;
import com.sparta.showmethecode.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Transactional
@SpringBootTest
public class MypageTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ReviewRequestRepository reviewRequestRepository;
    @Autowired
    UserService userService;

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

        List<ReviewRequest> all = reviewRequestRepository.findAll();
        for (ReviewRequest reviewRequest : all) {
            System.out.println(reviewRequest.getTitle());
            System.out.println(reviewRequest.getStatus().toString());
        }

        PageResponseDto<ReviewRequestResponseDto> result = userService.getMyReviewRequestList(user1, 0, 10, "createdAt", true, null);
        List<ReviewRequestResponseDto> data = result.getData();

        for (ReviewRequestResponseDto d : data) {
            System.out.println(d);
        }

    }
}
