package com.sparta.showmethecode.dto.request;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class SignupRequestDto {

    private String username;
    private String password;
    private boolean reviewer;
    private Set<String> languages = new HashSet<>();
    private int reviewCount;
    private int rankingPoint;
}
