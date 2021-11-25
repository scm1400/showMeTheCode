package com.sparta.showmethecode.repository;

import com.sparta.showmethecode.domain.ReviewRequestComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRequestCommentRepository extends JpaRepository<ReviewRequestComment, Long> {
}
