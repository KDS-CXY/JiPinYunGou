package com.baizhanshopping.shopping_admin_service.mapper;

import com.baizhanshopping.shopping_common.pojo.Admin;
import com.baizhanshopping.shopping_common.pojo.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.extern.java.Log;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminMapper extends BaseMapper<Admin> {
    void deleteAdminAllRole(Long id);
    Admin findByAid(Long id);
    void addRoleToAdmin(@Param("aid") Long aid, @Param("rid") Long rid);
    // 根据管理员名查询权限
    List<Permission> findAllPermission(String username);

}
