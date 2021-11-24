package com.sparta.showmethecode.repository;

import com.sparta.showmethecode.domain.ReviewRequest;
import com.sparta.showmethecode.repository.dao.CommentDao;
import com.sparta.showmethecode.repository.dao.ReviewRequestDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRequestRepository extends JpaRepository<ReviewRequest, Long>, ReviewRequestDao {

    Page<ReviewRequest> findAll(Pageable pageable);

}
