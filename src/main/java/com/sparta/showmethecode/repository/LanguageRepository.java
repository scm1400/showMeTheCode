package com.sparta.showmethecode.repository;

import com.sparta.showmethecode.domain.Language;
import com.sparta.showmethecode.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, Long> {

}
