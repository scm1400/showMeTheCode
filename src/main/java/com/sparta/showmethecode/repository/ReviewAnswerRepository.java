package com.sparta.showmethecode.repository;

import com.sparta.showmethecode.domain.ReviewAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewAnswerRepository extends JpaRepository<ReviewAnswer, Long> {

}
