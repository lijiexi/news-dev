package com.ljx.exception;

import com.ljx.grace.result.ResponseStatusEnum;

/**
 * 自定义异常，传入枚举常量，便于解耦
 */
public class MyCustomException extends RuntimeException{

    private ResponseStatusEnum responseStatusEnum;
    public MyCustomException(ResponseStatusEnum responseStatusEnum){
        super("异常状态码："+responseStatusEnum.status()
                +"; 具体信息："+responseStatusEnum.msg());
        this.responseStatusEnum = responseStatusEnum;
    }

    public ResponseStatusEnum getResponseStatusEnum() {
        return responseStatusEnum;
    }

    public void setResponseStatusEnum(ResponseStatusEnum responseStatusEnum) {
        this.responseStatusEnum = responseStatusEnum;
    }
}
