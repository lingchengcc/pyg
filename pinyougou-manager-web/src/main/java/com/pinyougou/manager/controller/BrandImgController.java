package com.pinyougou.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.BrandImg;
import com.pinyougou.sellergoods.service.BrandImgService;
import com.github.pagehelper.PageInfo;
import com.pinyougou.entity.Result;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/brandImg")
public class BrandImgController {

    @Reference
    private BrandImgService brandImgService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<BrandImg> findAll() {
        return brandImgService.findAll();
    }


    @RequestMapping("/findPage")
    public PageInfo<BrandImg> findPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return brandImgService.findPage(pageNo, pageSize);
    }

    /**
     * 增加
     *
     * @param brandImg
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody BrandImg brandImg) {
        try {
            brandImgService.add(brandImg);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param brandImg
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody BrandImg brandImg) {
        try {
            brandImgService.update(brandImg);
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
    public BrandImg findOne(@PathVariable(value = "id") Long id) {
        return brandImgService.findOne(id);
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
            brandImgService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }


    @RequestMapping("/search")
    public PageInfo<BrandImg> findPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                       @RequestBody BrandImg brandImg) {
        return brandImgService.findPage(pageNo, pageSize, brandImg);
    }

}
