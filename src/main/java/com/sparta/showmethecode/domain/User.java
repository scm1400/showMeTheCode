package com.sparta.showmethecode.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class User extends Timestamped{

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole; // ROLE_USER: 일반사용자, ROLE_REVIEWER: 리뷰어

    private int reviewCount; // 몇 개의 코드리뷰를 완료했는지
    private int rankingPoint; // 리뷰어의 랭킹포인트

    @OneToMany(mappedBy = "user")
    private List<Language> languages = new ArrayList<>();

    // 연관관계 편의 메서드
    private void addLanguage(Language language) {
            this.getLanguages().add(language);
            language.setUser(this);
    }

    public User(String username, String password, UserRole userRole, int reviewCount, int rankingPoint, List<Language> languages) {
        this.username = username;
        this.password = password;
        this.userRole = userRole;
        this.reviewCount = reviewCount;
        this.rankingPoint = rankingPoint;
        this.languages = languages;
    }

    public User(String username, String password, UserRole userRole, int reviewCount, int rankingPoint) {
        this.username = username;
        this.password = password;
        this.userRole = userRole;
        this.reviewCount = reviewCount;
        this.rankingPoint = rankingPoint;
    }
}
