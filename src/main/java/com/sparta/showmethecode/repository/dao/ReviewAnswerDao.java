package com.sparta.showmethecode.repository.dao;

public interface ReviewAnswerDao {

    // 현재 답변에 평가가 됐는지 조회
    boolean isEvaluated(Long answerId);
}
