package com.baizhanshopping.shopping_manager_api.controller;

import com.baizhanshopping.shopping_common.pojo.Role;
import com.baizhanshopping.shopping_common.result.BaseRsult;
import com.baizhanshopping.shopping_common.service.RoleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @DubboReference
    private RoleService roleService;
    /**
     * 新增角色
     * @param role 角色
     * @return 结果
     */
    @PostMapping("/add")
    public BaseRsult add(@RequestBody Role role){
        roleService.add(role);
        return BaseRsult.success();
    }
    /**
     * 修改角色
     * @param role 角色
     * @return 结果
     */
    @PutMapping("/update")
    public BaseRsult update(@RequestBody Role role){
        roleService.update(role);
        return BaseRsult.success();
    }
    /**
     * 删除角色
     * @param rid 角色id
     * @return 结果
     */
    @DeleteMapping("/delete")
    public BaseRsult delete(Long rid){
        roleService.delete(rid);
        return BaseRsult.success();
    }
    /**
     * 查询角色
     * @param rid 角色id
     * @return 结果
     */
    @GetMapping("/findById")
    public BaseRsult<Role> findById(Long rid){
        Role role = roleService.findById(rid);
        return BaseRsult.success(role);
    }
    /**
     * 分页查询
     * @param page 页码
     * @param size 每页数量
     * @return 结果
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('/role/search')")
    public BaseRsult<Page<Role>> search(int page,int size){
        Page<Role> rolePage = roleService.search(page, size);
        return BaseRsult.success(rolePage);
    }
    /**
     * 查询所有角色
     * @return 结果
     */
    @GetMapping("/findAll")
    public BaseRsult<List<Role>> findAll(){
        List<Role> all = roleService.findAll();
        return BaseRsult.success(all);
    }
    /**
     * 修改角色的权限
     * @param rid 角色id
     * @param pids 权限id数组
     * @return 结果
     */
    @PutMapping("/updatePermissionToRole")
    public BaseRsult updatePermissionToRole(Long rid, Long[] pids){
        roleService.updatePermissionToRole(rid,pids);
        return BaseRsult.success();
    }
}
