package com.pinyougou.portal.controller;

import java.util.List;

import com.pinyougou.content.service.ContentService;
import org.springframework.web.bind.annotation.*;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Content;

/**
 * @author silent
 */
@RestController
@RequestMapping("/content")
public class ContentController {

    @Reference
    private ContentService contentService;

    @RequestMapping("/findByCategoryId/{categoryId}")
    public List<Content> findByCategoryId(@PathVariable(value = "categoryId") Long categoryId) {
        return contentService.findByCategoryId(categoryId);
    }
}
