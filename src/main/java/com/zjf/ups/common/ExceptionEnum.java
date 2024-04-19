package com.zjf.ups.common;

public enum ExceptionEnum {

    THROWABLE_ERROR(10000, "未知错误"),
    ;


    private Integer errorCode;

    private String errorMessage;

    private ExceptionEnum(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
