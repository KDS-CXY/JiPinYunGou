package com.baizhanshopping.shopping_order_service.service;

import com.baizhanshopping.shopping_common.pojo.CartGoods;
import com.baizhanshopping.shopping_common.pojo.Orders;
import com.baizhanshopping.shopping_common.service.OrderService;
import com.baizhanshopping.shopping_order_service.mapper.CartGoodsMapper;
import com.baizhanshopping.shopping_order_service.mapper.OrdersMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@DubboService
public class OrdersServiceImpl implements OrderService {
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private CartGoodsMapper cartGoodsMapper;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    private final String CHECK_ORDERS_QUEUE= "check_orders_queue";
    @Override
    public Orders add(Orders orders) {
        orders.setStatus(1);
        orders.setCloseTime(new Date());
        List<CartGoods> cartGoods = orders.getCartGoods();
        BigDecimal sum=BigDecimal.ZERO;
        for (CartGoods cartGood : cartGoods) {
            BigDecimal num=new BigDecimal(cartGood.getNum());
            sum=sum.add(num.multiply(cartGood.getPrice()));
        }
        orders.setPayment(sum);
        ordersMapper.insert(orders);
        for (CartGoods cartGood : cartGoods) {
            // 购物车商品保存到数据库中
            cartGood.setOrderId(orders.getId());
            cartGoodsMapper.insert(cartGood);
        }
        rocketMQTemplate.syncSend(CHECK_ORDERS_QUEUE, MessageBuilder.withPayload(orders.getId()).build(),15000,15);
        return orders;
    }

    @Override
    public void update(Orders orders) {
        ordersMapper.updateById(orders);
    }

    @Override
    public Orders findById(String id) {
        return ordersMapper.findById(id);
    }

    @Override
    public List<Orders> findUserOrders(Long userId, Integer status) {
        return ordersMapper.findOrdersByUserIdAndStatus(userId,status);
    }
}
