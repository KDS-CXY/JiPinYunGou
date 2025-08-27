package com.baizhanshopping.shopping_admin_service.service;

import com.baizhanshopping.shopping_admin_service.mapper.PermissionMapper;
import com.baizhanshopping.shopping_common.pojo.Permission;
import com.baizhanshopping.shopping_common.service.PermissionService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Override
    public void add(Permission permission) {
        permissionMapper.insert(permission);
    }

    @Override
    public void update(Permission permission) {
        permissionMapper.updateById(permission);
    }

    @Override
    public void delete(Long id) {
        //删除权限和角色中间表数据
        permissionMapper.deletePermissionAllRole(id);
        //删除权限
        permissionMapper.deleteById(id);
    }

    @Override
    public Permission findById(Long id) {
        return permissionMapper.selectById(id);
    }

    @Override
    public Page<Permission> search(int page, int size) {
        return permissionMapper.selectPage(new Page<Permission>(page,size),null);
    }

    @Override
    public List<Permission> findAll() {
        return permissionMapper.selectList(null);
    }
}
