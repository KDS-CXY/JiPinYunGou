package com.baizhanshopping.shopping_common.service;

import com.baizhanshopping.shopping_common.pojo.Admin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface AdminService {
    void addAdmin(Admin admin);
    void updateAdmin(Admin admin);
    void deleteById(Long id);
    Admin findById(Long id);
    Page<Admin> search(int page,int size);
    void updateRoleToAdmin(Long aid,Long[]rids);
}
