package com.sparta.showmethecode.domain;

import lombok.Data;

import java.util.Map;

@Data
public class Result {

    private StatusEnum status;
    private String message;
    private Object object;

    public Result(){
        this.status = StatusEnum.BAD_REQUEST;
        this.object = null;
        this.message = null;
    }

}

