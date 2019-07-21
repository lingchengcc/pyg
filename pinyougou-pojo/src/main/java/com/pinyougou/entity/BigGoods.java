package com.pinyougou.entity;

import com.pinyougou.pojo.Goods;
import com.pinyougou.pojo.GoodsDesc;
import com.pinyougou.pojo.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author silent
 * @version 1.0
 * @date 2019/6/30 19:09
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BigGoods implements Serializable {
    /**
     * 商品SPU
     */
    private Goods goods;
    /**
     * 商品扩展
     */
    private GoodsDesc goodsDesc;
    /**
     * 商品SKU列表
     */
    private List<Item> itemList;
}
