package com.pinyougou.cart.controller;

import java.util.List;

import com.pinyougou.order.service.OrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Order;
import com.github.pagehelper.PageInfo;
import com.pinyougou.entity.Result;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<Order> findAll() {
        return orderService.findAll();
    }

    @RequestMapping("/findPage")
    public PageInfo<Order> findPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return orderService.findPage(pageNo, pageSize);
    }

    /**
     * 增加
     *
     * @param order
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Order order) {
        try {
            order.setUserId(SecurityContextHolder.getContext().getAuthentication().getName());
            orderService.add(order);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param order
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Order order) {
        try {
            orderService.update(order);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne/{id}")
    public Order findOne(@PathVariable(value = "id") Long id) {
        return orderService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Long[] ids) {
        try {
            orderService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    @RequestMapping("/search")
    public PageInfo<Order> findPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                    @RequestBody Order order) {
        return orderService.findPage(pageNo, pageSize, order);
    }

}
