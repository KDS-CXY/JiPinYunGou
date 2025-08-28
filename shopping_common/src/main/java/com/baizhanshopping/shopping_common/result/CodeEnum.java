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
    PARAMETER_ERROR(601,"参数异常"),
    INSERT_PRODUCT_TYPE_ERROR(602,"父类级别异常"),
    DELETE_PRODUCT_TYPE_ERROR(603,"存在子类删除异常"),
    UPLOAD_FILE_ERROR(604,"上传文件异常");
    private final Integer code;
    private final String message;
}
