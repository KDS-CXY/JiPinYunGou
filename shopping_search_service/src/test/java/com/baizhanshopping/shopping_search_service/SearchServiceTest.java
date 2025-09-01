package com.baizhanshopping.shopping_search_service;

import com.baizhanshopping.shopping_search_service.service.SearchServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SearchServiceTest {
    @Autowired
    private SearchServiceImpl service;
//    @DubboReference
//    private GoodsService goodsService;
    @Test
    public void testfenci(){
        List<String> analyze = service.analyze("我的爹是陈向阳", "ik_pinyin");
        analyze.forEach(System.out::println);
    }
//    @Test
//    public void  tongbu(){
//        List<GoodsDesc> all = goodsService.findAll();
//        for (GoodsDesc goodsDesc : all) {
//            if(goodsDesc.getIsMarketable()){
//                service.syncGoodsToES(goodsDesc);
//            }
//        }
//    }

}
