package com.pinyougou.sellergoods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import com.pinyougou.core.service.CoreServiceImpl;
import tk.mybatis.mapper.entity.Example;
import com.pinyougou.mapper.GoodsDescMapper;
import com.pinyougou.pojo.GoodsDesc;
import com.pinyougou.sellergoods.service.GoodsDescService;


/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class GoodsDescServiceImpl extends CoreServiceImpl<GoodsDesc> implements GoodsDescService {


    private GoodsDescMapper goodsDescMapper;

    @Autowired
    public GoodsDescServiceImpl(GoodsDescMapper goodsDescMapper) {
        super(goodsDescMapper, GoodsDesc.class);
        this.goodsDescMapper = goodsDescMapper;
    }


    @Override
    public PageInfo<GoodsDesc> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<GoodsDesc> all = goodsDescMapper.selectAll();
        return new PageInfo<>(all);
    }


    @Override
    public PageInfo<GoodsDesc> findPage(Integer pageNo, Integer pageSize, GoodsDesc goodsDesc) {
        PageHelper.startPage(pageNo, pageSize);

        Example example = new Example(GoodsDesc.class);
        Example.Criteria criteria = example.createCriteria();

        if (goodsDesc != null) {
            if (StringUtils.isNotBlank(goodsDesc.getIntroduction())) {
                criteria.andLike("introduction", "%" + goodsDesc.getIntroduction() + "%");
            }
            if (StringUtils.isNotBlank(goodsDesc.getSpecificationItems())) {
                criteria.andLike("specificationItems", "%" + goodsDesc.getSpecificationItems() + "%");
            }
            if (StringUtils.isNotBlank(goodsDesc.getCustomAttributeItems())) {
                criteria.andLike("customAttributeItems", "%" + goodsDesc.getCustomAttributeItems() + "%");
            }
            if (StringUtils.isNotBlank(goodsDesc.getItemImages())) {
                criteria.andLike("itemImages", "%" + goodsDesc.getItemImages() + "%");
            }
            if (StringUtils.isNotBlank(goodsDesc.getPackageList())) {
                criteria.andLike("packageList", "%" + goodsDesc.getPackageList() + "%");
            }
            if (StringUtils.isNotBlank(goodsDesc.getSaleService())) {
                criteria.andLike("saleService", "%" + goodsDesc.getSaleService() + "%");
            }

        }
        List<GoodsDesc> all = goodsDescMapper.selectByExample(example);
        return new PageInfo<>(all);
    }

}
