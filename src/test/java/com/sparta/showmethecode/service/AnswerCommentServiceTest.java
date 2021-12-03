package com.sparta.showmethecode.service;

import com.sparta.showmethecode.domain.*;
import com.sparta.showmethecode.dto.request.AddAnswerDto;
import com.sparta.showmethecode.repository.ReviewAnswerCommentRepository;
import com.sparta.showmethecode.repository.ReviewRequestCommentRepository;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import com.sparta.showmethecode.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;

@Transactional
@SpringBootTest
public class AnswerCommentServiceTest {

    @Autowired
    ReviewRequestRepository reviewRequestRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReviewRequestCommentRepository reviewRequestCommentRepository;
    @Autowired
    ReviewAnswerCommentRepository reviewAnswerCommentRepository;
    @Autowired
    ReviewerService reviewerService;
    @Autowired
    CommentService commentService;
    @Autowired
    EntityManager em;

    @BeforeEach
    void init() {
        User user1 = createUser("user1", "user1", UserRole.ROLE_USER);
        User reviewer1 = createUser("reviewer1", "reviewer1", UserRole.ROLE_REVIEWER);
        userRepository.saveAll(Arrays.asList(user1, reviewer1));

        ReviewRequest reviewRequest = createReviewRequest(user1, reviewer1, "리뷰제목",  "리뷰설명", "Java");
        reviewRequestRepository.save(reviewRequest);

        createReviewAnswer("답변제목", "답변내용", reviewer1, reviewRequest.getId());
    }


    private void createReviewAnswer(String title, String content, User answerUser, Long reviewId) {
        AddAnswerDto addAnswerDto = new AddAnswerDto(title, content);
        reviewerService.addReviewAndComment(answerUser, reviewId, addAnswerDto);
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
