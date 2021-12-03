package com.sparta.showmethecode.service;

import com.sparta.showmethecode.domain.*;
import com.sparta.showmethecode.dto.request.AddCommentDto;
import com.sparta.showmethecode.dto.request.UpdateReviewDto;
import com.sparta.showmethecode.dto.response.CommentResponseDto;
import com.sparta.showmethecode.dto.response.ReviewRequestDetailResponseDto;
import com.sparta.showmethecode.repository.ReviewRequestCommentRepository;
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
import javax.xml.stream.events.Comment;
import java.util.Arrays;
import java.util.List;

@Transactional
@SpringBootTest
public class CommentServiceTest {

    @Autowired
    ReviewRequestRepository reviewRequestRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReviewRequestCommentRepository reviewRequestCommentRepository;
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
    }

    @Test
    @DisplayName("1. 댓글등록 테스트")
    void 댓글등록() {
        User user1 = userRepository.findByUsername("user1").get();
        ReviewRequest reviewRequest = reviewRequestRepository.findByTitle("리뷰제목").get(0);
        em.flush();
        em.clear();

        AddCommentDto dto = new AddCommentDto("댓글댓글");
        commentService.addComment(user1, reviewRequest.getId(), dto);


        ReviewRequestDetailResponseDto reviewRequestDetailWithComment = reviewRequestRepository.getReviewRequestDetailWithComment(reviewRequest.getId());

        List<CommentResponseDto> commentResponseDtoList = reviewRequestDetailWithComment.getCommentResponseDtoList();

        Assertions.assertEquals("댓글댓글", commentResponseDtoList.get(0).getContent());
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

    @Test
    @DisplayName("2. 댓글삭제 테스트")
    void 댓글삭제() {
        User user1 = userRepository.findByUsername("user1").get();
        ReviewRequest reviewRequest = reviewRequestRepository.findByTitle("리뷰제목").get(0);
        AddCommentDto dto = new AddCommentDto("댓글댓글");
        commentService.addComment(user1, reviewRequest.getId(), dto);

        ReviewRequestComment reviewRequestComment = reviewRequestCommentRepository.findAll().get(0);

        em.flush();
        em.clear();

        long row = commentService.removeComment(user1, reviewRequest.getId(), reviewRequestComment.getId());

        Assertions.assertEquals(1, row);
        Assertions.assertEquals(0, reviewRequestCommentRepository.findAll().size());
    }

    @Test
    @DisplayName("3. 댓글수정 테스트")
    void 댓글수정() {
        User user1 = userRepository.findByUsername("user1").get();
        ReviewRequest reviewRequest = reviewRequestRepository.findByTitle("리뷰제목").get(0);
        AddCommentDto dto = new AddCommentDto("댓글댓글");
        commentService.addComment(user1, reviewRequest.getId(), dto);

        ReviewRequestComment reviewRequestComment = reviewRequestCommentRepository.findAll().get(0);

        em.flush();
        em.clear();

        final String updateContent = "댓글수정수정수정";
        UpdateReviewDto updateReviewDto = new UpdateReviewDto(updateContent);
        commentService.updateComment(user1, reviewRequest.getId(), reviewRequestComment.getId(), updateReviewDto);

        Assertions.assertEquals(updateContent, reviewRequestCommentRepository.findById(reviewRequestComment.getId()).get().getContent());
    }
}
