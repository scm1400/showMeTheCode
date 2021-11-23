package com.sparta.showmethecode;

import com.sparta.showmethecode.domain.*;
import com.sparta.showmethecode.repository.ReviewRequestCommentRepository;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import com.sparta.showmethecode.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class TestDataInit implements ApplicationRunner {

    private final UserRepository userRepository;
    private final ReviewRequestRepository reviewRequestRepository;
    private final ReviewRequestCommentRepository reviewRequestCommentRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User questionUser1 = createdNormalUser("questionUser1", "test");
        User savedQuestionUser1 = userRepository.save(questionUser1);
        User questionUser2 = createdNormalUser("questionUser2", "test");
        User savedQuestionUser2 = userRepository.save(questionUser2);

        User answerUser = createdNormalUser("answerUser", "test1");
        User savedUser2 = userRepository.save(answerUser);

        ReviewRequest reviewRequest = createdReviewRequest("title1", "code1", "comment1", "Java", savedQuestionUser1, savedUser2);
        ReviewRequest savedReviewRequest = reviewRequestRepository.save(reviewRequest);

        ReviewRequestComment reviewRequestComment = addRequestComment("content1", savedReviewRequest, savedUser2);
        ReviewRequestComment reviewRequestComment1 = addRequestComment("content2", savedReviewRequest, savedUser2);
        ReviewRequestComment reviewRequestComment2 = addRequestComment("content3", savedReviewRequest, savedQuestionUser2);
        ReviewRequestComment reviewRequestComment3 = addRequestComment("content4", savedReviewRequest, savedQuestionUser2);

        reviewRequestCommentRepository.saveAll(Arrays.asList(reviewRequestComment, reviewRequestComment1, reviewRequestComment2, reviewRequestComment3));
    }

    private User createdNormalUser(String username, String password) {
        return new User(username, password, UserRole.ROLE_USER, 0, 0);
    }

    private ReviewRequest createdReviewRequest(String title, String code, String comment, String language, User requestUser, User answerUser) {
        return new ReviewRequest(requestUser, title, code, comment, ReviewRequestStatus.REQUESTED, language);
    }

    private ReviewRequestComment addRequestComment(String content, ReviewRequest reviewRequest, User user) {
        return new ReviewRequestComment(content, user, reviewRequest);
    }
}
