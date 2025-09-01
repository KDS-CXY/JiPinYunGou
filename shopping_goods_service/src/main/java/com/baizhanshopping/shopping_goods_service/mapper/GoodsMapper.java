package com.baizhanshopping.shopping_goods_service.mapper;

import com.baizhanshopping.shopping_common.pojo.Goods;
import com.baizhanshopping.shopping_common.pojo.GoodsDesc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper extends BaseMapper<Goods> {
    void addGoodsSpecificationOption(@Param("gid") Long gid,@Param("optionId") Long optionId);
    // 删除商品下的所有规格项
    void deleteGoodsSpecificationOption(Long gid);
    // 商品上/下架
    void putAway(@Param("id") Long id,@Param("isMarketable") Boolean isMarketable);
    // 根据id查询商品详情
    Goods findById(Long id);
    List<GoodsDesc> findAll();
    GoodsDesc findDesc(Long id);
}
