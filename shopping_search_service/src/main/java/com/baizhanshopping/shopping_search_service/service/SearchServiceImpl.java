package com.baizhanshopping.shopping_search_service.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.GeoHashLocation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import co.elastic.clients.elasticsearch.core.search.FieldSuggester;
import co.elastic.clients.elasticsearch.core.search.Suggester;
import co.elastic.clients.elasticsearch.core.search.Suggestion;
import co.elastic.clients.elasticsearch.indices.AnalyzeRequest;
import co.elastic.clients.elasticsearch.indices.AnalyzeResponse;
import co.elastic.clients.elasticsearch.indices.analyze.AnalyzeToken;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.ObjectBuilder;
import com.baizhanshopping.shopping_common.pojo.*;
import com.baizhanshopping.shopping_common.service.SearchService;
import com.baizhanshopping.shopping_search_service.respository.GoodsEsReponsitory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.netty.util.internal.StringUtil;
import jdk.jshell.SourceCodeAnalysis;
import lombok.SneakyThrows;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;

@Service//测试用的
@DubboService
public class SearchServiceImpl implements SearchService {
    @Autowired
    private ElasticsearchClient client;//es客户端  用于分词操作
    @Autowired
    private GoodsEsReponsitory goodsEsReponsitory;//es商品仓库  用于同步数据
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate; //es模板  用于查询
    /**
     * 分词方法
     *
     * @param text     需要分词的文本
     * @param analyzer 分词器
     * @return 分词后关键词集合
     */
    @SneakyThrows
    public List<String> analyze(String text, String analyzer) {
        //创建分词请求
        AnalyzeRequest request = AnalyzeRequest.of(a -> a.index("goods").analyzer(analyzer).text(text));
        //发送分词请求
        AnalyzeResponse response = client.indices().analyze(request);
        //处理分词结果
        List<AnalyzeToken> tokens = response.tokens();
        List<String> words = new ArrayList<>();
        tokens.stream()
                .map(AnalyzeToken::token)
                .forEach(words::add);
        return words;
    }

    @SneakyThrows
    @Override
    public List<String> autoSuggest(String keyword) {
        //1、自动补齐查询条件
        Suggester suggester = Suggester.of(s -> s.suggesters("prefix_suggstion", FieldSuggester.of(
                        fs -> fs.completion(
                                cs -> cs.skipDuplicates(true)
                                        .size(10)
                                        .field("tags")
                        )
                )).text(keyword)
        );
        //2.自动补齐查询
        SearchResponse<Map> response = client.search(s -> s.index("goods").suggest(suggester), Map.class);
        Map<String, List<Suggestion<Map>>> resultMap = response.suggest();
        List<Suggestion<Map>> prefixSuggstion = resultMap.get("prefix_suggstion");
        Suggestion<Map> mapSuggestion = prefixSuggstion.get(0);
        List<CompletionSuggestOption<Map>> options = mapSuggestion.completion().options();
        ArrayList<String> result = new ArrayList<>();
        for (CompletionSuggestOption<Map> option : options) {
            String text = option.text();
            result.add(text);
        }
        return result;
    }

    @Override
    public GoodsSearchResult search(GoodsSearchParam goodsSearchParam) {
        NativeQuery nativeQuery = buildQuery(goodsSearchParam);
        SearchHits<GoodsES> search = elasticsearchTemplate.search(nativeQuery, GoodsES.class);
        ArrayList<GoodsES> goodsES = new ArrayList<>();
        for (SearchHit<GoodsES> goodsESSearchHit : search) {
            goodsES.add(goodsESSearchHit.getContent());
        }
        Page<GoodsES> goodsESPage = new Page<>();
        goodsESPage.setCurrent(goodsSearchParam.getPage())
            .setSize(goodsSearchParam.getSize())
            .setTotal(search.getTotalHits())
            .setRecords(goodsES);
        // 4.封装结果对象
        // 4.1 查询结果
        GoodsSearchResult result = new GoodsSearchResult();
        result.setGoodsPage(goodsESPage);
        // 4.2 查询参数
        result.setGoodsSearchParam(goodsSearchParam);
        // 4.3 查询面板
        buildSearchPanel(goodsSearchParam,result);
        return result;
    }
    public void buildSearchPanel(GoodsSearchParam goodsSearchParam,GoodsSearchResult result){
        // 1.构造搜索条件
        goodsSearchParam.setPage(1);
        goodsSearchParam.setSize(20);
        goodsSearchParam.setSort(null);
        goodsSearchParam.setSortFiled(null);
        NativeQuery query = buildQuery(goodsSearchParam);
        // 2.搜索
        SearchHits<GoodsES> search = elasticsearchTemplate.search(query, GoodsES.class);
        // 3.将结果封装为List对象
        List<GoodsES> content = new ArrayList();
        for (SearchHit<GoodsES> goodsESSearchHit : search) {
            GoodsES goodsES = goodsESSearchHit.getContent();
            content.add(goodsES);
        }
        // 4.遍历集合，封装查询面板
        // 商品相关的品牌列表
        Set<String> brands = new HashSet();
        // 商品相关的类型列表
        Set<String> productTypes = new HashSet();
        // 商品相关的规格列表
        Map<String, Set<String>> specifications = new HashMap();
        for (GoodsES goodsES : content) {
            // 获取品牌
            brands.add(goodsES.getBrand());
            // 获取类型
            List<String> productType = goodsES.getProductType();
            productTypes.addAll(productType);
            // 获取规格
            Map<String, List<String>> specification = goodsES.getSpecification();
            Set<Map.Entry<String, List<String>>> entries = specification.entrySet();
            for (Map.Entry<String, List<String>> entry : entries) {
                // 规格名
                String key = entry.getKey();
                // 规格值
                List<String> value = entry.getValue();
                // 如果没有遍历出该规格，新增键值对，如果已经遍历出该规格，则向规格中添加规格项
                if (!specifications.containsKey(key)){
                    specifications.put(key,new HashSet(value));
                }else{
                    specifications.get(key).addAll(value);
                }
            }
        }
        result.setBrands(brands);
        result.setProductType(productTypes);
        result.setSpecifications(specifications);
    }

    /**
     * 构建查询条件
     * @param goodsSearchParam 搜索参数
     * @return 构建好的查询条件
     */
    public NativeQuery buildQuery(GoodsSearchParam goodsSearchParam) {
        // 1.创建复杂查询条件对象
        NativeQueryBuilder nativeQueryBuilder = new NativeQueryBuilder();
        // 1.1.创建BoolQuery查询条件
        //bool 查询允许你把多个查询条件（must、should、must_not、filter）组合在一起，就像 SQL 里 AND、OR、NOT 一样。
        BoolQuery.Builder builder = new BoolQuery.Builder();
        // 2.如果查询条件有关键词，关键词可以匹配商品名、副标题、品牌字段；否则查询所有商品
        if (!StringUtils.hasText(goodsSearchParam.getKeyword())) {
            MatchAllQuery matchAllQuery = new MatchAllQuery.Builder().build();
            builder.must(matchAllQuery._toQuery());
        } else {
            String keyword = goodsSearchParam.getKeyword();
            MultiMatchQuery keywordQuery = MultiMatchQuery.of(q -> q.query(keyword).fields("goodsName", "caption", "brand"));
            builder.must(keywordQuery._toQuery());
        }

        // 3.如果查询条件有品牌，则精准匹配品牌
        String brand = goodsSearchParam.getBrand();
        if (StringUtils.hasText(brand)) {
            TermQuery brandQuery = TermQuery.of(q -> q.field("brand").value(brand));
            builder.must(brandQuery._toQuery());
        }

        // 4.如果查询条件有价格，则匹配价格
        Double highPrice = goodsSearchParam.getHighPrice();
        Double lowPrice = goodsSearchParam.getLowPrice();
        if (highPrice != null && highPrice != 0) {
            RangeQuery lte = RangeQuery.of(q -> q.field("price").lte(JsonData.of(highPrice)));
            builder.must(lte._toQuery());
        }
        if (lowPrice != null && lowPrice != 0) {
            RangeQuery gte = RangeQuery.of(q -> q.field("price").gte(JsonData.of(lowPrice)));
            builder.must(gte._toQuery());
        }

        // 5.如果查询条件有规格项，则精准匹配规格项
        Map<String, String> specificationOptions = goodsSearchParam.getSpecificationOption();
        if (specificationOptions != null && specificationOptions.size() > 0) {
            Set<Map.Entry<String, String>> entries = specificationOptions.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (StringUtils.hasText(key)) {
                    TermQuery termQuery = TermQuery.of(q -> q.field("specification." + key + ".keyword").value(value));
                    builder.must(termQuery._toQuery());
                }
            }
        }
        nativeQueryBuilder.withQuery(builder.build()._toQuery());

        // 6.添加分页条件
        PageRequest pageable = PageRequest.of(goodsSearchParam.getPage() - 1, goodsSearchParam.getSize());
        nativeQueryBuilder.withPageable(pageable);

        // 7.如果查询条件有排序，则添加排序条件
        String sortFiled = goodsSearchParam.getSortFiled();
        String sort = goodsSearchParam.getSort();
        if (StringUtils.hasText(sort) && StringUtils.hasText(sortFiled)) {
            Sort sortParam = null;
            // 新品的正序是id的倒序
            if (sortFiled.equals("NEW")) {
                if (sort.equals("ASC")) {
                    sortParam = Sort.by(Sort.Direction.DESC, "id");
                }
                if (sort.equals("DESC")) {
                    sortParam = Sort.by(Sort.Direction.ASC, "id");
                }
            }
            if (sortFiled.equals("PRICE")) {
                if (sort.equals("ASC")) {
                    sortParam = Sort.by(Sort.Direction.ASC, "price");
                }
                if (sort.equals("DESC")) {
                    sortParam = Sort.by(Sort.Direction.DESC, "price");
                }
            }
            nativeQueryBuilder.withSort(sortParam);
        }
        // 8.返回查询条件对象
        return nativeQueryBuilder.build();
    }

    // 向ES同步商品数据
    @Override
    public void syncGoodsToES(GoodsDesc goodsDesc) {
        // 将商品详情对象转为GoodsES对象
        GoodsES goodsES = new GoodsES();
        goodsES.setId(goodsDesc.getId());
        goodsES.setGoodsName(goodsDesc.getGoodsName());
        goodsES.setCaption(goodsDesc.getCaption());
        goodsES.setPrice(goodsDesc.getPrice());
        goodsES.setHeaderPic(goodsDesc.getHeaderPic());
        goodsES.setBrand(goodsDesc.getBrand().getName());
        // 类型集合
        List<String> productType = new ArrayList();
        productType.add(goodsDesc.getProductType1().getName());
        productType.add(goodsDesc.getProductType2().getName());
        productType.add(goodsDesc.getProductType3().getName());
        goodsES.setProductType(productType);
        // 规格集合
        Map<String, List<String>> map = new HashMap();
        List<Specification> specifications = goodsDesc.getSpecifications();
        // 遍历规格
        for (Specification specification : specifications) {
            // 规格项集合
            List<SpecificationOption> options = specification.getSpecificationOptions();
            // 规格项名集合
            List<String> optionStrList = new ArrayList();
            for (SpecificationOption option : options) {
                optionStrList.add(option.getOptionName());
            }
            map.put(specification.getSpecName(), optionStrList);
        }
        goodsES.setSpecification(map);
        // 关键字
        List<String> tags = new ArrayList();
        tags.add(goodsDesc.getBrand().getName()); //品牌名是关键字
        tags.addAll(analyze(goodsDesc.getGoodsName(), "ik_smart"));//商品名分词后为关键词
        tags.addAll(analyze(goodsDesc.getCaption(), "ik_smart"));//副标题分词后为关键词
        goodsES.setTags(tags);

        // 将GoodsES对象存入ES
        goodsEsReponsitory.save(goodsES);
    }

    @Override
    public void delete(Long id) {
        goodsEsReponsitory.deleteById(id);
    }
}
