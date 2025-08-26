package com.baizhanshopping.shopping_common.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果
 * @param <T> 返回data对象类型
 */
@AllArgsConstructor
@Data
public class BaseRsult<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    public static<T> BaseRsult<T> success() {
        return new BaseRsult(CodeEnum.SUCCESS.getCode(),CodeEnum.SUCCESS.getMessage(),null);
    }
    public static <T> BaseRsult<T> success(T data){
        return new BaseRsult(CodeEnum.SUCCESS.getCode(),CodeEnum.SUCCESS.getMessage(),data);
    }
}
