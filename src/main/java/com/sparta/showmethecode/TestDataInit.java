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
//@Component
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

        ReviewRequestComment reviewRequestComment = addRequestComment("content1", savedUser2);
        ReviewRequestComment reviewRequestComment1 = addRequestComment("content2", savedUser2);
        ReviewRequestComment reviewRequestComment2 = addRequestComment("content3", savedQuestionUser2);
        ReviewRequestComment reviewRequestComment3 = addRequestComment("content4", savedQuestionUser2);

        reviewRequest.addComment(reviewRequestComment);
        reviewRequest.addComment(reviewRequestComment1);
        reviewRequest.addComment(reviewRequestComment2);
        reviewRequest.addComment(reviewRequestComment3);

        ReviewRequest savedReviewRequest = reviewRequestRepository.save(reviewRequest);


    }

    private User createdNormalUser(String username, String password) {
        return new User(username, password, UserRole.ROLE_USER, 0, 0);
    }

    private ReviewRequest createdReviewRequest(String title, String code, String comment, String language, User requestUser, User answerUser) {
        return new ReviewRequest(requestUser, title, code, comment, ReviewRequestStatus.REQUESTED, language);
    }

    private ReviewRequestComment addRequestComment(String content, User user) {
        return new ReviewRequestComment(content, user);
    }
}
