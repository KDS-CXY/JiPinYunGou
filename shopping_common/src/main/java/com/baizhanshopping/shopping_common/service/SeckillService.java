package com.baizhanshopping.shopping_common.service;

import com.baizhanshopping.shopping_common.pojo.Orders;
import com.baizhanshopping.shopping_common.pojo.SeckillGoods;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface SeckillService {
    /**
     * 前台用户查询秒杀商品
     * @param page 页数
     * @param size 每页条数
     * @return 查询结果
     */
    Page<SeckillGoods> findPageByRedis(int page, int size);

    /**
     * 查询秒杀商品详情
     * @param goodsId 秒杀商品对应的商品Id
     * @return 查询结果
     */
    SeckillGoods findSeckillGoodsByRedis(Long goodsId);


    /**
     * 生成秒杀订单
     * @param orders 订单数据
     * @return
     */
    Orders createOrder(Orders orders);

    /**
     * 根据id查询秒杀订单
     * @param id 订单id
     * @return
     */
    Orders findOrder(String id);

    /**
     * 支付秒杀订单
     * @param orderId 订单id
     * @return
     */
    Orders pay(String orderId);
    /**
     * 将一个秒杀商品保存到redis中
     * @param seckillGoods 秒杀商品对象
     */
    void addRedisSeckillGoods(SeckillGoods seckillGoods);
    /**
     * 从数据库根据商品id查询秒杀商品
     */
    SeckillGoods findSecillGoodsByMySql(Long goodsId);
}
