package com.baizhanshopping.shopping_order_service.service;

import com.baizhanshopping.shopping_common.pojo.Address;
import com.baizhanshopping.shopping_common.pojo.Area;
import com.baizhanshopping.shopping_common.pojo.City;
import com.baizhanshopping.shopping_common.pojo.Province;
import com.baizhanshopping.shopping_common.service.AddressService;
import com.baizhanshopping.shopping_order_service.mapper.AddressMapper;
import com.baizhanshopping.shopping_order_service.mapper.AreaMapper;
import com.baizhanshopping.shopping_order_service.mapper.CityMapper;
import com.baizhanshopping.shopping_order_service.mapper.ProvinceMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@DubboService
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private ProvinceMapper provinceMapper;
    @Override
    public List<Province> findAllProvince() {
        return provinceMapper.selectList(null);
    }

    @Override
    public List<City> findCityByProvince(Long provinceId) {
        QueryWrapper<City> cityQueryWrapper = new QueryWrapper<>();
        cityQueryWrapper.eq("provinceId",provinceId);
        return cityMapper.selectList(cityQueryWrapper);
    }

    @Override
    public List<Area> findAreaByCity(Long cityId) {
        QueryWrapper<Area> areaQueryWrapper = new QueryWrapper<>();
        areaQueryWrapper.eq("cityId",cityId);
        return areaMapper.selectList(areaQueryWrapper);
    }

    @Override
    public void add(Address address) {
        addressMapper.insert(address);
    }

    @Override
    public void update(Address address) {
        addressMapper.updateById(address);
    }

    @Override
    public Address findById(Long id) {
        return addressMapper.selectById(id);
    }

    @Override
    public void delete(Long id) {
        addressMapper.deleteById(id);
    }

    @Override
    public List<Address> findByUser(Long userId) {
        QueryWrapper<Address> addressQueryWrapper = new QueryWrapper<>();
        addressQueryWrapper.eq("userId",userId);
        return addressMapper.selectList(addressQueryWrapper);
    }
}
