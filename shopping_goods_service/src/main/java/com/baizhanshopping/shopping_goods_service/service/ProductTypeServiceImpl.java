package com.baizhanshopping.shopping_goods_service.service;

import com.baizhanshopping.shopping_common.pojo.ProductType;
import com.baizhanshopping.shopping_common.result.BusException;
import com.baizhanshopping.shopping_common.result.CodeEnum;
import com.baizhanshopping.shopping_common.service.ProductTypeService;
import com.baizhanshopping.shopping_goods_service.mapper.ProductTypeMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
@Transactional
@DubboService
public class ProductTypeServiceImpl implements ProductTypeService {
    @Autowired
    private ProductTypeMapper productTypeMapper;
    private ProductType checkLevel(ProductType productType)throws BusException{
        ProductType productTypeParent = productTypeMapper.selectById(productType.getParentId());
        if(productTypeParent==null){
            productType.setLevel(1);
        }else if(productTypeParent.getLevel()>=3){
            throw new BusException(CodeEnum.INSERT_PRODUCT_TYPE_ERROR);
        }else {
            productType.setLevel((int) (productTypeParent.getLevel()+1));
        }
        return productType;
    }
    @Override
    public void add(ProductType productType) {
        ProductType productTypeCheck = checkLevel(productType);
        productTypeMapper.insert(productTypeCheck);
    }

    @Override
    public void update(ProductType productType) {
        ProductType productTypeCheck = checkLevel(productType);
        productTypeMapper.updateById(productTypeCheck);
    }

    @Override
    public ProductType findById(Long id) {
        return productTypeMapper.selectById(id);
    }

    @Override
    public void delete(Long id) {
        QueryWrapper<ProductType> productTypeQueryWrapper = new QueryWrapper<>();
        productTypeQueryWrapper.eq("parentId",id);
        List<ProductType> productTypes = productTypeMapper.selectList(productTypeQueryWrapper);
        if(!productTypes.isEmpty()){
            throw new BusException(CodeEnum.DELETE_PRODUCT_TYPE_ERROR);
        }
        productTypeMapper.deleteById(id);
    }

    @Override
    public Page<ProductType> search(ProductType productType, int page, int size) {
        QueryWrapper<ProductType> queryWrapper = new QueryWrapper();
        if(productType != null){
            // 类型名不为空时
            if(StringUtils.hasText(productType.getName())){
                queryWrapper.like("name",productType.getName());
            }
            // 上级类型id不为空时
            if (productType.getParentId() != null){
                queryWrapper.eq("parentId",productType.getParentId());
            }
        }
        return productTypeMapper.selectPage(new Page(page,size),queryWrapper);
    }

    @Override
    public List<ProductType> findProductType(ProductType productType) {
        QueryWrapper<ProductType> queryWrapper = new QueryWrapper();
        if(productType != null){
            // 类型名不为空时
            if(StringUtils.hasText(productType.getName())){
                queryWrapper.like("name",productType.getName());
            }
            // 上级类型id不为空时
            if (productType.getParentId() != null){
                queryWrapper.eq("parentId",productType.getParentId());
            }
        }
        return productTypeMapper.selectList(queryWrapper);
    }
}
