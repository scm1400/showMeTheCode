package com.sparta.showmethecode.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
@Data
public class ReviewerInfoDto {

    private Long id;
    private String username;
    private List<String> languages = new ArrayList<>();

    private int answerCount;
    private double point;
}
