package com.baizhanshopping.shopping_category_customer_api.controller;

import com.baizhanshopping.shopping_common.pojo.Category;
import com.baizhanshopping.shopping_common.result.BaseRsult;
import com.baizhanshopping.shopping_common.service.CategoryService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/category")
public class CategoryController {
    @DubboReference
    private CategoryService categoryService;
    @GetMapping("/all")
    public BaseRsult<List<Category>> findAll(){
        return BaseRsult.success(categoryService.findAll());
    }
}
