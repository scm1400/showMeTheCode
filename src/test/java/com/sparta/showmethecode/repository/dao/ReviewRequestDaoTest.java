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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ReviewRequestDaoTest {

    @Autowired
    ReviewRequestRepository reviewRequestRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void init() {
        User user = new User("user1", "pass1", UserRole.ROLE_USER, 0, 0);
        userRepository.save(user);

        ReviewRequest reviewRequest1 = new ReviewRequest(user, "Java가 여려워요.", "code1", "java도 어려운데 jpa는 ㅠ", ReviewRequestStatus.REQUESTED, "JAVA");
        ReviewRequest reviewRequest2 = new ReviewRequest(user, "spring이 이상해요", "code2", "comment2", ReviewRequestStatus.REQUESTED, "JAVA");
        ReviewRequest reviewRequest3 = new ReviewRequest(user, "jpa가 이상해요", "code3", "spring에서 jpa가 이상해요", ReviewRequestStatus.REQUESTED, "PYTHON");

        reviewRequestRepository.saveAll(Arrays.asList(reviewRequest1, reviewRequest2, reviewRequest3));

        for (int i=0;i<11;i++) {
            ReviewRequest reviewRequest = new ReviewRequest(user, "jpa가 이상해요", "code3", "spring에서 jpa가 이상해요", ReviewRequestStatus.REQUESTED, "PYTHON");
            reviewRequestRepository.save(reviewRequest);
        }
    }

    @Test
    void 검색_테스트() {
        final String keyword = "jpa";
        final int page = 1;
        final int size = 6;

        Pageable pageable = PageRequest.of(page, size);

        ReviewRequestListResponseDto results = reviewRequestRepository.findSearchByTitleOrComment(keyword, pageable);
        List<ReviewRequestResponseDto> list = results.getList();

        list.forEach(l -> System.out.println(l.getTitle() + ": " + l.getComment()));
        System.out.println("totalPage  = " + results.getTotalPage());
        System.out.println("totalElements = " + results.getTotalElements());
        System.out.println("page = " + results.getPage());
        System.out.println("size = " + results.getSize());

    }
}
