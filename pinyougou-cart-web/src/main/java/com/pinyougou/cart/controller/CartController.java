package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.common.entity.SysConstants;
import com.pinyougou.common.util.CookieUtil;
import com.pinyougou.entity.Cart;
import com.pinyougou.entity.Result;
import com.pinyougou.cart.service.CartService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/16 20:22
 **/
@RestController
@RequestMapping("/cart")
public class CartController {
    @Reference
    private CartService cartService;

    @RequestMapping("/getName")
    public String getLoginName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * 获取购物车列表
     *
     * @param request 请求
     * @return 购物车列表
     */
    @RequestMapping("/findCartList")
    public List<Cart> findCartList(HttpServletRequest request, HttpServletResponse response) {
        //获取用户名
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        //如果是匿名用户则操作cookie,不是则操作redis
        if (SysConstants.ANONYMOUS_USER.equals(name)) {
            String cartList = CookieUtil.getCookieValue(request, "cartList", "utf-8");
            if (StringUtils.isEmpty(cartList)) {
                cartList = "[]";
            }
            return JSON.parseArray(cartList, Cart.class);
        } else {
            List<Cart> redisCartList = cartService.findCartListFromRedis(name);
            if (redisCartList == null) {
                redisCartList = new ArrayList<>();
            }
            String cookieValue = CookieUtil.getCookieValue(request, "cartList", "utf-8");
            if (StringUtils.isEmpty(cookieValue)) {
                cookieValue = "[]";
            }
            List<Cart> cookieCartList = JSON.parseArray(cookieValue, Cart.class);
            if (cookieCartList.size() > 0) {
                List<Cart> mergeCartList = cartService.mergeCartList(cookieCartList, redisCartList);
                cartService.saveCartListToRedis(name, mergeCartList);
                //移除
                CookieUtil.deleteCookie(request, response, "cartList");
                return mergeCartList;
            }
            return redisCartList;
        }
    }

    /**
     * 添加商品到已有的购物车的列表中
     *
     * @param itemId sku id
     * @param num    sku 数量
     * @return Result
     */
    @RequestMapping("/addGoodsToCartList")
    @CrossOrigin(origins = {"http://localhost:9105"}, allowCredentials = "true")
    public Result addGoodsToCartList(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println(name);
            if (SysConstants.ANONYMOUS_USER.equals(name)) {
                //获取购物车列表
                List<Cart> cartList = findCartList(request, response);
                cartList = cartService.addGoodsToCartList(cartList, itemId, num);
                CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList), 3600 * 24, "UTF-8");
                return new Result(true, "添加成功");
            } else {
                List<Cart> cartList = cartService.findCartListFromRedis(name);
                cartList = cartService.addGoodsToCartList(cartList, itemId, num);
                cartService.saveCartListToRedis(name, cartList);
                return new Result(true, "添加成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }

}
