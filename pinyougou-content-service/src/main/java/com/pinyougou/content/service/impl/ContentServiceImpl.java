package com.pinyougou.content.service.impl;

import java.util.List;

import com.pinyougou.common.entity.SysConstants;
import com.pinyougou.content.service.ContentService;
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
    public List<Content> findByCategoryId(Long categoryId) {
        List<Content> contentsFromRedis = (List<Content>) redisTemplate.boundHashOps(SysConstants.CONTENT_REDIS_KEY).get(categoryId);
        if (contentsFromRedis != null && contentsFromRedis.size() > 0) {
            System.out.println("查询缓存!!");
            return contentsFromRedis;
        }
        Content record = new Content();
        record.setCategoryId(categoryId);
        record.setStatus("1");
        List<Content> contents = contentMapper.select(record);
        redisTemplate.boundHashOps(SysConstants.CONTENT_REDIS_KEY).put(categoryId, contents);
        System.out.println("查询数据库!!!");
        return contents;
    }
}
