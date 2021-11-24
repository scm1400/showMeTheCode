package com.sparta.showmethecode.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @JoinColumn(name = "review_request_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewRequest reviewRequest;

    public void setReviewRequest(ReviewRequest reviewRequest) {
        this.reviewRequest = reviewRequest;
    }

    public ReviewRequestComment(String content, User user) {
        this.content = content;
        this.user = user;
    }
}
