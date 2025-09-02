package com.baizhanshopping.shopping_message_service.service;

import com.baizhanshopping.shopping_common.result.BaseRsult;
import com.baizhanshopping.shopping_common.service.MessageService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@Service
@DubboService
public class MessageServiceImpl implements MessageService {
    @Override
    public BaseRsult sendMessage(String phoneNumber, String code) {
        return new BaseRsult(200,"OK","信息发送成功");
    }
}
