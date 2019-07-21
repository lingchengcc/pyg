package com.pinyougou.user.service;

import com.pinyougou.pojo.Address;
import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.CoreService;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface AddressService extends CoreService<Address> {

    /**
     * 返回分页列表
     *
     * @return
     */
    PageInfo<Address> findPage(Integer pageNo, Integer pageSize);

    /**
     * 分页
     *
     * @param pageNo   当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    PageInfo<Address> findPage(Integer pageNo, Integer pageSize, Address address);

}
