package com.sparta.showmethecode.repository.querydsltest;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.showmethecode.domain.*;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import com.sparta.showmethecode.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static com.sparta.showmethecode.domain.QReviewRequest.reviewRequest;

@SpringBootTest
@Transactional
public class GroupbyTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRequestRepository reviewRequestRepository;
    @Autowired
    JPAQueryFactory query;

    @BeforeEach
    void init() {
        User user = new User("user1", "pass1", UserRole.ROLE_USER, 0, 0);
        userRepository.save(user);

        ReviewRequest reviewRequest1 = new ReviewRequest(user, "Java가 여려워요.", "code1", "java도 어려운데 jpa는 ㅠ", ReviewRequestStatus.REQUESTED, "Java");
        ReviewRequest reviewRequest2 = new ReviewRequest(user, "spring이 이상해요", "code2", "comment2", ReviewRequestStatus.REQUESTED, "JAVA");
        ReviewRequest reviewRequest3 = new ReviewRequest(user, "jpa가 이상해요", "code3", "spring에서 jpa가 이상해요", ReviewRequestStatus.REQUESTED, "PYTHON");

        reviewRequestRepository.saveAll(Arrays.asList(reviewRequest1, reviewRequest2, reviewRequest3));
    }

    @Test
    void 그룹화_카운팅_테스트() {
        List<ReviewRequest> reviews = reviewRequestRepository.findAll();
        reviews.forEach(r -> System.out.println(r.getLanguageName()));

        System.out.println("size = " + reviews.size());

        List<Tuple> result = query.select(reviewRequest.languageName, reviewRequest.id.count())
                .from(reviewRequest)
                .groupBy(reviewRequest.languageName)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println(tuple);
        }
    }
}
