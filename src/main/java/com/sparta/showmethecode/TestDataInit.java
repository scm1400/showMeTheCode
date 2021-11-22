package com.sparta.showmethecode;

import com.sparta.showmethecode.domain.ReviewRequest;
import com.sparta.showmethecode.domain.ReviewRequestStatus;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.domain.UserRole;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import com.sparta.showmethecode.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TestDataInit implements ApplicationRunner {

    private final UserRepository userRepository;
    private final ReviewRequestRepository reviewRequestRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User questionUser = createdNormalUser("questionUser", "test");
        userRepository.save(questionUser);
        User answerUser = createdNormalUser("answerUser", "test1");
        userRepository.save(answerUser);

        for (int i=0;i<5;i++) {
            ReviewRequest reviewRequest = createdReviewRequest("title" + i, "code" + i, "comment" + i, "JAVA", questionUser, answerUser);
            reviewRequestRepository.save(reviewRequest);
        }

    }

    private User createdNormalUser(String username, String password) {
        return new User(username, password, UserRole.ROLE_USER, 0, 0);
    }

    private ReviewRequest createdReviewRequest(String title, String code, String comment, String language, User requestUser, User answerUser) {
        return new ReviewRequest(requestUser, title, code, comment, ReviewRequestStatus.REQUESTED, language);
    }
}
