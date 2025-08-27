package com.baizhanshopping.shopping_manager_api.security.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class AccessDeniedExcptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public void defaultExceptionHandler (AccessDeniedException e) throws AccessDeniedException {
        throw e;
    }
}
