package com.sparta.showmethecode.service;

import com.sparta.showmethecode.domain.*;
import com.sparta.showmethecode.dto.request.AddAnswerDto;
import com.sparta.showmethecode.dto.request.UpdateAnswerDto;
import com.sparta.showmethecode.dto.response.PageResponseDto;
import com.sparta.showmethecode.dto.response.ReviewAnswerResponseDto;
import com.sparta.showmethecode.dto.response.ReviewerInfoDto;
import com.sparta.showmethecode.repository.ReviewAnswerRepository;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import com.sparta.showmethecode.repository.UserRepository;
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

@Transactional
@SpringBootTest
public class ReviewerServiceTest {

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

    @BeforeEach
    void init() {
        User user1 = new User("user1", "user1", UserRole.ROLE_USER, 0, 0, 0);
        User reviewer1 = new User("reviewer1", "reviewer1", UserRole.ROLE_REVIEWER, 0, 0, 0, Arrays.asList(new Language("Java")));
        userRepository.saveAll(Arrays.asList(user1, reviewer1));

        ReviewRequest reviewRequest1 = new ReviewRequest(user1, reviewer1, "title1"," comment1", ReviewRequestStatus.REQUESTED, "Java");
        ReviewRequest reviewRequest2 = new ReviewRequest(user1, reviewer1, "title2","comment2", ReviewRequestStatus.REQUESTED, "Java");
        ReviewRequest reviewRequest3 = new ReviewRequest(user1, reviewer1, "title3",  "comment3", ReviewRequestStatus.REQUESTED, "Java");
        reviewRequestRepository.saveAll(Arrays.asList(reviewRequest1, reviewRequest2, reviewRequest3));
    }

    @DisplayName("1. 나에게 요청된 리뷰에 리뷰하기")
    @Test
    void 요청리뷰_답변() {
        User reviewer = userRepository.findByUsername("reviewer1").get();
        ReviewRequest reviewRequest = reviewRequestRepository.findByTitle("title1").get(0);
        em.flush();
        em.clear();

        final String title = "답변제목";
        final String content = "답변코멘트";
        AddAnswerDto addAnswerDto = new AddAnswerDto("답변제목",  "답변코멘트");

        reviewerService.addReviewAndComment(reviewer, reviewRequest.getId(), addAnswerDto);
        ReviewRequest reviewRequest1 = reviewRequestRepository.findByTitle("title1").get(0);
        ReviewAnswer reviewAnswer = reviewRequest1.getReviewAnswer();

        Assertions.assertEquals(title, reviewAnswer.getTitle());
        Assertions.assertEquals(content, reviewAnswer.getContent());

//        System.out.println("=========================================");
//        System.out.println(reviewAnswer.getTitle());
//        System.out.println(reviewAnswer.getComment());
//        System.out.println(reviewAnswer.getAnswerUser().getUsername());
//        System.out.println("=========================================");
//        ReviewRequest reviewRequest1 = reviewRequestRepository.findAll().get(0);
//        System.out.println(reviewRequest1.getTitle());
//        System.out.println(reviewRequest1.getAnswerUser().getUsername());
//        System.out.println(reviewRequest1.getReviewAnswer().getTitle());
    }

    @DisplayName("2. 나에게 요청된 리뷰에 거절하기")
    @Test
    void 리뷰거절() {
        User reviewer = userRepository.findByUsername("reviewer1").get();
        ReviewRequest reviewRequest = reviewRequestRepository.findByTitle("title1").get(0);
        em.flush();
        em.clear();

        reviewerService.rejectRequestedReview(reviewer, reviewRequest.getId());

        ReviewRequest reviewRequest1 = reviewRequestRepository.findById(reviewRequest.getId()).get();

        Assertions.assertEquals(ReviewRequestStatus.REJECTED, reviewRequest1.getStatus());
    }

    @Test
    @DisplayName("3. 리뷰어 랭킹 조회 테스트")
    void 리뷰어_랭킹() {
        User reviewer1 = createReviewer("test1", "test1", 30, 30, 4.5);
        User reviewer2 = createReviewer("test2", "test1", 30, 30, 4.3);
        User reviewer3 = createReviewer("test3", "test1", 30, 30, 4.8);
        User reviewer4 = createReviewer("test4", "test1", 30, 30, 4.9);
        User reviewer5 = createReviewer("test5", "test1", 30, 30, 3.0);
        User reviewer6 = createReviewer("test6", "test1", 30, 30, 2.0);
        User reviewer7 = createReviewer("test7", "test1", 30, 30, 3.8);

        userRepository.saveAll(Arrays.asList(reviewer1, reviewer2, reviewer3, reviewer4, reviewer5, reviewer6, reviewer7));

        PageResponseDto<ReviewerInfoDto> reviewerRanking = reviewerService.getReviewerRanking(0, 10, false);

        System.out.println("===============내림차순================");
        reviewerRanking.getData().forEach(System.out::println);
        System.out.println("======================================");

        reviewerRanking = reviewerService.getReviewerRanking(0, 10, true);
        System.out.println("===============오름차순================");
        reviewerRanking.getData().forEach(System.out::println);
        System.out.println("======================================");
    }

    @Test
    @DisplayName("4. 리뷰어 랭킹 조회 테스트 (상위 5명 조회)")
    void 리뷰어_랭킹_상위5명() {
        User reviewer1 = createReviewer("test1", "test1", 30, 30, 4.5);
        User reviewer2 = createReviewer("test2", "test1", 30, 30, 4.3);
        User reviewer3 = createReviewer("test3", "test1", 30, 30, 4.8);
        User reviewer4 = createReviewer("test4", "test1", 30, 30, 4.9);
        User reviewer5 = createReviewer("test5", "test1", 30, 30, 3.0);
        User reviewer6 = createReviewer("test6", "test1", 30, 30, 2.0);
        User reviewer7 = createReviewer("test7", "test1", 30, 30, 3.8);

        userRepository.saveAll(Arrays.asList(reviewer1, reviewer2, reviewer3, reviewer4, reviewer5, reviewer6, reviewer7));

        List<ReviewerInfoDto> reviewerRanking = reviewerService.getReviewerTop5Ranking();

        System.out.println(reviewerRanking.get(0));
        System.out.println(reviewerRanking.get(reviewerRanking.size()-1));

        Assertions.assertEquals(5, reviewerRanking.size());
        Assertions.assertEquals("test4", reviewerRanking.get(0).getUsername());
        Assertions.assertEquals("test7", reviewerRanking.get(4).getUsername());
    }

    @DisplayName("5. 내가 답변한 리뷰 조회하기")
    @Test
    void 답변한_리뷰_조회() {
        List<ReviewRequest> requests = reviewRequestRepository.findAll();
        User user1 = userRepository.findByUsername("user1").get();
        User reviewer1 = userRepository.findByUsername("reviewer1").get();

        em.flush();
        em.clear();

        for (ReviewRequest request : requests) {
            AddAnswerDto addAnswerDto = new AddAnswerDto("답변제목", "답변설명");
            reviewerService.addReviewAndComment(reviewer1, request.getId(), addAnswerDto);
        }
        List<ReviewAnswer> answers = reviewAnswerRepository.findAll();
        for (int i=0;i<answers.size();i++) {
            answers.get(i).evaluate(3 + i);
        }

        System.out.println("================================================");

        final String sortBy = "createdAt";
//        final String sortBy = "point";
        PageResponseDto<ReviewAnswerResponseDto> result = reviewerService.getMyAnswerList(reviewer1, 0, 10, false, sortBy);
        List<ReviewAnswerResponseDto> data = result.getData();
        data.forEach(System.out::println);
    }

    private User createReviewer(String username, String password, int answerCount, int evalCount, double avg) {
        return User.builder()
                .username(username)
                .password(password)
                .answerCount(10)
                .evalCount(30)
                .evalTotal(evalCount*avg)
                .role(UserRole.ROLE_REVIEWER)
                .languages(Arrays.asList(new Language("Java"))).build();
    }

    @Test
    @DisplayName("6. 답변 수정하기")
    void 답변_수정하기() {
        User user1 = userRepository.findByUsername("user1").get();
        User reviewer1 = userRepository.findByUsername("reviewer1").get();
        ReviewRequest reviewRequest = reviewRequestRepository.findAll().get(0);

        em.flush();
        em.clear();

        AddAnswerDto addAnswerDto = new AddAnswerDto("답변제목",  "답변설명");
        reviewerService.addReviewAndComment(reviewer1, reviewRequest.getId(), addAnswerDto);

        List<ReviewAnswerResponseDto> answerList = reviewerService.getMyAnswerList(reviewer1, 0, 10, true, "createdAt").getData();

        UpdateAnswerDto updateAnswerDto = new UpdateAnswerDto("답변제목수정", "답변설명수정");
        reviewerService.updateAnswer(reviewer1, answerList.get(0).getReviewAnswerId(), updateAnswerDto);

        em.flush();
        em.clear();

        answerList = reviewerService.getMyAnswerList(reviewer1, 0, 10, true, "createdAt").getData();
        System.out.println(answerList.get(0));

        Assertions.assertEquals("답변제목수정", answerList.get(0).getAnswerTitle());

    }
}
