package com.pinyougou.manager.controller;

import java.util.List;

import com.pinyougou.common.entity.SysConstants;
import org.springframework.web.bind.annotation.*;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Content;
import com.pinyougou.sellergoods.service.ContentService;
import com.github.pagehelper.PageInfo;
import com.pinyougou.entity.Result;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/content")
public class ContentController {

    @Reference
    private ContentService contentService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<Content> findAll() {
        return contentService.findAll();
    }


    @RequestMapping("/findPage")
    public PageInfo<Content> findPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return contentService.findPage(pageNo, pageSize);
    }

    /**
     * 增加
     *
     * @param content
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Content content) {
        try {
            contentService.add(content);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param content
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Content content) {
        try {
            contentService.update(content);
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
    public Content findOne(@PathVariable(value = "id") Long id) {
        return contentService.findOne(id);
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
            contentService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    @RequestMapping("/search")
    public PageInfo<Content> findPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                      @RequestBody Content content) {
        return contentService.findPage(pageNo, pageSize, content);
    }

}
