package com.pinyougou.content.service;

import com.pinyougou.pojo.ContentCategory;
import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.CoreService;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface ContentCategoryService extends CoreService<ContentCategory> {


    /**
     * 返回分页列表
     *
     * @return
     */
    PageInfo<ContentCategory> findPage(Integer pageNo, Integer pageSize);


    /**
     * 分页
     *
     * @param pageNo   当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    PageInfo<ContentCategory> findPage(Integer pageNo, Integer pageSize, ContentCategory contentCategory);

}
