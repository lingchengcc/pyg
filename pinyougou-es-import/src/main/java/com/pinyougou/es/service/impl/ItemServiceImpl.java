package com.pinyougou.es.service.impl;

import com.alibaba.fastjson.JSON;
import com.pinyougou.es.dao.ItemDao;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.pojo.Item;
import com.pinyougou.es.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/4 16:30
 **/
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemDao dao;

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public void ImportDataToEs() {
        //1.从数据库查询出符合条件的tbitem的数据
        Item record = new Item();
        //审核过的
        record.setStatus("2");
        List<Item> itemList = itemMapper.select(record);
        for (Item item : itemList) {
            String spec = item.getSpec();
            if (StringUtils.isNotBlank(spec)) {
                Map<String, String> map = JSON.parseObject(spec, Map.class);
                item.setSpecMap(map);
            }
        }
        //2.保存即可
        dao.saveAll(itemList);
    }
}
