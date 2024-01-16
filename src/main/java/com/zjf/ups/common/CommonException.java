package com.zjf.ups.common;

public class CommonException extends Exception {
    //错误码
    private Integer errorCode;

    public Integer getErrorCode() {
        return errorCode;
    }

    public CommonException(Integer errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
