package com.baizhanshopping.shopping_order_customer_api.controller;

import com.baizhanshopping.shopping_common.pojo.CartGoods;
import com.baizhanshopping.shopping_common.pojo.Orders;
import com.baizhanshopping.shopping_common.result.BaseRsult;
import com.baizhanshopping.shopping_common.service.AddressService;
import com.baizhanshopping.shopping_common.service.CartService;
import com.baizhanshopping.shopping_common.service.OrderService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/orders")
public class OrderController {
    @DubboReference
    private OrderService orderService;
    @DubboReference
    private CartService cartService;
    @PostMapping("/add")
    public BaseRsult<Orders> add(@RequestBody Orders orders, @RequestHeader Long userid){
        orders.setUserId(userid);
        Orders orders1 = orderService.add(orders);
        List<CartGoods> cartGoods = orders.getCartGoods();
        for (CartGoods cartGood : cartGoods) {
            cartService.deleteCartOption(userid,cartGood.getGoodId());
        }
        return BaseRsult.success(orders1);
    }
    @GetMapping("/findById")
    public BaseRsult<Orders> findById(String id){
        Orders orders = orderService.findById(id);
        return BaseRsult.success(orders);
    }
    @GetMapping("/findUserOrders")
    public BaseRsult<List<Orders>> findUserOrders(Integer status,@RequestHeader Long userId){
        List<Orders> userOrders = orderService.findUserOrders(userId, status);
        return BaseRsult.success(userOrders);
    }
}
