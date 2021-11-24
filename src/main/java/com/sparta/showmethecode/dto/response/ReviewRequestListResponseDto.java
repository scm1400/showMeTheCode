package com.sparta.showmethecode.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ReviewRequestListResponseDto {

    private List<ReviewRequestResponseDto> list = new ArrayList<>();

    private int totalPage;
    private int totalElements;
    private int page;
    private int size;
}
