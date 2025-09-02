package com.baizhanshopping.shopping_common.service;

import com.baizhanshopping.shopping_common.result.BaseRsult;

public interface MessageService {
    BaseRsult sendMessage(String phoneNumber,String code);
}
