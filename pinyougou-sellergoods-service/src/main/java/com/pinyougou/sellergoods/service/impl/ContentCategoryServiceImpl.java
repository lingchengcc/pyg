package com.pinyougou.sellergoods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import com.pinyougou.core.service.CoreServiceImpl;
import tk.mybatis.mapper.entity.Example;
import com.pinyougou.mapper.ContentCategoryMapper;
import com.pinyougou.pojo.ContentCategory;
import com.pinyougou.sellergoods.service.ContentCategoryService;


/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class ContentCategoryServiceImpl extends CoreServiceImpl<ContentCategory> implements ContentCategoryService {


    private ContentCategoryMapper contentCategoryMapper;

    @Autowired
    public ContentCategoryServiceImpl(ContentCategoryMapper contentCategoryMapper) {
        super(contentCategoryMapper, ContentCategory.class);
        this.contentCategoryMapper = contentCategoryMapper;
    }

    @Override
    public PageInfo<ContentCategory> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<ContentCategory> all = contentCategoryMapper.selectAll();
        return new PageInfo<>(all);
    }

    @Override
    public PageInfo<ContentCategory> findPage(Integer pageNo, Integer pageSize, ContentCategory contentCategory) {
        PageHelper.startPage(pageNo, pageSize);

        Example example = new Example(ContentCategory.class);
        Example.Criteria criteria = example.createCriteria();

        if (contentCategory != null) {
            if (StringUtils.isNotBlank(contentCategory.getName())) {
                criteria.andLike("name", "%" + contentCategory.getName() + "%");
            }
            if (StringUtils.isNotBlank(contentCategory.getContentGroup())) {
                criteria.andLike("contentGroup", "%" + contentCategory.getContentGroup() + "%");
            }
            if (StringUtils.isNotBlank(contentCategory.getContentKey())) {
                criteria.andLike("contentKey", "%" + contentCategory.getContentKey() + "%");
            }
            if (StringUtils.isNotBlank(contentCategory.getStatus())) {
                criteria.andLike("status", "%" + contentCategory.getStatus() + "%");
            }
        }
        List<ContentCategory> all = contentCategoryMapper.selectByExample(example);
        return new PageInfo<>(all);
    }

}
