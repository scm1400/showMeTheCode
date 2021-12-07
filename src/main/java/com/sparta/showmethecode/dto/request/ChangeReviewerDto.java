package com.sparta.showmethecode.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ChangeReviewerDto {

    private Long newReviewerId;
}
