package com.baizhanshopping.shopping_manager_api.controller;

import com.baizhanshopping.shopping_common.pojo.SeckillGoods;
import com.baizhanshopping.shopping_common.result.BaseRsult;
import com.baizhanshopping.shopping_common.service.SeckillGoodsService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {
    @DubboReference
    private SeckillGoodsService seckillGoodsService;

    /**
     * 添加秒杀商品
     * @param seckillGoods 秒杀商品实体
     * @return 操作结果
     */
    @PostMapping("/add")
    public BaseRsult add(@RequestBody SeckillGoods seckillGoods) {
        seckillGoodsService.add(seckillGoods);
        return BaseRsult.success();
    }

    /**
     * 修改秒杀商品
     * @param seckillGoods 秒杀商品实体
     * @return 操作结果
     */
    @PutMapping("/update")
    public BaseRsult update(@RequestBody SeckillGoods seckillGoods) {
        seckillGoodsService.update(seckillGoods);
        return BaseRsult.success();
    }

    /**
     * 分页查询秒杀商品
     * @param page 页数
     * @param size 每页条数
     * @return 查询结果
     */
    @GetMapping("/findPage")
    public BaseRsult<Page<SeckillGoods>> findPage(int page, int size) {
        Page<SeckillGoods> seckillGoodsPage = seckillGoodsService.findPage(page, size);
        return BaseRsult.success(seckillGoodsPage);
    }
}
