package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.ItemCat;
import com.pinyougou.sellergoods.service.ItemCatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;

    /**
     * 通过父id查找分类
     *
     * @param parentId
     * @return
     */
    @RequestMapping("/findByParentId/{parentId}")
    public List<ItemCat> findByParentId(@PathVariable(value = "parentId") Long parentId) {
        return itemCatService.findByParentId(parentId);
    }

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<ItemCat> findAll() {
        return itemCatService.findAll();
    }


    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne/{id}")
    public ItemCat findOne(@PathVariable(value = "id") Long id) {
        return itemCatService.findOne(id);
    }

}
