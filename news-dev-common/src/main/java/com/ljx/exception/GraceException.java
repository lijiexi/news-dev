package com.ljx.exception;

import com.ljx.grace.result.ResponseStatusEnum;

/**
 * 自定义异常
 */
public class GraceException {
    public static void display(ResponseStatusEnum responseStatusEnum){
        throw new MyCustomException(responseStatusEnum);
    }
}
