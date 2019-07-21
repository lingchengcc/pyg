package com.pinyougou.entity;

import com.pinyougou.pojo.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/16 15:27
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart implements Serializable {
    /**
     * 商家ID
     */
    private String sellerId;
    /**
     * 商家名称
     */
    private String sellerName;
    /**
     * 购物车明细
     */
    private List<OrderItem> orderItemList;

    public Cart(String sellerId, String sellerName) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
    }
}
