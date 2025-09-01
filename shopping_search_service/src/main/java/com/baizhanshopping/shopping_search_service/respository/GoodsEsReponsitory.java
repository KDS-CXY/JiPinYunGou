package com.baizhanshopping.shopping_search_service.respository;

import com.baizhanshopping.shopping_common.pojo.GoodsES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsEsReponsitory extends ElasticsearchRepository<GoodsES,Long> {
}
