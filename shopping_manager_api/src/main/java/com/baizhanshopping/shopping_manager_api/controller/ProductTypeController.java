package com.baizhanshopping.shopping_manager_api.controller;

import com.baizhanshopping.shopping_common.pojo.ProductType;
import com.baizhanshopping.shopping_common.result.BaseRsult;
import com.baizhanshopping.shopping_common.service.ProductTypeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productType")
public class ProductTypeController {
    @DubboReference
    private ProductTypeService productTypeService;

    /**
     * 新增商品类型
     * @param productType 商品类型
     * @return 执行结果
     */
    @PostMapping("/add")
    public BaseRsult add(@RequestBody ProductType productType){
        productTypeService.add(productType);
        return BaseRsult.success();
    }

    /**
     * 修改商品类型
     * @param productType 商品类型
     * @return 执行结果
     */
    @PutMapping("/update")
    public BaseRsult update(@RequestBody ProductType productType){
        productTypeService.update(productType);
        return BaseRsult.success();
    }

    /**
     * 删除商品类型
     * @param id 商品类型id
     * @return 执行结果
     */
    @DeleteMapping("/delete")
    public BaseRsult delete(Long id){
        productTypeService.delete(id);
        return BaseRsult.success();
    }

    /**
     * 根据id查询商品类型
     * @param id 商品类型id
     * @return 查询结果
     */
    @GetMapping("/findById")
    public BaseRsult<ProductType> findById(Long id){
        ProductType productType = productTypeService.findById(id);
        return BaseRsult.success(productType);
    }


    /**
     * 分页查询商品类型
     * @param productType 查询条件对象
     * @param page 页码
     * @param size 每页条数
     * @return 查询结果
     */
    @GetMapping("/search")
    public BaseRsult<Page<ProductType>> search(ProductType productType, int page, int size){
        Page<ProductType> page1 = productTypeService.search(productType, page, size);
        return BaseRsult.success(page1);
    }

    /**
     * 查询商品类型列表
     * @param productType 查询条件对象
     * @return 查询结果
     */
    @GetMapping("/findProductType")
    public BaseRsult<List<ProductType>> findProductType(ProductType productType){
        List<ProductType> productType1 = productTypeService.findProductType(productType);
        return BaseRsult.success(productType1);
    }

    /**
     * 根据父类型id查询商品类型列表
     * @param parentId 父类型id
     * @return 查询结果
     */
    @GetMapping("/findByParentId")
    public BaseRsult<List<ProductType>> findByParentId(Long parentId){
        ProductType productType = new ProductType();
        productType.setParentId(parentId);
        List<ProductType> productType1 = productTypeService.findProductType(productType);
        return BaseRsult.success(productType1);
    }
}
