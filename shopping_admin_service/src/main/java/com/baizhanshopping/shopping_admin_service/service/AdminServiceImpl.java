package com.baizhanshopping.shopping_admin_service.service;

import com.baizhanshopping.shopping_admin_service.mapper.AdminMapper;
import com.baizhanshopping.shopping_common.pojo.Admin;
import com.baizhanshopping.shopping_common.pojo.Permission;
import com.baizhanshopping.shopping_common.service.AdminService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.netty.util.internal.StringUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Transactional
@DubboService
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Override
    public void addAdmin(Admin admin) {
        adminMapper.insert(admin);
    }

    @Override
    public void updateAdmin(Admin admin) {
        if(!StringUtils.hasText(admin.getPassword())){
            admin.setPassword(adminMapper.selectById(admin.getAid()).getPassword());
        }
        adminMapper.updateById(admin);
    }

    @Override
    public void deleteById(Long id) {
        adminMapper.deleteAdminAllRole(id);
        adminMapper.deleteById(id);
    }

    @Override
    public Admin findById(Long id) {
        return adminMapper.findByAid(id);
    }

    @Override
    public Page<Admin> search(int page, int size) {
        return adminMapper.selectPage(new Page<Admin>(page,size), null);
    }

    @Override
    public void updateRoleToAdmin(Long aid, Long[] rids) {
        adminMapper.deleteAdminAllRole(aid);
        for (Long rid : rids) {
            adminMapper.addRoleToAdmin(aid,rid);
        }
    }

    @Override
    public Admin findByAdminName(String username) {
        QueryWrapper<Admin> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return adminMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Permission> findAllPermission(String username) {
        return adminMapper.findAllPermission(username);
    }
}
