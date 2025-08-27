package com.baizhanshopping.shopping_manager_api.controller;

import com.baizhanshopping.shopping_common.pojo.Specification;
import com.baizhanshopping.shopping_common.pojo.SpecificationOptions;
import com.baizhanshopping.shopping_common.result.BaseRsult;
import com.baizhanshopping.shopping_common.service.SpecificationService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/specification")
public class SpecificationController {
    @DubboReference
    private SpecificationService specificationService;

    /**
     * 新增商品规格
     * @param specification 商品规格
     * @return 执行结果
     */
    @PostMapping("/add")
    public BaseRsult add(@RequestBody Specification specification){
        specificationService.add(specification);
        return BaseRsult.success();
    }

    /**
     * 修改商品规格
     * @param specification 商品规格
     * @return 执行结果
     */
    @PutMapping("/update")
    public BaseRsult update(@RequestBody Specification specification){
        specificationService.update(specification);
        return BaseRsult.success();
    }

    /**
     * 删除商品规格
     * @param ids 商品规格id集合
     * @return 执行结果
     */
    @DeleteMapping("/delete")
    public BaseRsult delete(Long[] ids){
        specificationService.delete(ids);
        return BaseRsult.success();
    }

    /**
     * 根据id查询商品规格
     * @param id 商品规格id
     * @return 查询结果
     */
    @GetMapping("/findById")
    public BaseRsult findById(Long id){
        Specification specification = specificationService.findById(id);
        return BaseRsult.success(specification);
    }

    /**
     * 分页查询商品规格
     * @param page 页码
     * @param size 每页条数
     * @return 查询结果
     */
    @GetMapping("/search")
    public BaseRsult<Page<Specification>> search(int page, int size){
        Page<Specification> page1 = specificationService.search(page, size);
        return BaseRsult.success(page1);
    }

    /**
     * 查询某种商品类型下的所有规格
     * @param id 商品类型id
     * @return 查询结果
     */
    @GetMapping("/findByProductTypeId")
    public BaseRsult<List<Specification>> findByProductTypeId(Long id){
        List<Specification> specifications = specificationService.findByProductTypeId(id);
        return BaseRsult.success(specifications);
    }

    /**
     * 新增商品规格项
     * @param specificationOptions 商品规格项集合
     * @return 执行结果
     */
    @PostMapping("/addOption")
    public BaseRsult addOption(@RequestBody SpecificationOptions specificationOptions){
        specificationService.addOption(specificationOptions);
        return BaseRsult.success();
    }

    /**
     * 删除商品规格项
     * @param ids 商品规格项id集合
     * @return 执行结果
     */
    @DeleteMapping("/deleteOption")
    public BaseRsult deleteOption(Long[] ids){
        specificationService.deleteOption(ids);
        return BaseRsult.success();
    }
}
