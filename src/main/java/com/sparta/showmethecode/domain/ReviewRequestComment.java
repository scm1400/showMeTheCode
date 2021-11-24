package com.sparta.showmethecode.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 리뷰 요청서에 달리는 댓글
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class ReviewRequestComment extends Timestamped{

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "review_request_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewRequest reviewRequest;

    public ReviewRequestComment(String content, User user, ReviewRequest reviewRequest) {
        this.content = content;
        this.user = user;
        this.reviewRequest = reviewRequest;
    }
}
