package com.baizhanshopping.shopping_admin_service.service;

import com.baizhanshopping.shopping_admin_service.mapper.RoleMapper;
import com.baizhanshopping.shopping_common.pojo.Role;
import com.baizhanshopping.shopping_common.service.RoleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@DubboService
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public void add(Role role) {
        roleMapper.insert(role);
    }

    @Override
    public void update(Role role) {
        roleMapper.updateById(role);
    }

    @Override
    public void delete(Long id) {
        //删除角色所有权限
        roleMapper.deleteRoleAllPermission(id);
        //删除所有此角色的管理员
        roleMapper.deleteRoleAllAdmin(id);
        //删除角色
        roleMapper.deleteById(id);
    }

    @Override
    public Role findById(Long id) {
        return roleMapper.findById(id);
    }

    @Override
    public List<Role> findAll() {
        return roleMapper.selectList(null);
    }

    @Override
    public Page<Role> search(int page, int size) {
        return roleMapper.selectPage(new Page<>(page, size), null);
    }

    @Override
    public void updatePermissionToRole(Long rid, Long[] pids) {
        roleMapper.deleteRoleAllPermission(rid);
        for (Long pid : pids) {
            roleMapper.addPermissionToRole(rid,pid);
        }
    }
}
