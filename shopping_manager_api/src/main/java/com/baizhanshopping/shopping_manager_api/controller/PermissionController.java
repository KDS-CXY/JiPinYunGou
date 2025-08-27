package com.baizhanshopping.shopping_manager_api.controller;

import com.baizhanshopping.shopping_common.pojo.Permission;
import com.baizhanshopping.shopping_common.result.BaseRsult;
import com.baizhanshopping.shopping_common.service.PermissionService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {
    @DubboReference
    private PermissionService permissionService;

    /**
     * 新增权限
     * @param permission 权限信息
     * @return 新增成功
     */
    @PostMapping("/add")
    public BaseRsult add(@RequestBody Permission permission){
        permissionService.add(permission);
        return BaseRsult.success();
    }
    /**
     * 修改权限
     * @param permission 权限信息
     * @return 修改成功
     */
    @PutMapping("/update")
    public BaseRsult update(@RequestBody Permission permission){
        permissionService.update(permission);
        return BaseRsult.success();
    }
    /**
     * 删除权限
     * @param pid 权限id
     * @return 删除成功
     */
    @DeleteMapping("/delete")
    public BaseRsult delete(Long pid){
        permissionService.delete(pid);
        return BaseRsult.success();
    }
    /**
     * 根据id查询权限
     * @param pid 权限id
     * @return
     */
    @GetMapping("/findById")
    public BaseRsult<Permission> findById(Long pid){
        Permission permission = permissionService.findById(pid);
        return BaseRsult.success(permission);
    }
    /**
     * 分页查询权限
     * @param page 页码
     * @param size 每页条数
     * @return 分页查询结果
     */
    @GetMapping("/search")
    public BaseRsult<Page<Permission>> search(int page,int size){
        Page<Permission> permissionPage = permissionService.search(page, size);
        return BaseRsult.success(permissionPage);
    }
    /**
     * 查询所有权限
     * @return 所有权限列表
     */
    @GetMapping("/findAll")
    public BaseRsult<List<Permission>> findAll(){
        List<Permission> permissionServiceAll = permissionService.findAll();
        return BaseRsult.success(permissionServiceAll);
    }
}
