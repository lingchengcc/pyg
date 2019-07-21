package com.pinyougou.manager.controller;

import java.util.List;

import com.pinyougou.entity.SpecificationOptions;
import com.pinyougou.pojo.Specification;
import org.springframework.web.bind.annotation.*;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.SpecificationService;

import com.github.pagehelper.PageInfo;
import com.pinyougou.entity.Result;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/specification")
public class SpecificationController {

    @Reference
    private SpecificationService specificationService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<Specification> findAll() {
        return specificationService.findAll();
    }


    @RequestMapping("/findPage")
    public PageInfo<Specification> findPage(@RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize) {
        return specificationService.findPage(pageNo, pageSize);
    }

    /**
     * 修改
     *
     * @param specificationOptions
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody SpecificationOptions specificationOptions) {
        try {
            specificationService.updateSpecificationOptions(specificationOptions);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 增加
     *
     * @param specificationOptions
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody SpecificationOptions specificationOptions) {
        try {
            specificationService.addSpecificationOptions(specificationOptions);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne/{id}")
    public SpecificationOptions findOne(@PathVariable(value = "id") Long id) {
        return specificationService.findSpecificationOptions(id);
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
            specificationService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    @RequestMapping("/search")
    public PageInfo<Specification> findPage(@RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize,
                                            @RequestBody Specification specification) {
        return specificationService.findPage(pageNo, pageSize, specification);
    }

}
