package com.sparta.showmethecode.repository;

import com.sparta.showmethecode.domain.ReviewRequestComment;
import com.sparta.showmethecode.repository.dao.CommentDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRequestCommentRepository extends JpaRepository<ReviewRequestComment, Long>, CommentDao {
}
