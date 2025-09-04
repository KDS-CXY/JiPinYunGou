package com.baizhanshopping.shopping_order_service.mapper;

import com.baizhanshopping.shopping_common.pojo.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrdersMapper extends BaseMapper<Orders> {
    Orders findById(String id);
    // 查询用户订单
    List<Orders> findOrdersByUserIdAndStatus(@Param("userId") Long userId,
                                             @Param("status") Integer status);
}
