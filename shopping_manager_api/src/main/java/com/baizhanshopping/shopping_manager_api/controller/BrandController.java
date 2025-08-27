package com.baizhanshopping.shopping_manager_api.controller;

import com.baizhanshopping.shopping_common.pojo.Brand;
import com.baizhanshopping.shopping_common.result.BaseRsult;
import com.baizhanshopping.shopping_common.service.BrandService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @DubboReference
    private BrandService brandService;
    @GetMapping ("/findById")
    public BaseRsult<Brand> findBrandById(Long id){
        return BaseRsult.success(brandService.findBrandById(id));
    }
    @GetMapping("/all")
    public BaseRsult<List<Brand>> findAll(){
        List<Brand> all = brandService.findAll();
        return BaseRsult.success(all);
    }
    @PostMapping("/add")
    public BaseRsult add(@RequestBody Brand brand){
        brandService.add(brand);
        return BaseRsult.success();
    }
    @PutMapping("/update")
    public BaseRsult update(@RequestBody Brand brand){
        brandService.update(brand);
        return BaseRsult.success();
    }
    @DeleteMapping("/delete")
    public BaseRsult delete(Long id){
        brandService.delete(id);
        return BaseRsult.success();
    }
    @GetMapping("/search")
    public BaseRsult<Page<Brand>> search(Brand brand ,int page,int size){
        return BaseRsult.success(brandService.search(brand, page, size));
    }
}
