package com.baizhanshopping.shopping_search_service.listender;

import com.baizhanshopping.shopping_common.pojo.GoodsDesc;
import com.baizhanshopping.shopping_common.service.SearchService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "sync_goods_queue",consumerGroup = "sync_goods_group")
public class SyncGoodsListener implements RocketMQListener<GoodsDesc> {
     @Autowired
    private SearchService service;

    @Override
    public void onMessage(GoodsDesc goodsDesc) {
        service.syncGoodsToES(goodsDesc);
        System.out.println("同步商品成功");
    }
}
