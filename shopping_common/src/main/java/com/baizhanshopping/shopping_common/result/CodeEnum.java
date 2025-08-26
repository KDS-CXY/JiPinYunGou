package com.baizhanshopping.shopping_common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 返回结果的枚举类型
 */
@Getter
@AllArgsConstructor
public enum CodeEnum {
    SUCCESS(200,"请求成功"),
    SYSTEM_ERROR(500,"系统异常"),
    PARAMETER_ERROR(601,"参数异常");
    private final Integer code;
    private final String message;
}
