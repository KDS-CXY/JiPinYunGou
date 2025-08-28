package com.baizhanshopping.shopping_manager_api.controller;

import com.baizhanshopping.shopping_common.pojo.Goods;
import com.baizhanshopping.shopping_common.result.BaseRsult;
import com.baizhanshopping.shopping_common.service.GoodsService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @DubboReference
    private GoodsService goodsService;

    /**
     * 新增商品
     *
     * @param goods 商品实体
     * @return 执行结果
     */
    @PostMapping("/add")
    public BaseRsult add(@RequestBody Goods goods) {
        goodsService.add(goods);
        return BaseRsult.success();
    }
    /**
     * 修改商品
     *
     * @param goods 商品实体
     * @return 执行结果
     */
    @PutMapping("/update")
    public BaseRsult update(@RequestBody Goods goods) {
        goodsService.update(goods);
        return BaseRsult.success();
    }
    /**
     * 上架/下架商品
     *
     * @param id 商品id
     * @param isMarketable 是否上架
     * @return 执行结果
     */
    @PutMapping("/putAway")
    public BaseRsult putAway(Long id,Boolean isMarketable) {
        goodsService.putAway(id,isMarketable);
        return BaseRsult.success();
    }
    /**
     * 根据id查询商品详情
     *
     * @param id 商品id
     * @return 商品详情
     */
    @GetMapping("/findById")
    public BaseRsult<Goods> findById(Long id) {
        Goods goods = goodsService.findById(id);
        return BaseRsult.success(goods);
    }
    /**
     * 分页查询
     * @param goods 商品条件对象
     * @param page 页码
     * @param size 每页条数
     * @return 查询结果
     */
    @GetMapping("/search")
    public BaseRsult<Page<Goods>> search(Goods goods, int page, int size) {
        Page<Goods> page1 = goodsService.search(goods, page, size);
        return BaseRsult.success(page1);
    }


}
