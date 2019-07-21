package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.BrandImg;
import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.CoreService;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface BrandImgService extends CoreService<BrandImg> {

    /**
     * 返回分页列表
     *
     * @return
     */
    PageInfo<BrandImg> findPage(Integer pageNo, Integer pageSize);

    /**
     * 分页
     *
     * @param pageNo   当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    PageInfo<BrandImg> findPage(Integer pageNo, Integer pageSize, BrandImg brandImg);

}
