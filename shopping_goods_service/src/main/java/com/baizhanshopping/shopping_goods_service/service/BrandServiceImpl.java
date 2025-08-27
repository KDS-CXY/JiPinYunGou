package com.baizhanshopping.shopping_goods_service.service;

import com.baizhanshopping.shopping_common.pojo.Brand;
import com.baizhanshopping.shopping_common.result.BusException;
import com.baizhanshopping.shopping_common.result.CodeEnum;
import com.baizhanshopping.shopping_common.service.BrandService;
import com.baizhanshopping.shopping_goods_service.mapper.BrandMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.source.tree.BreakTree;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DubboService
@Transactional
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;
    @Override
    public Brand findBrandById(Long id) {
        if (id == 0){
            int i = 1/0; // 模拟系统异常
        }else if (id == -1){
            throw new BusException(CodeEnum.PARAMETER_ERROR); // 模拟业务异常
        }
        return brandMapper.selectById(id);
    }

    @Override
    public List<Brand> findAll() {
        return brandMapper.selectList(null);
    }

    @Override
    public void add(Brand brand) {
        brandMapper.insert(brand);
    }

    @Override
    public void update(Brand brand) {
        brandMapper.updateById(brand);
    }

    @Override
    public void delete(Long id) {
        brandMapper.deleteById(id);
    }

    @Override
    public Page<Brand> search(Brand brand, int page, int size) {
        LambdaQueryWrapper<Brand> lqw=new LambdaQueryWrapper<>();
        if(brand!=null){
            lqw.like(brand.getName()!=null,Brand::getName,brand.getName());
        }
        return brandMapper.selectPage(new Page<Brand>(page,size),lqw);
    }
}
