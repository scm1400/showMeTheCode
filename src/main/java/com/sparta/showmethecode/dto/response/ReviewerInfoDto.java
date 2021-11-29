package com.sparta.showmethecode.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
public class ReviewerInfoDto {

    private String username;
    private List<String> languages = new ArrayList<>();

    private int answerCount;
    private double point;
}
