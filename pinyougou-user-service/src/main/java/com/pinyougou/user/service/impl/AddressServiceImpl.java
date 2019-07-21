package com.pinyougou.user.service.impl;

import java.util.List;

import com.pinyougou.user.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import com.pinyougou.core.service.CoreServiceImpl;
import tk.mybatis.mapper.entity.Example;
import com.pinyougou.mapper.AddressMapper;
import com.pinyougou.pojo.Address;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class AddressServiceImpl extends CoreServiceImpl<Address> implements AddressService {

    private AddressMapper addressMapper;

    @Autowired
    public AddressServiceImpl(AddressMapper addressMapper) {
        super(addressMapper, Address.class);
        this.addressMapper = addressMapper;
    }

    @Override
    public PageInfo<Address> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<Address> all = addressMapper.selectAll();
        return new PageInfo<>(all);
    }

    @Override
    public PageInfo<Address> findPage(Integer pageNo, Integer pageSize, Address address) {
        PageHelper.startPage(pageNo, pageSize);

        Example example = new Example(Address.class);
        Example.Criteria criteria = example.createCriteria();

        if (address != null) {
            if (StringUtils.isNotBlank(address.getUserId())) {
                criteria.andLike("userId", "%" + address.getUserId() + "%");
            }
            if (StringUtils.isNotBlank(address.getProvinceId())) {
                criteria.andLike("provinceId", "%" + address.getProvinceId() + "%");
            }
            if (StringUtils.isNotBlank(address.getCityId())) {
                criteria.andLike("cityId", "%" + address.getCityId() + "%");
            }
            if (StringUtils.isNotBlank(address.getTownId())) {
                criteria.andLike("townId", "%" + address.getTownId() + "%");
            }
            if (StringUtils.isNotBlank(address.getMobile())) {
                criteria.andLike("mobile", "%" + address.getMobile() + "%");
            }
            if (StringUtils.isNotBlank(address.getAddress())) {
                criteria.andLike("address", "%" + address.getAddress() + "%");
            }
            if (StringUtils.isNotBlank(address.getContact())) {
                criteria.andLike("contact", "%" + address.getContact() + "%");
            }
            if (StringUtils.isNotBlank(address.getIsDefault())) {
                criteria.andLike("isDefault", "%" + address.getIsDefault() + "%");
            }
            if (StringUtils.isNotBlank(address.getNotes())) {
                criteria.andLike("notes", "%" + address.getNotes() + "%");
            }
            if (StringUtils.isNotBlank(address.getAlias())) {
                criteria.andLike("alias", "%" + address.getAlias() + "%");
            }

        }
        List<Address> all = addressMapper.selectByExample(example);
        return new PageInfo<>(all);
    }

}
