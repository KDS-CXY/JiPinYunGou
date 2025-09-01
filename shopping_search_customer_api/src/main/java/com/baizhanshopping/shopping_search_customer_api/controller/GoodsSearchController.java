package com.baizhanshopping.shopping_search_customer_api.controller;

import com.baizhanshopping.shopping_common.pojo.GoodsDesc;
import com.baizhanshopping.shopping_common.pojo.GoodsSearchParam;
import com.baizhanshopping.shopping_common.pojo.GoodsSearchResult;
import com.baizhanshopping.shopping_common.result.BaseRsult;
import com.baizhanshopping.shopping_common.service.GoodsService;
import com.baizhanshopping.shopping_common.service.SearchService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/goodsSearch")
public class GoodsSearchController {
    @DubboReference
    private SearchService service;
    @DubboReference
    private GoodsService goodsService;
    @GetMapping("/autoSuggest")
    public BaseRsult<List<String>> autoSuggest(String keyword){
        return BaseRsult.success(service.autoSuggest(keyword));
    }
    /**
     * 搜索商品
     * @param goodsSearchParam 搜索条件
     * @return 搜索结果
     */
    @PostMapping("/search")
    public BaseRsult<GoodsSearchResult> search(@RequestBody GoodsSearchParam goodsSearchParam){
        GoodsSearchResult result = service.search(goodsSearchParam);
        return BaseRsult.success(result);
    }
    /**
     * 根据id查询商品详情
     * @param id 商品id
     * @return 商品详情
     */
    @GetMapping("findDesc")
    public BaseRsult<GoodsDesc> findDesc(Long id){
        return BaseRsult.success(goodsService.findDesc(id));
    }
}
