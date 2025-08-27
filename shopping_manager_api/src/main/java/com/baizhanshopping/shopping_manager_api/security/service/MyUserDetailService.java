package com.baizhanshopping.shopping_manager_api.security.service;

import com.baizhanshopping.shopping_common.pojo.Admin;
import com.baizhanshopping.shopping_common.pojo.Permission;
import com.baizhanshopping.shopping_common.service.AdminService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailService implements UserDetailsService {
    @DubboReference
    private AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminService.findByAdminName(username);//根据用户名查询管理员
        if(admin==null){
            throw new UsernameNotFoundException("用户不存在");
        }
        List<Permission> allPermission = adminService.findAllPermission(username);//查询所有权限
        List<GrantedAuthority> grantedAuthorities=new ArrayList<>();//权限列表
        if(allPermission.get(0)!=null){
            //遍历所有权限
            allPermission.forEach(permission -> grantedAuthorities.add(new SimpleGrantedAuthority(permission.getUrl())));
        }
        UserDetails userDetails = User.withUsername(admin.getUsername())//用户名
                .password(admin.getPassword())//密码
                .authorities(grantedAuthorities)
                .build();//构建用户
        return userDetails;
    }
}
