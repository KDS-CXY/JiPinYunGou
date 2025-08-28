package com.baizhanshopping.shopping_manager_api.controller;

import com.baizhanshopping.shopping_common.pojo.Category;
import com.baizhanshopping.shopping_common.result.BaseRsult;
import com.baizhanshopping.shopping_common.service.CategoryService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @DubboReference
    private CategoryService categoryService;

    /**
     * 分页查询广告
     *
     * @param page 页码
     * @param size 每页条数
     * @return 查询结果
     */
    @GetMapping("/search")
    public BaseRsult<Page<Category>> search(int page, int size) {
        Page<Category> page1 = categoryService.search(page, size);
        return BaseRsult.success(page1);
    }

    /**
     * 增加广告
     *
     * @param category 广告对象
     * @return 操作结果
     */
    @PostMapping("/add")
    public BaseRsult add(@RequestBody Category category) {
        categoryService.add(category);
        return BaseRsult.success();
    }

    /**
     * 修改广告
     *
     * @param category 广告对象
     * @return 操作结果
     */
    @PutMapping("/update")
    public BaseRsult update(@RequestBody Category category) {
        categoryService.update(category);
        return BaseRsult.success();
    }

    /**
     * 修改广告状态
     *
     * @param id   广告id
     * @param status 广告状态 0:未启用 1:启用
     * @return 操作结果
     */
    @PutMapping("/updateStatus")
    public BaseRsult updateStatus(Long id, Integer status) {
        categoryService.updateStatus(id,status);
        return BaseRsult.success();
    }

    /**
     * 根据Id查询广告
     *
     * @param id 广告id
     * @return 查询结果
     */
    @GetMapping("/findById")
    public BaseRsult<Category> findById(Long id) {
        Category category = categoryService.findById(id);
        return BaseRsult.success(category);
    }

    /**
     * 删除广告
     *
     * @param ids 广告id集合
     * @return 操作结果
     */
    @DeleteMapping("/delete")
    public BaseRsult delete(Long[] ids) {
        categoryService.delete(ids);
        return BaseRsult.success();
    }
}
