package com.baizhanshopping.shopping_order_customer_api.controller;

import com.baizhanshopping.shopping_common.pojo.Address;
import com.baizhanshopping.shopping_common.pojo.Area;
import com.baizhanshopping.shopping_common.pojo.City;
import com.baizhanshopping.shopping_common.pojo.Province;
import com.baizhanshopping.shopping_common.result.BaseRsult;
import com.baizhanshopping.shopping_common.service.AddressService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/address")
public class AddressController {
    @DubboReference
    private AddressService addressService;
    /**
     * 查询所有省份
     * @return 所有省份集合
     */
    @GetMapping("/findAllProvince")
    public BaseRsult<List<Province>> findAllProvince(){
        List<Province> provinces = addressService.findAllProvince();
        return BaseRsult.success(provinces);
    }

    /**
     * 查询某省下的所有城市
     * @param provinceId 省份id
     * @return 城市集合
     */
    @GetMapping("/findCityByProvince")
    public BaseRsult<List<City>> findCityByProvince(Long provinceId){
        List<City> cities = addressService.findCityByProvince(provinceId);
        return BaseRsult.success(cities);
    }

    /**
     * 查询某市下的所有区县
     * @param cityId 城市id
     * @return 区县集合
     */
    @GetMapping("/findAreaByCity")
    public BaseRsult<List<Area>> findAreaByCity(Long cityId){
        List<Area> areas = addressService.findAreaByCity(cityId);
        return BaseRsult.success(areas);
    }

    /**
     * 新增地址
     * @param userId 用户id
     * @param address 地址对象
     * @return 执行结果
     */
    @PostMapping("/add")
    public BaseRsult add(@RequestHeader Long userId, @RequestBody Address address){
        address.setUserId(userId);
        addressService.add(address);
        return BaseRsult.success();
    }

    /**
     * 删除地址
     * @param userId 用户id
     * @param address 地址对象
     * @return 执行结果
     */
    @PutMapping("/update")
    public BaseRsult update(@RequestHeader Long userId, @RequestBody Address address){
        address.setUserId(userId);
        addressService.update(address);
        return BaseRsult.success();
    }

    /**
     * 根据id查询地址
     * @param id 地址id
     * @return 查询结果
     */
    @GetMapping("/findById")
    public BaseRsult<Address> findById(Long id){
        Address address = addressService.findById(id);
        return BaseRsult.success(address);
    }

    /**
     * 删除地址
     * @param id 地址id
     * @return 执行结果
     */
    @DeleteMapping("/delete")
    public BaseRsult delete(Long id){
        addressService.delete(id);
        return BaseRsult.success();
    }

    /**
     * 查询登录用户的所有地址
     * @param userId 用户id
     * @return 查询结果
     */
    @GetMapping("/findByUser")
    public BaseRsult<List<Address>> findByUser(@RequestHeader Long userId){
        List<Address> addresses = addressService.findByUser(userId);
        return BaseRsult.success(addresses);
    }
}
