package com.sparta.showmethecode.repository.dao;

import com.sparta.showmethecode.domain.ReviewRequest;
import com.sparta.showmethecode.domain.ReviewRequestStatus;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.domain.UserRole;
import com.sparta.showmethecode.dto.response.ReviewRequestListResponseDto;
import com.sparta.showmethecode.dto.response.ReviewRequestResponseDto;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import com.sparta.showmethecode.repository.UserRepository;
import jdk.jfr.BooleanFlag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ReviewRequestDaoTest {

    @Autowired
    ReviewRequestRepository reviewRequestRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void init() throws InterruptedException {
        User user = new User("user1", "pass1", UserRole.ROLE_USER, 0, 0);
        userRepository.save(user);

        ReviewRequest reviewRequest1 = new ReviewRequest(user, "Java가 여려워요.", "code1", "java도 어려운데 jpa는 ㅠ", ReviewRequestStatus.REQUESTED, "JAVA");
        ReviewRequest reviewRequest2 = new ReviewRequest(user, "spring이 이상해요", "code2", "comment2", ReviewRequestStatus.REQUESTED, "JAVA");
        ReviewRequest reviewRequest3 = new ReviewRequest(user, "jpa가 이상해요", "code3", "spring에서 jpa가 이상해요", ReviewRequestStatus.REQUESTED, "PYTHON");

        reviewRequestRepository.saveAll(Arrays.asList(reviewRequest1, reviewRequest2, reviewRequest3));

        for (int i=0;i<11;i++) {
            Thread.sleep(500);
            ReviewRequest reviewRequest = new ReviewRequest(user, "jpa가 이상해요"+i, "code3", "spring에서 jpa가 이상해요", ReviewRequestStatus.REQUESTED, "PYTHON");
            reviewRequestRepository.save(reviewRequest);
        }
    }

    @Test
    void 검색_테스트1() {
        final String keyword = "jpa";
        final int page = 0;
        final int size = 12;

        Pageable pageable = makePageable(page, size, "createdAt", false);

        Page<ReviewRequestResponseDto> results = reviewRequestRepository.findSearchByTitleOrComment(keyword, pageable);
        List<ReviewRequestResponseDto> list = results.getContent();

        list.forEach(l -> System.out.println(l.getTitle() + ": " + l.getComment() + ": " + l.getCreatedAt()));
        System.out.println("totalPage  = " + results.getTotalPages());
        System.out.println("totalElements = " + results.getTotalElements());
        System.out.println("page = " + results.getNumber());
        System.out.println("size = " + results.getSize());

    }

    @Test
    void 검색_테스트2() {
        final String keyword = "jpa";
        final int page = 0;
        final int size = 12;

        Pageable pageable = makePageable(page, size, "createdAt", false);

        Page<ReviewRequestResponseDto> results = reviewRequestRepository.findSearchByTitleOrCommentAdvanced(keyword, pageable);
        List<ReviewRequestResponseDto> list = results.getContent();

        list.forEach(l -> System.out.println(l.getTitle() + ": " + l.getComment() + ": " + l.getCreatedAt()));
        System.out.println("totalPage  = " + results.getTotalPages());
        System.out.println("totalElements = " + results.getTotalElements());
        System.out.println("page = " + results.getNumber());
        System.out.println("size = " + results.getSize());
    }

    private Pageable makePageable(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        return PageRequest.of(page, size, sort);
    }
}
