package com.sparta.showmethecode.repository;

import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.repository.dao.UserDao;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserDao {

    Optional<User> findByUsername(String username);

    List<User> findTop5ByOrderByEvalTotalDesc();
}
