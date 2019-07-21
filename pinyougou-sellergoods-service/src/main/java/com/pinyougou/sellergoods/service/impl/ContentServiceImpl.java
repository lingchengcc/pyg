package com.pinyougou.sellergoods.service.impl;

import java.util.List;

import com.pinyougou.common.entity.SysConstants;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import com.pinyougou.core.service.CoreServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;
import com.pinyougou.mapper.ContentMapper;
import com.pinyougou.pojo.Content;
import com.pinyougou.sellergoods.service.ContentService;


/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class ContentServiceImpl extends CoreServiceImpl<Content> implements ContentService {

    private ContentMapper contentMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    public ContentServiceImpl(ContentMapper contentMapper) {
        super(contentMapper, Content.class);
        this.contentMapper = contentMapper;
    }

    @Override
    public PageInfo<Content> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<Content> all = contentMapper.selectAll();
        return new PageInfo<>(all);
    }

    @Override
    public PageInfo<Content> findPage(Integer pageNo, Integer pageSize, Content content) {

        PageHelper.startPage(pageNo, pageSize);
        Example example = new Example(Content.class);
        Example.Criteria criteria = example.createCriteria();
        if (content != null) {
            if (StringUtils.isNotBlank(content.getTitle())) {
                criteria.andLike("title", "%" + content.getTitle() + "%");
            }
            if (StringUtils.isNotBlank(content.getUrl())) {
                criteria.andLike("url", "%" + content.getUrl() + "%");
            }
            if (StringUtils.isNotBlank(content.getPic())) {
                criteria.andLike("pic", "%" + content.getPic() + "%");
            }
            if (StringUtils.isNotBlank(content.getContent())) {
                criteria.andLike("content", "%" + content.getContent() + "%");
            }
            if (StringUtils.isNotBlank(content.getStatus())) {
                criteria.andLike("status", "%" + content.getStatus() + "%");
            }

        }
        List<Content> all = contentMapper.selectByExample(example);
        return new PageInfo<>(all);
    }

    @Override
    public void delete(Object[] ids) {
        removeRedisCache(SysConstants.CONTENT_REDIS_KEY);
        super.delete(ids);
    }

    @Override
    public void update(Content record) {
        removeRedisCache(SysConstants.CONTENT_REDIS_KEY);
        super.update(record);
    }

    @Override
    public void add(Content record) {
        removeRedisCache(SysConstants.CONTENT_REDIS_KEY);
        super.add(record);
    }

    @Override
    public void removeRedisCache(String key) {
        if (redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
        }

    }

}
