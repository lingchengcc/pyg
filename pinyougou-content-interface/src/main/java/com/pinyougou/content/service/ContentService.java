package com.pinyougou.content.service;

import java.util.List;

import com.pinyougou.pojo.Content;
import com.pinyougou.core.service.CoreService;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface ContentService extends CoreService<Content> {
    List<Content> findByCategoryId(Long categoryId);
}
