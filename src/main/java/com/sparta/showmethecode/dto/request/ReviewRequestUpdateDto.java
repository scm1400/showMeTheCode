package com.sparta.showmethecode.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class ReviewRequestUpdateDto {

    private String title;
    private String content;

}
