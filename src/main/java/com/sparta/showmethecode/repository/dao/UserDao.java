package com.sparta.showmethecode.repository.dao;

import com.sparta.showmethecode.domain.User;

import java.util.List;

public interface UserDao {

    // 언어별 리뷰어 조회
    List<User> findReviewerByLanguage(String language);
}
