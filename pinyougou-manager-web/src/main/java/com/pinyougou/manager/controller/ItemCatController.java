package com.pinyougou.manager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.ItemCat;
import com.pinyougou.sellergoods.service.ItemCatService;

import com.github.pagehelper.PageInfo;
import com.pinyougou.entity.Result;

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


    @RequestMapping("/findPage")
    public PageInfo<ItemCat> findPage(@RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize) {
        return itemCatService.findPage(pageNo, pageSize);
    }

    /**
     * 增加
     *
     * @param itemCat
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody ItemCat itemCat) {
        try {
            itemCatService.add(itemCat);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param itemCat
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody ItemCat itemCat) {
        try {
            itemCatService.update(itemCat);
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
    public ItemCat findOne(@PathVariable(value = "id") Long id) {
        return itemCatService.findOne(id);
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
            itemCatService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }


    @RequestMapping("/search")
    public PageInfo<ItemCat> findPage(@RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize,
                                      @RequestBody ItemCat itemCat) {
        return itemCatService.findPage(pageNo, pageSize, itemCat);
    }

}
