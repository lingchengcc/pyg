package com.pinyougou.sellergoods.service;

import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.CoreService;
import com.pinyougou.pojo.Brand;

/**
 * @author silent
 * @version 1.0
 * @date 2019/6/24 14:35
 **/
public interface BrandService extends CoreService<Brand> {
    /**
     * 分页搜索显示Brand
     *
     * @param pageNo   当前页
     * @param pageSize 每页条数
     * @param brand    搜索条件
     * @return PageInfo<Brand>
     */
    PageInfo<Brand> findBrandPage(Integer pageNo, Integer pageSize, Brand brand);
}
