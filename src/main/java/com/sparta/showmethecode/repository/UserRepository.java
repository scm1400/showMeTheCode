package com.sparta.showmethecode.repository;

import com.sparta.showmethecode.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
