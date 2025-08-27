package com.baizhanshopping.shopping_goods_service.mapper;

import com.baizhanshopping.shopping_common.pojo.Specification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface SpecificationMapper extends BaseMapper<Specification> {
    Specification findById(Long id);
    List<Specification> findByProductTypeId(Long productTypeId);
}
