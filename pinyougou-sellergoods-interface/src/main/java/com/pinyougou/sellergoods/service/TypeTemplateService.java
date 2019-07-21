package com.pinyougou.sellergoods.service;

import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.CoreService;
import com.pinyougou.pojo.TypeTemplate;

import java.util.List;
import java.util.Map;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface TypeTemplateService extends CoreService<TypeTemplate> {


    /**
     * 返回分页列表
     *
     * @return
     */
    PageInfo<TypeTemplate> findPage(Integer pageNo, Integer pageSize);


    /**
     * 分页
     *
     * @param pageNo   当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    PageInfo<TypeTemplate> findPage(Integer pageNo, Integer pageSize, TypeTemplate typeTemplate);

    void pushTypeToRedis();

    void removeTypeFromRedis();

    List<Map> findSpecList(Long typeTemplateId);
}
