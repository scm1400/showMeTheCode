package com.sparta.showmethecode;

import com.sparta.showmethecode.domain.*;
import com.sparta.showmethecode.dto.request.AddAnswerDto;
import com.sparta.showmethecode.dto.request.AddCommentDto;
import com.sparta.showmethecode.dto.response.*;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import com.sparta.showmethecode.repository.UserRepository;
import com.sparta.showmethecode.service.CommentService;
import com.sparta.showmethecode.service.ReviewRequestService;
import com.sparta.showmethecode.service.ReviewerService;
import com.sparta.showmethecode.service.UserService;
import org.junit.jupiter.api.Assertions;
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
    @Autowired
    ReviewerService reviewerService;
    @Autowired
    ReviewRequestService reviewRequestService;
    @Autowired
    CommentService commentService;


    static User user;
    static User reviewer;
    static ReviewRequest reviewRequest1;
    static ReviewRequest reviewRequest2;
    @BeforeEach
    void init() {
        user = new User("user1", "test", "nickname", UserRole.ROLE_USER, 0, 0, 0);
        reviewer = new User("reviewer", "test", "nickname1", UserRole.ROLE_REVIEWER, 0, 0, 0, Arrays.asList(new Language("JAVA")));
        userRepository.saveAll(Arrays.asList(user, reviewer));


        reviewRequest1 = new ReviewRequest(user, reviewer, "title", "content", ReviewRequestStatus.UNSOLVE, "JAVA");
        reviewRequestRepository.save(reviewRequest1);
        reviewRequest2 = new ReviewRequest(user, reviewer, "title2", "content2", ReviewRequestStatus.UNSOLVE, "JAVA");
        reviewRequestRepository.save(reviewRequest2);

    }

    @Test
    void test() {

        PageResponseDto<ReviewRequestResponseDto> result = reviewRequestService.searchRequestByLanguageName("JAVA", 0, 10, true);
        List<ReviewRequestResponseDto> data1 = result.getData();

        for (ReviewRequestResponseDto d : data1) {
            System.out.println(d.getTitle());
        }

        PageResponseDto<ReviewRequestResponseDto> result2 = reviewRequestService.searchRequestByLanguageName("java", 0, 10, true);
        List<ReviewRequestResponseDto> data2 = result2.getData();

        for (ReviewRequestResponseDto reviewRequestResponseDto : data2) {
            System.out.println(reviewRequestResponseDto);
        }

        PageResponseDto<ReviewRequestResponseDto> result3 = reviewRequestService.searchRequestByLanguageName("python", 0, 10, true);
        List<ReviewRequestResponseDto> data3 = result3.getData();

        Assertions.assertEquals(2, data1.size());
        Assertions.assertEquals(2, data2.size());
        Assertions.assertEquals(0, data3.size());



    }
}
