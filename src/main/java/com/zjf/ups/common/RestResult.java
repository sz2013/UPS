package com.zjf.ups.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RestResult<T> {

    private Boolean success = true;

    private Integer errorCode;

    private String errorMessage;

    private T data;

    public RestResult(T data) {
        this.data = data;
    }

    public RestResult() {
        this.data = null;
    }

    public RestResult(Integer errorCode, String errorMessage) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.data = null;
    }
}
