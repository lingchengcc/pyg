package com.pinyougou.sellergoods.service;

import com.pinyougou.entity.BigGoods;
import com.pinyougou.pojo.Goods;
import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.CoreService;
import com.pinyougou.pojo.Item;

import java.util.List;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface GoodsService extends CoreService<Goods> {

    void updateStatus(Long[] ids, String status);

    void add(BigGoods bigGoods);

    /**
     * 返回分页列表
     *
     * @return
     */
    PageInfo<Goods> findPage(Integer pageNo, Integer pageSize);

    /**
     * 分页
     *
     * @param pageNo   当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    PageInfo<Goods> findPage(Integer pageNo, Integer pageSize, Goods goods);

    List<Item> findItemListByIds(Long[] ids);
}
