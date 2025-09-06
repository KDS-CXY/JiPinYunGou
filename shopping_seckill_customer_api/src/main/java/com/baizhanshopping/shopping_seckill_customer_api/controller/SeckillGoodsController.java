package com.baizhanshopping.shopping_seckill_customer_api.controller;

import com.baizhanshopping.shopping_common.pojo.Orders;
import com.baizhanshopping.shopping_common.pojo.SeckillGoods;
import com.baizhanshopping.shopping_common.result.BaseRsult;
import com.baizhanshopping.shopping_common.service.OrderService;
import com.baizhanshopping.shopping_common.service.SeckillService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/seckillGoods")
public class SeckillGoodsController {
    @DubboReference
    private SeckillService service;
    @DubboReference
    private OrderService orderService;

    /**
     * 用户分页查询秒杀商品
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/findPage")
    public BaseRsult<Page<SeckillGoods>> findPage(int page,int size){
        Page<SeckillGoods> pageByRedis = service.findPageByRedis(page, size);
        return BaseRsult.success(pageByRedis);
    }
    /**
     * 用户查询秒杀商品详情
     * @param id 商品Id
     * @return 查询结果
     */
    @GetMapping("/findById")
    public BaseRsult<SeckillGoods> findById(Long id){
        // 从redis中查询秒杀商品详情
        SeckillGoods seckillGoods = service.findSeckillGoodsByRedis(id);
        if (seckillGoods != null){
            return BaseRsult.success(seckillGoods);
        }else {
            // 如果redis中查找不到，再从数据库查询秒杀商品详情
            SeckillGoods secillGoodsByMySql = service.findSecillGoodsByMySql(id);
            return BaseRsult.success(secillGoodsByMySql);
        }
    }
    /**
     * 生成秒杀订单
     * @param orders 订单对象
     * @return 生成的订单
     */
    @PostMapping("/add")
    public BaseRsult<Orders> add(@RequestBody Orders orders,@RequestHeader Long userId){
        orders.setUserId(userId);
        Orders order = service.createOrder(orders);
        return BaseRsult.success(order);
    }
    /**
     * 根据id查询秒杀订单
     * @param id 订单id
     * @return 查询结果
     */
    @GetMapping("/findOrder")
    public BaseRsult<Orders> findOrder(String id){
        Orders orders = service.findOrder(id);
        return BaseRsult.success(orders);
    }
    /**
     * 支付秒杀订单
     * @param id 订单id
     */
    @GetMapping("/pay")
    public BaseRsult pay(String id){
        // 支付秒杀订单
        Orders orders = service.pay(id);
        // 将订单数据存入数据库
        orderService.add(orders);
        return BaseRsult.success();
    }


}
