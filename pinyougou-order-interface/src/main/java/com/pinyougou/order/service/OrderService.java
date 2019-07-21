package com.pinyougou.order.service;

import com.pinyougou.pojo.Order;
import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.CoreService;
import com.pinyougou.pojo.PayLog;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface OrderService extends CoreService<Order> {

    /**
     * 返回分页列表
     *
     * @return
     */
    PageInfo<Order> findPage(Integer pageNo, Integer pageSize);

    /**
     * 分页
     *
     * @param pageNo   当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    PageInfo<Order> findPage(Integer pageNo, Integer pageSize, Order Order);

    /**
     * 从redis查询payLog
     *
     * @param userId 用户ID
     * @return payLog
     */
    public PayLog searchPayLogFromRedis(String userId);

    /**
     * 更新订单状态
     * @param outTradeNo 订单id
     * @param transactionId 交易流水
     */
    public void updateOrderStatus(String outTradeNo, String transactionId);

}
