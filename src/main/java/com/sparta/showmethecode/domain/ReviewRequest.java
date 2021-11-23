package com.sparta.showmethecode.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 코드리뷰 요청서
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class ReviewRequest extends Timestamped{

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String code;

    private String comment;

    @Enumerated(EnumType.STRING)
    private ReviewRequestStatus status;

    @Column(nullable = false)
    private String languageName;

    // 한 명의 사용자는 여러 개 쿄드리뷰 요청서를 작성할 수 있다.
    @JoinColumn(name = "request_user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User requestUser;

    // 한 명의 리뷰어는 여러 개 코드리뷰 요청서에 리뷰를 할 수 있다.
    @JoinColumn(name = "answer_user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User answerUser;

    @JoinColumn(name = "review_answer_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewAnswer reviewAnswer;

    public ReviewRequest(User requestUser, String title, String code, String comment, ReviewRequestStatus status, String languageName) {
        this.requestUser = requestUser;
        this.title = title;
        this.code = code;
        this.comment = comment;
        this.status = status;
        this.languageName = languageName;
    }

    public ReviewRequest(User requestUser, User answerUser,String title, String code, String comment, ReviewRequestStatus status, String languageName) {
        this.requestUser = requestUser;
        this.answerUser = answerUser;
        this.title = title;
        this.code = code;
        this.comment = comment;
        this.status = status;
        this.languageName = languageName;
    }
}
