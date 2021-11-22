package com.sparta.showmethecode.repository;

import com.sparta.showmethecode.domain.ReviewRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRequestRepository extends JpaRepository<ReviewRequest, Long> {

    Page<ReviewRequest> findAll(Pageable pageable);

}
