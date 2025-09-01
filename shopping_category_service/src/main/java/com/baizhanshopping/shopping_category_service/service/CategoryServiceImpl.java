package com.baizhanshopping.shopping_category_service.service;

import com.baizhanshopping.shopping_category_service.mapper.CategoryMapper;
import com.baizhanshopping.shopping_common.pojo.Category;
import com.baizhanshopping.shopping_common.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@DubboService
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void add(Category category) {
        categoryMapper.insert(category);
        //刷新Redis中的缓存
        refreshRedisCategory();
    }

    @Override
    public void update(Category category) {
        categoryMapper.updateById(category);
        //刷新Redis中的缓存
        refreshRedisCategory();
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Category category = categoryMapper.selectById(id);
        category.setStatus(status);
        categoryMapper.updateById(category);
        //刷新Redis中的缓存
        refreshRedisCategory();
    }

    @Override
    public void delete(Long[] ids) {
        categoryMapper.deleteBatchIds(Arrays.asList(ids));
        //刷新Redis中的缓存
        refreshRedisCategory();
    }

    @Override
    public Category findById(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public Page<Category> search(int page, int size) {
        return categoryMapper.selectPage(new Page<Category>(page,size),null);
    }
    //返回所有启用的category
    @Override
    public List<Category> findAll() {
        ListOperations<String,Category> listOperations = redisTemplate.opsForList();
        List<Category> categorys = listOperations.range("categorys", 0, -1);
        if(categorys!=null && categorys.size()>0){
            System.out.println("从redis中查找");
            return categorys;
        }else {
            QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
            categoryQueryWrapper.eq("status",1);
            List<Category> categories = categoryMapper.selectList(categoryQueryWrapper);
            listOperations.leftPushAll("categorys",categories);
            System.out.println("从mysql中查找");
            return categories;
        }
    }
    public void refreshRedisCategory(){
        //从数据库查询出来数据
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.eq("status",1);
        List<Category> categories = categoryMapper.selectList(categoryQueryWrapper);
        //删除Redis中的缓存
        redisTemplate.delete("categorys");
        //重新将查询的数据存入Redis中
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.leftPushAll("categorys",categories);
    }
}
