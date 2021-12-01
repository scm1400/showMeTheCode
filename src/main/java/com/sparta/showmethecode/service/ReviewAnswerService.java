package com.sparta.showmethecode.service;

import com.sparta.showmethecode.domain.ReviewAnswer;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.dto.request.ReviewAnswerDto;
import com.sparta.showmethecode.dto.request.ReviewRequestDto;
import com.sparta.showmethecode.repository.ReviewAnswerRepository;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import com.sparta.showmethecode.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReviewAnswerService {

    private final ReviewRequestRepository reviewRequestRepository;
    private final ReviewAnswerRepository reviewAnswerRepository;
    private final UserRepository userRepository;

//    /**
//     * 나에게 요청된 리뷰목록 조회
//     */
//    @Transactional(readOnly = true)
//    public ReviewRequestDto getReceivedReviewRequest(Long id) {
//        ReviewRequestDto reviewRequestDto = (ReviewRequestDto) reviewRequestRepository.findByAnswerUser(id);
//        return reviewRequestDto;
//    }
//
//    /**
//     * 나에게 요청된 리뷰에 리뷰 작성
//     */
//    public void addReviewAnswer(ReviewAnswerDto answerDto, User user) {
//        ReviewAnswer reviewAnswer
//                = new ReviewAnswer(user, answerDto.getTitle(), answerDto.getCode(), answerDto.getComment());
//
//        reviewAnswerRepository.save(reviewAnswer);
//    }
}
