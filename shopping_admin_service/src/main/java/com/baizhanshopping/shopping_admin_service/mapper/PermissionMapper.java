package com.baizhanshopping.shopping_admin_service.mapper;

import com.baizhanshopping.shopping_common.pojo.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface PermissionMapper extends BaseMapper<Permission> {
    // 删除角色_权限表中的相关数据
    void deletePermissionAllRole(Long pid);
}
