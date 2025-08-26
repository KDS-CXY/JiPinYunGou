package com.baizhanshopping.shopping_common.result;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusException.class)
    public BaseRsult defaultExceptionHandler(BusException e){
        BaseRsult baseRsult = new BaseRsult(e.getCode(),e.getMsg(),null);
        return baseRsult;
    }
    @ExceptionHandler(Exception.class)
    public BaseRsult defaultExceptionHandler(Exception e){
        e.printStackTrace();
        return new BaseRsult(CodeEnum.SYSTEM_ERROR.getCode(),CodeEnum.SYSTEM_ERROR.getMessage(),null);
    }
}
