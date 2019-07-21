package com.pinyougou.cart.service;

import com.pinyougou.entity.Cart;

import java.util.List;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/16 15:36
 **/
public interface CartService {
    /**
     * 向已有的购物车添加商品
     *
     * @param cartList 已有的购物车
     * @param itemId   商品的ID
     * @param num      要购买的数量
     * @return Cart集合
     */
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num);

    /**
     * 从redis中查询购物车
     *
     * @param username
     * @return
     */
    public List<Cart> findCartListFromRedis(String username);

    /**
     * 将购物车保存到redis
     *
     * @param username
     * @param cartList
     */
    public void saveCartListToRedis(String username, List<Cart> cartList);

    /**
     * 将cookie的购物车和redis的购物车合并
     *
     * @param cookieList
     * @param redisList
     * @return
     */
    public List<Cart> mergeCartList(List<Cart> cookieList, List<Cart> redisList);
}
