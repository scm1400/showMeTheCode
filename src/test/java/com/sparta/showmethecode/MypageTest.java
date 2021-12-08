package com.sparta.showmethecode;

import com.sparta.showmethecode.domain.*;
import com.sparta.showmethecode.dto.request.AddAnswerDto;
import com.sparta.showmethecode.dto.request.AddCommentDto;
import com.sparta.showmethecode.dto.response.CommentResponseDto;
import com.sparta.showmethecode.dto.response.RequestAndAnswerResponseDto;
import com.sparta.showmethecode.dto.response.ReviewRequestDetailResponseDto;
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
    static ReviewRequest reviewRequest;
    @BeforeEach
    void init() {
        user = new User("user1", "test", UserRole.ROLE_USER, 0, 0, 0);
        reviewer = new User("reviewer", "test", UserRole.ROLE_REVIEWER, 0, 0, 0, Arrays.asList(new Language("JAVA")));
        userRepository.saveAll(Arrays.asList(user, reviewer));


        reviewRequest = new ReviewRequest(user, reviewer, "title", "content", ReviewRequestStatus.UNSOLVE, "JAVA");
        reviewRequestRepository.save(reviewRequest);

//        commentService.addComment_Question(user, reviewRequest.getId(), new AddCommentDto("content1"));
//        commentService.addComment_Question(user, reviewRequest.getId(), new AddCommentDto("content2"));
    }

    @Test
    void deleteTest() {

        reviewRequestService.deleteReviewRequest(reviewRequest.getId(), user);
        int size = reviewRequestRepository.findByTitle("title").size();
        System.out.println(size);

        ReviewRequestDetailResponseDto reviewRequest1 = reviewRequestService.getReviewRequest(MypageTest.reviewRequest.getId());
        List<CommentResponseDto> comments = reviewRequest1.getComments();
        for (CommentResponseDto comment : comments) {
            System.out.println(comment);
        }

        Assertions.assertEquals(0, size);


    }
}
