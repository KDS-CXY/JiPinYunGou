package com.baizhanshopping.shopping_cart_service.service;

import com.baizhanshopping.shopping_common.pojo.CartGoods;
import com.baizhanshopping.shopping_common.service.CartService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
@Service
@DubboService
public class CartServiceImpl implements CartService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void addCart(Long userId, CartGoods cartGoods) {
        // 1.根据用户id获取用户购物车列表
        List<CartGoods> cartList = findCartList(userId);
        // 2.查询购物车是否有该商品，如果有商品，添加商品数量
        for (CartGoods cartGoods1 : cartList) {
            if (cartGoods.getGoodId().equals(cartGoods1.getGoodId())){
                int newNum = cartGoods1.getNum() + cartGoods.getNum();
                cartGoods1.setNum(newNum);
                redisTemplate.boundHashOps("cartList").put(userId,cartList);
                return;
            }
        }
        // 3.如果购物车没有该商品，将商品添加到购物车列表
        cartList.add(cartGoods);
        redisTemplate.boundHashOps("cartList").put(userId,cartList);
    }

    @Override
    public void handleCart(Long userId, Long goodId, Integer num) {
        List<CartGoods> cartList = findCartList(userId);
        for (CartGoods cartGoods : cartList) {
            if(goodId.equals(cartGoods.getGoodId())){
                cartGoods.setNum(num);
                break;
            }
        }
        redisTemplate.boundHashOps("cartList").put(userId,cartList);
    }

    @Override
    public void deleteCartOption(Long userId, Long goodId) {
        List<CartGoods> cartList = findCartList(userId);
        for (CartGoods cartGoods : cartList) {
            if(goodId.equals(cartGoods.getGoodId())){
                cartList.remove(cartGoods);
                break;
            }
        }
        redisTemplate.boundHashOps("cartList").put(userId,cartList);
    }

    @Override
    public List<CartGoods> findCartList(Long userId) {
        Object cartList = redisTemplate.boundHashOps("cartList").get(userId);
        if(cartList==null){
            return new ArrayList<CartGoods>();
        }else {
            return (List<CartGoods>) cartList;
        }
    }

    @Override
    public void refreshCartGoods(CartGoods cartGoods) {
        BoundHashOperations cartList = redisTemplate.boundHashOps("cartList");
        Map<Long,List<CartGoods>> allCartGoods = cartList.entries();
        Collection<List<CartGoods>> values = allCartGoods.values();
        for (List<CartGoods> value : values) {
            for (CartGoods goods : value) {
                if(goods.getGoodId().equals(cartGoods.getGoodId())){
                    goods.setGoodsName(cartGoods.getGoodsName());
                    goods.setHeaderPic(cartGoods.getHeaderPic());
                    goods.setPrice(cartGoods.getPrice());
                }
            }
        }
        // 将改变后所有用户购物车重新放入redis
        redisTemplate.delete("cartList");
        redisTemplate.boundHashOps("cartList").putAll(allCartGoods);
    }

    @Override
    public void deleteCartGoods(Long goodId) {
        // 获取所有用户购物车
        BoundHashOperations cartList = redisTemplate.boundHashOps("cartList");
        Map<Long,List<CartGoods>> allCartGoods = cartList.entries();
        Collection<List<CartGoods>> values = allCartGoods.values();

        // 遍历所有用户的购物车
        for (List<CartGoods> goodsList : values) {
            // 遍历一个用户购物车的所有商品
            for (CartGoods goods : goodsList) {
                // 如果该商品是被下架的商品，在购物车删除该商品
                if (goodId.equals(goods.getGoodId())){
                    goodsList.remove(goods);
                    break;
                }
            }
        }

        // 将改变后所有用户购物车重新放入redis
        redisTemplate.delete("cartList");
        redisTemplate.boundHashOps("cartList").putAll(allCartGoods);
    }
}
