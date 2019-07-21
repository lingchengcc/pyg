package com.pinyougou.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Seller;
import com.pinyougou.sellergoods.service.SellerService;
import com.github.pagehelper.PageInfo;
import com.pinyougou.entity.Result;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Reference
    private SellerService sellerService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<Seller> findAll() {
        return sellerService.findAll();
    }


    @RequestMapping("/findPage")
    public PageInfo<Seller> findPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return sellerService.findPage(pageNo, pageSize);
    }

    /**
     * 增加
     *
     * @param seller
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Seller seller) {
        try {
            seller.setPassword(passwordEncoder.encode(seller.getPassword()));
            sellerService.add(seller);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param seller
     * @return
     */
    @RequestMapping("/update/{status}")
    public Result update(@PathVariable("status") String status, @RequestBody Seller seller) {
        try {
            seller.setStatus(status);
            sellerService.update(seller);
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
    @RequestMapping(value = "/findOne/{id}", method = RequestMethod.GET)
    public Seller findOne(@PathVariable(value = "id") String id) {
        return sellerService.findOne(id);
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
            sellerService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }


    @RequestMapping("/search")
    public PageInfo<Seller> findPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                     @RequestBody Seller seller) {
        return sellerService.findPage(pageNo, pageSize, seller);
    }

}
