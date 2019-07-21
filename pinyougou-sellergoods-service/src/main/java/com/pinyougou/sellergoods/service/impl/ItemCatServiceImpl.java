package com.pinyougou.sellergoods.service.impl;

import java.util.HashMap;
import java.util.List;

import com.pinyougou.common.entity.SysConstants;
import com.sun.tools.javac.resources.compiler;
import com.sun.tools.javac.resources.javac;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import com.pinyougou.core.service.CoreServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import tk.mybatis.mapper.entity.Example;
import com.pinyougou.mapper.ItemCatMapper;
import com.pinyougou.pojo.ItemCat;
import com.pinyougou.sellergoods.service.ItemCatService;


/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class ItemCatServiceImpl extends CoreServiceImpl<ItemCat> implements ItemCatService {


    private ItemCatMapper itemCatMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    public ItemCatServiceImpl(ItemCatMapper itemCatMapper) {
        super(itemCatMapper, ItemCat.class);
        this.itemCatMapper = itemCatMapper;
    }


    @Override
    public PageInfo<ItemCat> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<ItemCat> all = itemCatMapper.selectAll();
        return new PageInfo<>(all);
    }


    @Override
    public PageInfo<ItemCat> findPage(Integer pageNo, Integer pageSize, ItemCat itemCat) {
        PageHelper.startPage(pageNo, pageSize);

        Example example = new Example(ItemCat.class);
        Example.Criteria criteria = example.createCriteria();

        if (itemCat != null) {
            if (StringUtils.isNotBlank(itemCat.getName())) {
                criteria.andLike("name", "%" + itemCat.getName() + "%");
            }

        }
        List<ItemCat> all = itemCatMapper.selectByExample(example);
        return new PageInfo<>(all);
    }

    @Override
    public List<ItemCat> findByParentId(Long parentId) {
        ItemCat itemCat = new ItemCat();
        itemCat.setParentId(parentId);
        pushItemCatToRedis();
        return itemCatMapper.select(itemCat);
    }

    @Override
    public void add(ItemCat record) {
        super.add(record);
        removeItemCatFromRedis();
    }

    @Override
    public void update(ItemCat record) {
        super.update(record);
        removeItemCatFromRedis();
    }

    @Override
    public void delete(Object[] ids) {
        super.delete(ids);
        removeItemCatFromRedis();
    }

    @Override
    public void pushItemCatToRedis() {
        if (!redisTemplate.hasKey(SysConstants.ITEM_CAT_REDIS_KEY)) {
            final List<ItemCat> itemCats = findAll();
            for (ItemCat itemCat : itemCats) {
                redisTemplate.boundHashOps(SysConstants.ITEM_CAT_REDIS_KEY).put(itemCat.getName(), itemCat.getTypeId());
            }
        }
    }

    @Override
    public void removeItemCatFromRedis() {
        if (redisTemplate.hasKey(SysConstants.ITEM_CAT_REDIS_KEY)) {
            redisTemplate.delete(SysConstants.ITEM_CAT_REDIS_KEY);
        } else {
            final List<ItemCat> itemCats = findAll();
            for (ItemCat itemCat : itemCats) {
                redisTemplate.boundHashOps(SysConstants.ITEM_CAT_REDIS_KEY).put(itemCat.getName(), itemCat.getTypeId());
            }
        }
    }

}
