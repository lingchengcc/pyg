package com.pinyougou.seckill.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/22 11:27
 **/
@RestController
@RequestMapping("/page")
public class PageController {
    @RequestMapping("/login")
    public String showPage(String url) {
        return "redirect:" + url;
    }
}
