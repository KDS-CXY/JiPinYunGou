package com.baizhanshopping.shopping_manager_api.controller;

import com.baizhanshopping.shopping_common.pojo.Admin;
import com.baizhanshopping.shopping_common.result.BaseRsult;
import com.baizhanshopping.shopping_common.service.AdminService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.java.Log;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @DubboReference
    private AdminService adminService;

    /**
     * 添加管理员
     * @param admin 前端传过来的admin参数
     * @return 返回BaseRsult统一结果集
     */
    @PostMapping("/add")
    public BaseRsult add(@RequestBody Admin admin){
        adminService.addAdmin(admin);
        return BaseRsult.success();
    }

    /**
     * 更新管理员
     * @param admin 前端传过来的admin参数
     * @return 返回BaseRsult统一结果集
     */
    @PostMapping("/update")
    public BaseRsult update(@RequestBody Admin admin){
        adminService.updateAdmin(admin);
        return BaseRsult.success();
    }

    /**
     * 删除管理员
     * @param aid 需要删除的管理员id
     * @return 返回BaseRsult统一结果集
     */
    @DeleteMapping("/delete")
    public BaseRsult delete(Long aid){
        adminService.deleteById(aid);
        return BaseRsult.success();
    }

    /**
     * 通过id查找管理员
     * @param aid 需要查找的管理员的id
     * @return 返回BaseRsult统一结果集
     */
    @GetMapping("/findById")
    public BaseRsult<Admin> findById(Long aid){
        Admin admin = adminService.findById(aid);
        return BaseRsult.success(admin);
    }

    /**
     * 分页查找全部管理员信息
     * @param page 第几页
     * @param size 一页几条
     * @return 返回BaseRsult统一结果集
     */
    //分页查询别忘了配置分页插件
    @GetMapping("/search")
    public BaseRsult<Page<Admin>> search(int page,int size){
        Page<Admin> adminPage = adminService.search(page, size);
        return BaseRsult.success(adminPage);
    }

    /**
     * 根据id更新管理员角色
     * @param aid 需要更新的管理员id
     * @param rids 更新的角色id数组
     * @return 返回BaseRsult统一结果集
     */
    @PostMapping("/updateRoleToAdmin")
    public BaseRsult updateRoleToAdmin(Long aid,Long[] rids){
        adminService.updateRoleToAdmin(aid,rids);
        return BaseRsult.success();
    }
}
