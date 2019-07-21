package com.pinyougou.page.service;

import java.io.IOException;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/8 9:12
 **/
public interface ItemPageService {
    /**
     * 生成商品详细页
     * @param goodsId
     */
    public void genItemHtml(Long goodsId) throws IOException;

    void deleteById(Long[] ids);
}
