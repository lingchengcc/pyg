package com.pinyougou.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pinyougou.common.util.IdWorker;
import com.pinyougou.entity.Cart;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.mapper.OrderItemMapper;
import com.pinyougou.mapper.PayLogMapper;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pojo.Item;
import com.pinyougou.pojo.OrderItem;
import com.pinyougou.pojo.PayLog;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import com.pinyougou.core.service.CoreServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;
import com.pinyougou.mapper.OrderMapper;
import com.pinyougou.pojo.Order;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class OrderServiceImpl extends CoreServiceImpl<Order> implements OrderService {

    private OrderMapper orderMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private PayLogMapper payLogMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper) {
        super(orderMapper, Order.class);
        this.orderMapper = orderMapper;
    }

    @Override
    public PageInfo<Order> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<Order> all = orderMapper.selectAll();
        return new PageInfo<>(all);
    }

    @Override
    public PageInfo<Order> findPage(Integer pageNo, Integer pageSize, Order order) {
        PageHelper.startPage(pageNo, pageSize);

        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();

        if (order != null) {
            if (StringUtils.isNotBlank(order.getPaymentType())) {
                criteria.andLike("paymentType", "%" + order.getPaymentType() + "%");
            }
            if (StringUtils.isNotBlank(order.getPostFee())) {
                criteria.andLike("postFee", "%" + order.getPostFee() + "%");
            }
            if (StringUtils.isNotBlank(order.getStatus())) {
                criteria.andLike("status", "%" + order.getStatus() + "%");
            }
            if (StringUtils.isNotBlank(order.getShippingName())) {
                criteria.andLike("shippingName", "%" + order.getShippingName() + "%");
            }
            if (StringUtils.isNotBlank(order.getShippingCode())) {
                criteria.andLike("shippingCode", "%" + order.getShippingCode() + "%");
            }
            if (StringUtils.isNotBlank(order.getUserId())) {
                criteria.andLike("userId", "%" + order.getUserId() + "%");
            }
            if (StringUtils.isNotBlank(order.getBuyerMessage())) {
                criteria.andLike("buyerMessage", "%" + order.getBuyerMessage() + "%");
            }
            if (StringUtils.isNotBlank(order.getBuyerNick())) {
                criteria.andLike("buyerNick", "%" + order.getBuyerNick() + "%");
            }
            if (StringUtils.isNotBlank(order.getBuyerRate())) {
                criteria.andLike("buyerRate", "%" + order.getBuyerRate() + "%");
            }
            if (StringUtils.isNotBlank(order.getReceiverAreaName())) {
                criteria.andLike("receiverAreaName", "%" + order.getReceiverAreaName() + "%");
            }
            if (StringUtils.isNotBlank(order.getReceiverMobile())) {
                criteria.andLike("receiverMobile", "%" + order.getReceiverMobile() + "%");
            }
            if (StringUtils.isNotBlank(order.getReceiverZipCode())) {
                criteria.andLike("receiverZipCode", "%" + order.getReceiverZipCode() + "%");
            }
            if (StringUtils.isNotBlank(order.getReceiver())) {
                criteria.andLike("receiver", "%" + order.getReceiver() + "%");
            }
            if (StringUtils.isNotBlank(order.getInvoiceType())) {
                criteria.andLike("invoiceType", "%" + order.getInvoiceType() + "%");
            }
            if (StringUtils.isNotBlank(order.getSourceType())) {
                criteria.andLike("sourceType", "%" + order.getSourceType() + "%");
            }
            if (StringUtils.isNotBlank(order.getSellerId())) {
                criteria.andLike("sellerId", "%" + order.getSellerId() + "%");
            }

        }
        List<Order> all = orderMapper.selectByExample(example);
        return new PageInfo<>(all);
    }

    @Override
    public PayLog searchPayLogFromRedis(String userId) {
        return (PayLog) redisTemplate.boundHashOps("payLog").get(userId);
    }

    @Override
    public void updateOrderStatus(String outTradeNo, String transactionId) {
        //修改支付日志
        PayLog payLog = payLogMapper.selectByPrimaryKey(outTradeNo);
        payLog.setPayTime(new Date());
        payLog.setTradeState("1");
        payLog.setTransactionId(transactionId);
        payLogMapper.updateByPrimaryKey(payLog);
        String orderList = payLog.getOrderList();
        String[] orderIds = orderList.split(",");
        //修改订单支付状态
        for (String orderId : orderIds) {
            Order order = orderMapper.selectByPrimaryKey(Long.parseLong(orderId));
            if (order != null) {
                order.setStatus("2");
                orderMapper.updateByPrimaryKey(order);
            }
        }
        redisTemplate.boundHashOps("payLog").delete(payLog.getUserId());
    }

    @Override
    public void add(Order order) {
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(order.getUserId());
        List<Long> orderList = new ArrayList<>();
        //总金额 （元）
        double totalMoney = 0;
        assert cartList != null;
        for (Cart cart : cartList) {
            final long orderId = idWorker.nextId();
            //订单ID
            order.setOrderId(orderId);
            //支付类型
            order.setPaymentType(order.getPaymentType());
            //状态：未付款
            order.setStatus("1");
            final Date date = new Date();
            //订单创建日期
            order.setCreateTime(date);
            //订单更新日期
            order.setUpdateTime(date);
            //保存订单ID
            orderList.add(orderId);
            double money = 0;
            for (OrderItem orderItem : cart.getOrderItemList()) {
                orderItem.setId(idWorker.nextId());
                orderItem.setOrderId(orderId);
                orderItem.setSellerId(cart.getSellerId());
                Item item = itemMapper.selectByPrimaryKey(orderItem.getItemId());
                orderItem.setGoodsId(item.getGoodsId());
                money += orderItem.getTotalFee().doubleValue();
                orderItemMapper.insert(orderItem);
            }
            order.setPayment(new BigDecimal(money));
            totalMoney += money;
            orderMapper.insert(order);
        }
        PayLog payLog = new PayLog();
        payLog.setUserId(order.getUserId());
        payLog.setOutTradeNo(idWorker.nextId() + "");
        payLog.setCreateTime(new Date());
        String ids = orderList.toString().replace("[", "").replace("]", "").replace(" ", "");
        payLog.setOrderList(ids);
        payLog.setPayType("1");
        payLog.setTotalFee((long) (totalMoney * 100));
        payLog.setTradeState("0");
        payLogMapper.insert(payLog);
        //将订单日志加入redis
        redisTemplate.boundHashOps("payLog").put(order.getUserId(), payLog);
        //从redis删除购物车
        redisTemplate.boundHashOps("cartList").delete(order.getUserId());
    }
}
