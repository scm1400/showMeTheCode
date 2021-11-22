package com.sparta.showmethecode.repository;

import com.sparta.showmethecode.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}
