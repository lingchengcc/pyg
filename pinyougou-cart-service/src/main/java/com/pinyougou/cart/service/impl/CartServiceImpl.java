package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.entity.Cart;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.pojo.Item;
import com.pinyougou.pojo.OrderItem;
import com.pinyougou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/16 15:40
 **/
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
       /* 1. 根据商品SKU ID查询SKU商品信息
        2. 获取商家ID
        3. 根据商家ID判断购物车列表中是否存在该商家的购物车
        4. 如果购物车列表中不存在该商家的购物车
        4.1 新建购物车对象
        4.2 将新建的购物车对象添加到购物车列表
        5. 如果购物车列表中存在该商家的购物车, 查询购物车明细列表中是否存在该商品
        5.1.如果没有, 新增购物车明细
        5.2.如果有, 在原购物车明细上添加数量, 更改金额*/
        try {
            Item item = itemMapper.selectByPrimaryKey(itemId);
            String sellerId = item.getSellerId();
            Cart cart = findCartBySellerId(sellerId, cartList);
            if (cart != null) {
                OrderItem orderItem = findOrderItemByItemId(itemId, cart.getOrderItemList());
                if (orderItem != null) {
                    //如果购物车存在商品则数量相加
                    orderItem.setNum(orderItem.getNum() + num);
                    orderItem.setTotalFee(new BigDecimal(orderItem.getNum() * item.getPrice().doubleValue()));
                    //判断如果商品的购买数量为0 表示不买了，就要删除商品
                    if (orderItem.getNum() == 0) {
                        cart.getOrderItemList().remove(orderItem);
                    }
                    //如果是长度为空说明 用户没购买该商家的商品就直接删除对象
                    if (cart.getOrderItemList().size() == 0) {
                        //商家也删除了
                        cartList.remove(cart);
                    }

                } else {
                    orderItem = new OrderItem();
                    setOrderItem(itemId, num, item, orderItem);
                    cart.getOrderItemList().add(orderItem);
                }
            } else {
                cart = new Cart(sellerId, item.getSeller());
                ArrayList<OrderItem> orderItems = new ArrayList<>();
                OrderItem orderItem = new OrderItem();
                setOrderItem(itemId, num, item, orderItem);
                orderItems.add(orderItem);
                cart.setOrderItemList(orderItems);
                cartList.add(cart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cartList;
    }

    @Override
    public List<Cart> findCartListFromRedis(String username) {
        return (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
    }

    @Override
    public void saveCartListToRedis(String username, List<Cart> cartList) {
        redisTemplate.boundHashOps("cartList").put(username, cartList);
    }

    @Override
    public List<Cart> mergeCartList(List<Cart> cookieList, List<Cart> redisList) {
        for (Cart cart : cookieList) {
            for (OrderItem orderItem : cart.getOrderItemList()) {
                redisList = addGoodsToCartList(redisList, orderItem.getItemId(), orderItem.getNum());
            }
        }
        return redisList;
    }

    /**
     * 给OrderItem赋值
     *
     * @param itemId    sku id
     * @param num       数量
     * @param item      sku
     * @param orderItem orderItem
     */
    private void setOrderItem(Long itemId, Integer num, Item item, OrderItem orderItem) {
        orderItem.setNum(num);
        orderItem.setItemId(itemId);
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setTitle(item.getTitle());
        orderItem.setPrice(item.getPrice());
        orderItem.setTotalFee(new BigDecimal(num * item.getPrice().doubleValue()));
        orderItem.setPicPath(item.getImage());
    }

    /**
     * 判断购物车里是否存在某种商品
     *
     * @param itemId        sku id
     * @param orderItemList sku 列表
     * @return OrderItem
     */
    private OrderItem findOrderItemByItemId(Long itemId, List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            if (orderItem.getItemId().longValue() == itemId) {
                return orderItem;
            }
        }
        return null;
    }

    /**
     * 判断商家是否存在购物车列表
     *
     * @param sellerId 商家id
     * @param cartList 购物车列表
     * @return 购物车列表
     */
    private Cart findCartBySellerId(String sellerId, List<Cart> cartList) {
        for (Cart cart : cartList) {
            if (cart.getSellerId().equals(sellerId)) {
                return cart;
            }
        }
        return null;
    }
}
