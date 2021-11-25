package com.sparta.showmethecode.repository.dao;

import com.sparta.showmethecode.domain.User;

import java.util.List;

public interface UserDao {

    List<User> findReviewerByLanguage(String language);
}
