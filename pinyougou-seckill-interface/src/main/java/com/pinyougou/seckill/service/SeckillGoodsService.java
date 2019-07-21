package com.pinyougou.seckill.service;

import com.pinyougou.pojo.SeckillGoods;
import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.CoreService;

import java.io.IOException;
import java.util.List;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface SeckillGoodsService extends CoreService<SeckillGoods> {

    /**
     * 返回分页列表
     *
     * @return
     */
    PageInfo<SeckillGoods> findPage(Integer pageNo, Integer pageSize);

    /**
     * 分页
     *
     * @param pageNo   当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    PageInfo<SeckillGoods> findPage(Integer pageNo, Integer pageSize, SeckillGoods SeckillGoods);

    /**
     * 更新秒杀商品状态
     *
     * @param ids    ids
     * @param status 状态
     */
    void updateStatus(Long[] ids, String status);

    /**
     * 通过ids查找秒杀商品
     *
     * @param ids ids
     * @return
     */
    List<SeckillGoods> findSeckillGoods(Long[] ids);

    void genItemHtml(SeckillGoods seckillGoods) throws IOException;

    void deleteById(Long[] ids);
}
