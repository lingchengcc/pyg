package com.pinyougou.es.dao;

import com.pinyougou.pojo.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/4 16:36
 **/
public interface ItemDao extends ElasticsearchRepository<Item, Long> {
}
