package com.sparta.showmethecode.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.showmethecode.dto.request.AddCommentDto;
import com.sparta.showmethecode.dto.request.UpdateAnswerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    private String content;

    private double point;

    @JoinColumn(name = "answer_user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User answerUser;

    @JsonIgnore
    @JoinColumn(name = "review_request_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewRequest reviewRequest;

    @OneToMany(mappedBy = "reviewAnswer", cascade = CascadeType.ALL)
    private List<ReviewAnswerComment> comments = new ArrayList<>();

    public ReviewAnswer(String title, String content, User answerUser) {
        this.title = title;
        this.content = content;
        this.answerUser = answerUser;
    }

    public void evaluate(double point) {
        this.point = point;
    }

    public void update(UpdateAnswerDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }

    public void addComment(ReviewAnswerComment comment) {
        this.getComments().add(comment);
        comment.setReviewAnswer(this);
    }
}
