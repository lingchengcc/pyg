package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.CoreServiceImpl;
import com.pinyougou.mapper.BrandMapper;
import com.pinyougou.pojo.Brand;
import com.pinyougou.sellergoods.service.BrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;

/**
 * @author silent
 * @version 1.0
 * @date 2019/6/24 14:38
 **/
@Service
public class BrandServiceImpl extends CoreServiceImpl<Brand> implements BrandService {

    private BrandMapper brandMapper;

    @Autowired
    public BrandServiceImpl(BrandMapper brandMapper) {
        super(brandMapper, Brand.class);
        this.brandMapper = brandMapper;
    }

    @Override
    public PageInfo<Brand> findBrandPage(Integer pageNo, Integer pageSize, Brand brand) {
        if (brand == null) {
            PageHelper.startPage(pageNo, pageSize);
            List<Brand> brands = brandMapper.selectAll();
            return new PageInfo<>(brands);
        }
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNoneBlank(brand.getName())) {
            criteria.andLike("name", "%" + brand.getName() + "%");
        }
        if (StringUtils.isNoneBlank(brand.getFirstChar())) {
            criteria.andEqualTo("firstChar", brand.getFirstChar());
        }
        PageHelper.startPage(pageNo, pageSize);
        List<Brand> brands = brandMapper.selectByExample(example);
        return new PageInfo<>(brands);
    }
}