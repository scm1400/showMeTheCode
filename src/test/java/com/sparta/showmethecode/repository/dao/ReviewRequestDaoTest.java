package com.sparta.showmethecode.repository.dao;

import com.sparta.showmethecode.domain.ReviewRequest;
import com.sparta.showmethecode.domain.ReviewRequestStatus;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.domain.UserRole;
import com.sparta.showmethecode.dto.response.ReviewRequestLanguageCount;
import com.sparta.showmethecode.dto.response.ReviewRequestResponseDto;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import com.sparta.showmethecode.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource(locations = "/application-test.yml")
@SpringBootTest
@Transactional
public class ReviewRequestDaoTest {

    @Autowired
    ReviewRequestRepository reviewRequestRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void init() throws InterruptedException {
        User user = new User("user1", "pass1", UserRole.ROLE_USER, 0, 0, 0);
        User savedUser = userRepository.save(user);

        User user2 = new User("user2", "pass2", UserRole.ROLE_USER, 0, 0, 0);
        User savedUser2 = userRepository.save(user2);

        ReviewRequest reviewRequest1 = new ReviewRequest(savedUser, "Java가 여려워요.", "code1", "java도 어려운데 jpa는 ㅠ", ReviewRequestStatus.REQUESTED, "JAVA");
        ReviewRequest reviewRequest2 = new ReviewRequest(savedUser, "spring이 이상해요", "code2", "comment2", ReviewRequestStatus.REQUESTED, "JAVA");
        ReviewRequest reviewRequest3 = new ReviewRequest(savedUser, "jpa가 이상해요", "code3", "spring에서 jpa가 이상해요", ReviewRequestStatus.REQUESTED, "PYTHON");

        ReviewRequest reviewRequest4 = new ReviewRequest(savedUser2, "jpa가 이상해요", "code3", "spring에서 jpa가 이상해요", ReviewRequestStatus.REQUESTED, "PYTHON");

        reviewRequestRepository.saveAll(Arrays.asList(reviewRequest1, reviewRequest2, reviewRequest3, reviewRequest4));
    }

    @Test
    void 검색_테스트1() {
        final String keyword = "jpa";
        final int page = 0;
        final int size = 12;

        Pageable pageable = makePageable(page, size, "createdAt", false);

        Page<ReviewRequestResponseDto> results = reviewRequestRepository.findSearchByTitleOrCommentAdvanced(keyword, pageable, false);
        List<ReviewRequestResponseDto> list = results.getContent();

        list.forEach(l -> System.out.println(l.getTitle() + ": " + l.getComment() + ": " + l.getCreatedAt()));
        System.out.println("totalPage  = " + results.getTotalPages());
        System.out.println("totalElements = " + results.getTotalElements());
        System.out.println("page = " + results.getNumber());
        System.out.println("size = " + results.getSize());

    }

    @Test
    void 검색_테스트_정렬() {
        final String keyword = "jpa";
        final int page = 0;
        final int size = 12;

        Pageable pageable = makePageable(page, size, "createdAt", false);

        Page<ReviewRequestResponseDto> results = reviewRequestRepository.findSearchByTitleOrCommentAdvanced(keyword, pageable, true);
        List<ReviewRequestResponseDto> list = results.getContent();

        list.forEach(l -> System.out.println(l.getTitle() + ": " + l.getComment() + ": " + l.getCreatedAt()));
        System.out.println("totalPage  = " + results.getTotalPages());
        System.out.println("totalElements = " + results.getTotalElements());
        System.out.println("page = " + results.getNumber());
        System.out.println("size = " + results.getSize());
    }

    @Test
    void 리뷰요청_언어별_카운팅() {
        List<ReviewRequestLanguageCount> result = reviewRequestRepository.getReviewRequestLanguageCountGroupByLanguage();

        result.forEach(System.out::println);
    }

    private Pageable makePageable(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        return PageRequest.of(page, size, sort);
    }

    @Test
    void 자신이_요청한_리뷰목록_조회() {
        User user = userRepository.findByUsername("user1").get();
        List<ReviewRequest> all = reviewRequestRepository.findAll();

        List<ReviewRequestResponseDto> result = reviewRequestRepository.findMyReviewRequestList(user.getId());

        result.forEach(System.out::println);

        assertEquals(all.size()-1, result.size());
    }
}
