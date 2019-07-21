package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.Seller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author silent
 * @version 1.0
 * @date 2019/6/28 12:09
 **/
@RestController
@RequestMapping("/seller")
public class RegisterController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Reference
    private SellerService sellerService;

    /**
     * 增加
     *
     * @param seller
     * @return
     */
    @RequestMapping("/register")
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

}
