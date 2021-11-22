package com.sparta.showmethecode.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 코드리뷰 답변서
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class ReviewAnswer extends Timestamped {

    @Id @GeneratedValue
    private Long id;

    private String title;

    private String code;

    private String comment;

    @JoinColumn(name = "answer_user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User answerUser;
}
