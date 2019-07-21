package com.pinyougou.search.service;

import com.pinyougou.pojo.Item;

import java.util.List;
import java.util.Map;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/4 17:10
 **/
public interface ItemSearchService {
    /**
     * 根据搜索条件搜索内容展示数据返回
     *
     * @param searchMap
     * @return
     */
    Map<String, Object> search(Map<String, Object> searchMap);

    void updateIndex(List<Item> itemList);

    void deleteByIds(Long[] ids);
}
