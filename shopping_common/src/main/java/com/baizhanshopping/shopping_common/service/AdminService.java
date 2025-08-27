package com.baizhanshopping.shopping_common.service;

import com.baizhanshopping.shopping_common.pojo.Admin;
import com.baizhanshopping.shopping_common.pojo.Permission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface AdminService {
    void addAdmin(Admin admin);
    void updateAdmin(Admin admin);
    void deleteById(Long id);
    Admin findById(Long id);
    Page<Admin> search(int page,int size);
    void updateRoleToAdmin(Long aid,Long[]rids);
    // 根据用户名查询管理员
    Admin findByAdminName(String username);
    // 根据用户名查询所有权限
    List<Permission> findAllPermission(String username);

}
