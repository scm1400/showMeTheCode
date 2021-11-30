package com.sparta.showmethecode.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AddReviewDto {

    private String title;
    private String code;
    private String comment;

}
