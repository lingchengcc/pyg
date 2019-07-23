package com.pinyougou.seckill.service;

import com.pinyougou.pojo.SeckillOrder;
import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.CoreService;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface SeckillOrderService extends CoreService<SeckillOrder> {

    /**
     * 返回分页列表
     *
     * @return
     */
    PageInfo<SeckillOrder> findPage(Integer pageNo, Integer pageSize);

    /**
     * 分页
     *
     * @param pageNo   当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    PageInfo<SeckillOrder> findPage(Integer pageNo, Integer pageSize, SeckillOrder SeckillOrder);

    /**
     * 秒杀下单
     *
     * @param seckillId 秒杀商品的ID
     * @param userId    下单的用户ID
     */
    public void submitOrder(Long seckillId, String userId);

    /**
     * @param userId
     * @return
     */
    public SeckillOrder getUserOrderStatus(String userId);

    /**
     * 更新订单的状态 支付成功的时候执行
     *
     * @param transaction_id 交易订单号
     * @param userId         支付的用户的ID
     */
    public void updateOrderStatus(String transaction_id, String userId);

    /**
     *  支付超时的时候执行。用于删除订单
     * @param userId
     */
    public void deleteOrder(String userId);


}
