package com.pinyougou.shop.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author silent
 * @version 1.0
 * @date 2019/6/27 15:44
 **/
@RestController
@RequestMapping("/login")
public class LoginController {
    @RequestMapping("/getName")
    public String getLoginName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
