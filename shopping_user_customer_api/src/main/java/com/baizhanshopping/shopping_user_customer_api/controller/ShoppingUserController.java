package com.baizhanshopping.shopping_user_customer_api.controller;

import com.baizhanshopping.shopping_common.pojo.ShoppingUser;
import com.baizhanshopping.shopping_common.result.BaseRsult;
import com.baizhanshopping.shopping_common.service.MessageService;
import com.baizhanshopping.shopping_common.service.ShoppingUserService;
import com.baizhanshopping.shopping_common.util.RandomUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/shoppingUser")
public class ShoppingUserController {
    @DubboReference
    private ShoppingUserService shoppingUserService;
    @DubboReference
    private MessageService messageService;
    @GetMapping("/sendMessage")
    public BaseRsult sendMessage(String phone){
        String code = RandomUtil.buildCheckCode(4);
        System.out.println(code);
        BaseRsult baseRsult = messageService.sendMessage(phone, code);
        if(baseRsult.getCode()==200){
            shoppingUserService.saveRegisterCheckCode(phone,code);
            return BaseRsult.success();
        }else {
            return baseRsult;
        }
    }
    @GetMapping("/registerCheckCode")
    public BaseRsult register(String phone,String checkCode){
        shoppingUserService.registerCheckCode(phone,checkCode);
        return BaseRsult.success();
    }
    /**
     * 用户注册
     * @param shoppingUser 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public BaseRsult register(@RequestBody ShoppingUser shoppingUser){
        shoppingUserService.register(shoppingUser);
        return BaseRsult.success();
    }
    /**
     * 用户名密码登录
     * @param shoppingUser 用户对象
     * @return 登录结果
     */
    @PostMapping("/loginPassword")
    public BaseRsult loginPassword(@RequestBody ShoppingUser shoppingUser){
        String token = shoppingUserService.loginPassword(shoppingUser.getUsername(), shoppingUser.getPassword());
        return BaseRsult.success(token);
    }
    /**
     * 发送登录短信验证码
     * @param phone 手机号
     * @return 操作结果
     */
    @GetMapping("/sendLoginCheckCode")
    public BaseRsult sendLoginCheckCode(String phone){
        // 1.判断该用户是否已经注册，状态是否正常
        shoppingUserService.checkPhone(phone);
        // 2.生成随机四位数验证码
        String checkCode = RandomUtil.buildCheckCode(4);
        System.out.println(checkCode);
        // 3.发送短信
        BaseRsult result = messageService.sendMessage(phone, checkCode);
        // 4.发送成功，将验证码保存到redis中，发送失败，返回发送结果
        if (200 == result.getCode()){
            shoppingUserService.saveLoginCheckCode(phone,checkCode);
            return BaseRsult.success();
        }else {
            return result;
        }
    }
    /**
     * 手机号验证码登录
     * @param phone 手机号
     * @param checkCode 验证码
     * @return 登录结果
     */
    @PostMapping("/loginCheckCode")
    public BaseRsult loginCheckCode(String phone, String checkCode){
        String token = shoppingUserService.loginCheckCode(phone, checkCode);
        return BaseRsult.success(token);
    }
    /**
     * 获取登录的用户名
     * @param authorization 令牌
     * @return 用户名
     */
    @GetMapping("/getName")
    public BaseRsult<String> getName(@RequestHeader("Authorization") String authorization){
        String token = authorization.replace("Bearer ","");
        String name = shoppingUserService.getName(token);
        return BaseRsult.success(name);
    }

}
