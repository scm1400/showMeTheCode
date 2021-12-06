package com.sparta.showmethecode.repository;

import com.sparta.showmethecode.domain.*;
import com.sparta.showmethecode.dto.request.ReviewRequestUpdateDto;
import com.sparta.showmethecode.service.ReviewRequestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
public class ReviewRequestRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ReviewRequestRepository reviewRequestRepository;
    @Autowired
    ReviewRequestService reviewRequestService;
    @Autowired
    EntityManager em;

    @BeforeEach
    void init() throws InterruptedException {
        User user = new User("user1", "pass1", UserRole.ROLE_USER, 0, 0, 0);
        userRepository.save(user);

        User reviewer1 = new User("user2", "pass2", UserRole.ROLE_REVIEWER, 0, 0, 0, Arrays.asList(new Language("JAVA")));
        userRepository.save(reviewer1);
        User reviewer2 = new User("user3", "pass3", UserRole.ROLE_REVIEWER, 0, 0, 0, Arrays.asList(new Language("JAVA")));
        userRepository.save(reviewer2);

        ReviewRequest reviewRequest1 = new ReviewRequest(user, reviewer1,"Java가 여려워요.", "java도 어려운데 jpa는 ㅠ", ReviewRequestStatus.REQUESTED, "JAVA");
        reviewRequestRepository.save(reviewRequest1);
        ReviewRequest reviewRequest2 = new ReviewRequest(user, reviewer1,"Java가 여려워요!!", "java도 어려운데 jpa는 ㅠ", ReviewRequestStatus.REQUESTED, "JAVA");
        reviewRequestRepository.save(reviewRequest2);
    }

    @DisplayName("리뷰요청 수정 테스트")
    @Test
    void 리뷰요청_수정_테스트1() {
        User requestUser = userRepository.findByUsername("user1").get();
        ReviewRequest reviewRequest = reviewRequestRepository.findByTitle("Java가 여려워요.").get(0);

        final String updateTitle = "제목수정";
        final String updateContent = "설명수정";

        ReviewRequestUpdateDto reviewRequestUpdateDto = ReviewRequestUpdateDto.builder()
                .title(updateTitle)
                .content(updateContent).build();

        reviewRequestService.updateReviewRequest(reviewRequestUpdateDto, reviewRequest.getId(), requestUser);

        Assertions.assertEquals(updateTitle, reviewRequest.getTitle());
        Assertions.assertEquals(updateContent, reviewRequest.getContent());
    }

    @DisplayName("리뷰요청 수정 테스트 (리뷰어 수정)")
    @Test
    void 리뷰요청_수정_테스트2() {
        User requestUser = userRepository.findByUsername("user1").get();
        User newReviewer = userRepository.findByUsername("user3").get();
        ReviewRequest reviewRequest = reviewRequestRepository.findByTitle("Java가 여려워요.").get(0);

        em.flush();
        em.clear();

        final String updateTitle = "제목수정";
        final String updateContent = "설명수정";

        reviewRequest = reviewRequestRepository.findById(reviewRequest.getId()).get();

        ReviewRequestUpdateDto reviewRequestUpdateDto = ReviewRequestUpdateDto.builder()
                .title(updateTitle)
                .content(updateContent)
                .reviewerId(newReviewer.getId())
                .build();

        reviewRequestService.updateReviewRequest(reviewRequestUpdateDto, reviewRequest.getId(), requestUser);

        Assertions.assertEquals(updateTitle, reviewRequest.getTitle());
        Assertions.assertEquals(updateContent, reviewRequest.getContent());
        Assertions.assertEquals(newReviewer.getId(), reviewRequest.getAnswerUser().getId());
    }

    @DisplayName("리뷰요청 삭제 테스트")
    @Test
    void 삭제_테스트() {
        User user = userRepository.findByUsername("user1").get();
        ReviewRequest reviewRequest = reviewRequestRepository.findByTitle("Java가 여려워요.").get(0);

        reviewRequestService.deleteReviewRequest(reviewRequest.getId(), user);

        List<ReviewRequest> requests = reviewRequestRepository.findAll();

        Assertions.assertEquals(1, requests.size());
    }
}
