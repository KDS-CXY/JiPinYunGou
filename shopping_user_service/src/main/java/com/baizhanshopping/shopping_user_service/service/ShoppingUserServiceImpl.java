package com.baizhanshopping.shopping_user_service.service;

import com.baizhanshopping.shopping_common.pojo.ShoppingUser;
import com.baizhanshopping.shopping_common.result.BusException;
import com.baizhanshopping.shopping_common.result.CodeEnum;
import com.baizhanshopping.shopping_common.service.ShoppingUserService;
import com.baizhanshopping.shopping_common.util.Md5Util;
import com.baizhanshopping.shopping_user_service.mapper.ShoppingUserMapper;
import com.baizhanshopping.shopping_user_service.util.JwtUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@DubboService
public class ShoppingUserServiceImpl implements ShoppingUserService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ShoppingUserMapper shoppingUserMapper;

    @Override
    public void saveRegisterCheckCode(String phone, String checkCode) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // redis键为手机号，值为验证码，过期时间5分钟
        valueOperations.set("registerCode:" + phone, checkCode, 300, TimeUnit.SECONDS);
    }

    @Override
    public void registerCheckCode(String phone, String checkCode) {
        // 验证验证码
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object checkCodeRedis = valueOperations.get("registerCode:" + phone);
        if (!checkCode.equals(checkCodeRedis)||checkCodeRedis==null) {
            throw new BusException(CodeEnum.REGISTER_CODE_ERROR);
        }
    }

    @Override
    public void register(ShoppingUser shoppingUser) {
        QueryWrapper<ShoppingUser> phoneQw = new QueryWrapper<>();
        phoneQw.eq("phone",shoppingUser.getPhone());
        List<ShoppingUser> shoppingUsers = shoppingUserMapper.selectList(phoneQw);
        if(shoppingUsers!=null&&shoppingUsers.size()>0){
            throw new BusException(CodeEnum.REGISTER_REPEAT_PHONE_ERROR);
        }
        QueryWrapper<ShoppingUser> nameQw = new QueryWrapper<>();
        nameQw.eq("name",shoppingUser.getName());
        List<ShoppingUser> shoppingUsers1 = shoppingUserMapper.selectList(nameQw);
        if(shoppingUsers1!=null&&shoppingUsers1.size()>0){
            throw new BusException(CodeEnum.REGISTER_REPEAT_NAME_ERROR);
        }
        shoppingUser.setPassword(Md5Util.encode(shoppingUser.getPassword()));
        shoppingUser.setStatus("Y");
        shoppingUserMapper.insert(shoppingUser);
    }

    @Override
    public String loginPassword(String username, String password) {
        QueryWrapper<ShoppingUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        ShoppingUser shoppingUser = shoppingUserMapper.selectOne(queryWrapper);
        // 验证用户名
        if (shoppingUser == null) {
            throw new BusException(CodeEnum.LOGIN_NAME_PASSWORD_ERROR);
        }
        // 验证密码
        boolean verify = Md5Util.verify(password, shoppingUser.getPassword());
        if (!verify) {
            throw new BusException(CodeEnum.LOGIN_NAME_PASSWORD_ERROR);
        }
        String token = JwtUtils.sign(shoppingUser.getId(), shoppingUser.getUsername());
        return token;
    }

    @Override
    public void saveLoginCheckCode(String phone, String checkCode) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // redis键为手机号，值为验证码，过期时间5分钟
        valueOperations.set("loginCode:" + phone, checkCode, 300, TimeUnit.SECONDS);
    }

    @Override
    public String loginCheckCode(String phone, String checkCode) {
        // 验证用户传入的手机号验证码是否在redis中存在
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object checkCodeRedis = valueOperations.get("loginCode:" + phone);
        if (!checkCode.equals(checkCodeRedis)) {
            throw new BusException(CodeEnum.LOGIN_CODE_ERROR);
        }
        // 登录成功，查询用户
        QueryWrapper<ShoppingUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", phone);
        ShoppingUser shoppingUser = shoppingUserMapper.selectOne(queryWrapper);
        // 返回用户名
        String token = JwtUtils.sign(shoppingUser.getId(), shoppingUser.getUsername());
        return token;
    }

    @Override
    public String getName(String token) {
        Map<String, Object> map = JwtUtils.verify(token);
        String username = (String)map.get("username");
        return username;
    }

    @Override
    public ShoppingUser getLoginUser(String token) {
        // 从令牌中获取用户id
        Map<String, Object> map = JwtUtils.verify(token);
        Long userId = (Long) map.get("userId");
        // 根据id查询用户
        QueryWrapper<ShoppingUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", userId);
        ShoppingUser shoppingUser = shoppingUserMapper.selectOne(queryWrapper);
        return shoppingUser;
    }

    @Override
    public void checkPhone(String phone) {
        // 1.判断手机号是否存在
        QueryWrapper<ShoppingUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone",phone);
        ShoppingUser shoppingUser = shoppingUserMapper.selectOne(queryWrapper);
        if (shoppingUser == null){
            throw new BusException(CodeEnum.LOGIN_NOPHONE_ERROR);
        }
        // 2.判断用户状态
        if(!"Y".equals(shoppingUser.getStatus())){
            throw new BusException(CodeEnum.LOGIN_USER_STATUS_ERROR);
        }
    }
}
