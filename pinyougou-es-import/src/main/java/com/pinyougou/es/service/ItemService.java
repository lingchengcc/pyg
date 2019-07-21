package com.pinyougou.es.service;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/4 16:30
 **/
public interface ItemService {
    /**
     * 从数据库中获取数据 导入到ES的索引库
     */
    void ImportDataToEs();
}
