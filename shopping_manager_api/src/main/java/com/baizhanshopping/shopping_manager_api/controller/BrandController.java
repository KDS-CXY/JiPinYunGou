package com.baizhanshopping.shopping_manager_api.controller;

import com.baizhanshopping.shopping_common.pojo.Brand;
import com.baizhanshopping.shopping_common.result.BaseRsult;
import com.baizhanshopping.shopping_common.service.BrandService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @DubboReference
    private BrandService brandService;
    @RequestMapping("/findById")
    public BaseRsult<Brand> findBrandById(Long id){
        return BaseRsult.success(brandService.findBrandById(id));
    }
}
