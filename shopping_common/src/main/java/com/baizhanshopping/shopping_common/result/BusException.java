package com.baizhanshopping.shopping_common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义异常
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusException extends RuntimeException{
    private Integer code;
    //所有异常类的父类中定义了一个getMessage方法如果用Message有可能出现取不到值
    private String msg;
    public BusException (CodeEnum codeEnum){
        this.code=codeEnum.getCode();
        this.msg=codeEnum.getMessage();
    }
}
