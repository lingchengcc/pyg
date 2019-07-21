package com.pinyougou.sellergoods.service;

import com.pinyougou.entity.SpecificationOptions;
import com.pinyougou.pojo.Specification;
import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.CoreService;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface SpecificationService extends CoreService<Specification> {

    /**
     * 返回分页列表
     *
     * @return
     */
    PageInfo<Specification> findPage(Integer pageNo, Integer pageSize);

    /**
     * 分页
     *
     * @param pageNo   当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    PageInfo<Specification> findPage(Integer pageNo, Integer pageSize, Specification specification);

    SpecificationOptions findSpecificationOptions(Long id);

    void updateSpecificationOptions(SpecificationOptions specificationOptions);

    void addSpecificationOptions(SpecificationOptions specificationOptions);
}
