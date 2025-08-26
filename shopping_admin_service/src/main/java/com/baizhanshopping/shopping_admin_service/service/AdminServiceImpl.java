package com.baizhanshopping.shopping_admin_service.service;

import com.baizhanshopping.shopping_admin_service.mapper.AdminMapper;
import com.baizhanshopping.shopping_common.pojo.Admin;
import com.baizhanshopping.shopping_common.service.AdminService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
}
