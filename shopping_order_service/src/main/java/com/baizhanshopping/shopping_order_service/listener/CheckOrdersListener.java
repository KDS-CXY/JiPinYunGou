package com.baizhanshopping.shopping_order_service.listener;

import com.baizhanshopping.shopping_common.pojo.Orders;
import com.baizhanshopping.shopping_common.service.OrderService;
import com.baizhanshopping.shopping_order_service.service.OrdersServiceImpl;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "check_orders_queue",consumerGroup = "check_orders_group")
public class CheckOrdersListener implements RocketMQListener<String> {
    @Autowired
    private OrderService orderService;
    @Override
    public void onMessage(String s) {
        Orders orders = orderService.findById(s);
        if (orders.getStatus()==1){
            // 订单未支付
            orders.setStatus(6);
            orderService.update(orders);
        }
    }
}
