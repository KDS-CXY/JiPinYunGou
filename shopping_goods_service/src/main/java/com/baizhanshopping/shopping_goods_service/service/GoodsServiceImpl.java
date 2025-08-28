package com.baizhanshopping.shopping_goods_service.service;

import com.baizhanshopping.shopping_common.pojo.Goods;
import com.baizhanshopping.shopping_common.pojo.GoodsImage;
import com.baizhanshopping.shopping_common.pojo.Specification;
import com.baizhanshopping.shopping_common.pojo.SpecificationOption;
import com.baizhanshopping.shopping_common.service.GoodsService;
import com.baizhanshopping.shopping_goods_service.mapper.GoodsImageMapper;
import com.baizhanshopping.shopping_goods_service.mapper.GoodsMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboService;
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
    }


    @Override
    public Goods findById(Long id) {
        return goodsMapper.findById(id);
    }

    @Override
    public void putAway(Long id, Boolean isMarketable) {
        goodsMapper.putAway(id,isMarketable);
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
}
