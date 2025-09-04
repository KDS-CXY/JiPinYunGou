package com.baizhanshopping.shopping_common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回结果的枚举类型
 */
@Getter
@AllArgsConstructor
public enum CodeEnum {
    SUCCESS(200,"请求成功"),
    ORDER_STATUS_ERROR(601,"订单状态异常"),
    SYSTEM_ERROR(500,"系统异常"),
    PARAMETER_ERROR(601,"参数异常"),
    INSERT_PRODUCT_TYPE_ERROR(602,"父类级别异常"),
    DELETE_PRODUCT_TYPE_ERROR(603,"存在子类删除异常"),
    UPLOAD_FILE_ERROR(604,"上传文件异常"),
    REGISTER_CODE_ERROR(605,"验证码错误"),
    REGISTER_REPEAT_PHONE_ERROR(606,"手机号已注册"),
    REGISTER_REPEAT_NAME_ERROR(607,"用户名已注册"),
    LOGIN_NAME_PASSWORD_ERROR(608,"用户名或密码错误"),
    LOGIN_CODE_ERROR(609,"登录验证码错误"),
    LOGIN_NOPHONE_ERROR(610,"该手机号未注册"),
    LOGIN_USER_STATUS_ERROR(611,"用户状态异常"),
    QR_CODE_ERROR(612,"二维码生成错误"),
    CHECK_SIGN_ERROR(612,"二维码生成错误")
    ;
    private final Integer code;
    private final String message;
}
