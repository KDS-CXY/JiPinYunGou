package com.baizhanshopping.shopping_goods_service.service;

import com.baizhanshopping.shopping_common.pojo.Brand;
import com.baizhanshopping.shopping_common.service.BrandService;
import com.baizhanshopping.shopping_goods_service.mapper.BrandMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DubboService
@Transactional
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;
    @Override
    public Brand findBrandById(Long id) {
        return brandMapper.selectById(id);
    }
}
