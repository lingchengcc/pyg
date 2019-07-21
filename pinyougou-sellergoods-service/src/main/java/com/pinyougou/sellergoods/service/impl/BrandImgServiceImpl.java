package com.pinyougou.sellergoods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import com.pinyougou.core.service.CoreServiceImpl;
import tk.mybatis.mapper.entity.Example;
import com.pinyougou.mapper.BrandImgMapper;
import com.pinyougou.pojo.BrandImg;
import com.pinyougou.sellergoods.service.BrandImgService;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class BrandImgServiceImpl extends CoreServiceImpl<BrandImg> implements BrandImgService {

    private BrandImgMapper brandImgMapper;

    @Autowired
    public BrandImgServiceImpl(BrandImgMapper brandImgMapper) {
        super(brandImgMapper, BrandImg.class);
        this.brandImgMapper = brandImgMapper;
    }

    @Override
    public PageInfo<BrandImg> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<BrandImg> all = brandImgMapper.selectAll();
        return new PageInfo<>(all);
    }

    @Override
    public PageInfo<BrandImg> findPage(Integer pageNo, Integer pageSize, BrandImg brandImg) {
        PageHelper.startPage(pageNo, pageSize);

        Example example = new Example(BrandImg.class);
        Example.Criteria criteria = example.createCriteria();

        if (brandImg != null) {
            if (StringUtils.isNotBlank(brandImg.getPic())) {
                criteria.andLike("pic", "%" + brandImg.getPic() + "%");
            }
            if (StringUtils.isNotBlank(brandImg.getBrand())) {
                criteria.andLike("brand", "%" + brandImg.getBrand() + "%");
            }

        }
        List<BrandImg> all = brandImgMapper.selectByExample(example);
        return new PageInfo<>(all);
    }
}
