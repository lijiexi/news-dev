package com.ljx.exception;

import com.ljx.grace.result.GraceJSONResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * AOP,统一自定义异常拦截处理，针对异常类型进行捕获，返回json到前端
 */
@ControllerAdvice
public class GraceExceptionHandler {

    @ExceptionHandler(MyCustomException.class)
    @ResponseBody
    public GraceJSONResult returnMyException(MyCustomException e){
        e.printStackTrace();
        return GraceJSONResult.exception(e.getResponseStatusEnum());
    }
}
