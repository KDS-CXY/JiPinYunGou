package com.baizhanshopping.shopping_seckill_service.redis;

import com.baizhanshopping.shopping_common.pojo.Orders;
import com.baizhanshopping.shopping_common.pojo.SeckillGoods;
import com.baizhanshopping.shopping_common.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillService seckillService;
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = message.toString();
        Orders orders = (Orders) redisTemplate.opsForValue().get(key + "_copy");
        Long goodId = orders.getCartGoods().get(0).getGoodId();
        Integer num = orders.getCartGoods().get(0).getNum();
        SeckillGoods seckillGoodsByRedis = seckillService.findSeckillGoodsByRedis(goodId);
        seckillGoodsByRedis.setStockCount(seckillGoodsByRedis.getStockCount() + num);
        redisTemplate.boundHashOps("seckillGoods").put(goodId,seckillGoodsByRedis);
        // 删除复制订单数据
        redisTemplate.delete(key+"_copy");
    }
}
