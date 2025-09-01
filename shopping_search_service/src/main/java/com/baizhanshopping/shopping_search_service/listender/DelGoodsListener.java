package com.baizhanshopping.shopping_search_service.listender;

import com.baizhanshopping.shopping_common.service.SearchService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "del_goods_queue",consumerGroup = "del_goods_group")
public class DelGoodsListener implements RocketMQListener<Long> {
    @Autowired
    private SearchService service;
    @Override
    public void onMessage(Long id) {
        service.delete(id);
        System.out.println("删除商品成功");
    }
}
