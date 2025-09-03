package com.baizhanshopping.shopping_goods_service.service;

import com.baizhanshopping.shopping_common.pojo.*;
import com.baizhanshopping.shopping_common.service.GoodsService;
import com.baizhanshopping.shopping_common.service.SearchService;
import com.baizhanshopping.shopping_goods_service.mapper.GoodsImageMapper;
import com.baizhanshopping.shopping_goods_service.mapper.GoodsMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
@DubboService
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsImageMapper goodsImageMapper;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    private final String SYNC_GOODS_QUEUE = "sync_goods_queue";
    private final String DEL_GOODS_QUEUE = "del_goods_queue";
    // 同步商品到购物车主题
    private final String SYNC_CART_QUEUE = "sync_cart_queue";
    // 删除商品到购物车主题
    private final String DEL_CART_QUEUE = "del_cart_queue";
    @Override
    public void add(Goods goods) {
        goodsMapper.insert(goods);
        Long goodsId = goods.getId();
        //取出image地址集合
        List<GoodsImage> images = goods.getImages();
        //遍历图片集合插入
        images.forEach(image->{
                image.setGoodsId(goodsId);
                goodsImageMapper.insert(image);});
        //取出规格集合
        List<Specification> specifications = goods.getSpecifications();
        //遍历规格
        specifications.forEach(specification -> {
            //取出规格项
            List<SpecificationOption> specificationOptions = specification.getSpecificationOptions();
            //遍历规格项插入商品规格表
            specificationOptions.forEach(specificationOption -> {
                goodsMapper.addGoodsSpecificationOption(goodsId,specificationOption.getId());
            });
        });
        //同步添加到ES中
        GoodsDesc desc = findDesc(goodsId);
        rocketMQTemplate.syncSend(SYNC_GOODS_QUEUE,desc);
    }

    @Override
    public void update(Goods goods) {
        // 删除旧图片数据
        Long goodsId = goods.getId(); // 商品id
        QueryWrapper<GoodsImage> queryWrapper = new QueryWrapper();
        queryWrapper.eq("goodsId",goodsId);
        goodsImageMapper.delete(queryWrapper);
        // 删除旧规格项数据
        goodsMapper.deleteGoodsSpecificationOption(goodsId);
        // 插入商品数据
        goodsMapper.updateById(goods);
        // 插入图片数据
        List<GoodsImage> images = goods.getImages(); // 商品图片
        for (GoodsImage image : images) {
            image.setGoodsId(goodsId); // 给图片设置商品id
            goodsImageMapper.insert(image); // 插入图片
        }
        // 插入商品_规格项数据
        List<Specification> specifications = goods.getSpecifications(); // 获取规格
        List<SpecificationOption> options = new ArrayList(); // 规格项集合
        // 遍历规格，获取规格中的所有规格项
        for (Specification specification : specifications) {
            options.addAll(specification.getSpecificationOptions());
        }
        // 遍历规格项，插入商品_规格项数据
        for (SpecificationOption option : options) {
            goodsMapper.addGoodsSpecificationOption(goodsId,option.getId());
        }
        //同步添加到ES中
        rocketMQTemplate.syncSend(DEL_GOODS_QUEUE,goodsId);
        GoodsDesc desc = findDesc(goodsId);
        rocketMQTemplate.syncSend(SYNC_GOODS_QUEUE,desc);
        // 将商品数据同步到购物车中
        CartGoods cartGoods = new CartGoods();
        cartGoods.setGoodId(goods.getId());
        cartGoods.setGoodsName(goods.getGoodsName());
        cartGoods.setHeaderPic(goods.getHeaderPic());
        cartGoods.setPrice(goods.getPrice());
        rocketMQTemplate.syncSend(SYNC_CART_QUEUE,cartGoods);
    }


    @Override
    public Goods findById(Long id) {
        return goodsMapper.findById(id);
    }

    @Override
    public void putAway(Long id, Boolean isMarketable) {
        goodsMapper.putAway(id,isMarketable);
        //上架时同步添加到ES中
        if (isMarketable){
            GoodsDesc goodsDesc = findDesc(id);
            rocketMQTemplate.syncSend(SYNC_GOODS_QUEUE,goodsDesc);
        }else {
            //下架时删除ES中的数据
            rocketMQTemplate.syncSend(DEL_GOODS_QUEUE,id);
            // 删除商品数据同步到购物车中
            rocketMQTemplate.syncSend(DEL_CART_QUEUE,id);
        }
    }

    @Override
    public Page<Goods> search(Goods goods, int page, int size) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper();
        // 判断商品名不为空
        if (goods != null && StringUtils.hasText(goods.getGoodsName())){
            queryWrapper.like("goodsName",goods.getGoodsName());
        }
        Page<Goods> page1 = goodsMapper.selectPage(new Page(page, size), queryWrapper);
        return page1;
    }

    @Override
    public List<GoodsDesc> findAll() {
        return goodsMapper.findAll();
    }

    @Override
    public GoodsDesc findDesc(Long id) {
        return goodsMapper.findDesc(id);
    }
}
