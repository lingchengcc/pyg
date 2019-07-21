package com.pinyougou.manager.controller;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.pinyougou.common.entity.MessageInfo;
import com.pinyougou.common.entity.SysConstants;
import com.pinyougou.pojo.Item;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Goods;
import com.pinyougou.sellergoods.service.GoodsService;
import com.github.pagehelper.PageInfo;
import com.pinyougou.entity.Result;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;
    @Autowired
    private DefaultMQProducer producer;

    @RequestMapping("/updateStatus/{status}")
    public Result updateStatus(@RequestBody Long[] ids, @PathVariable(value = "status") String status) {
        try {
            goodsService.updateStatus(ids, status);
            if (SysConstants.GOODS_STATUS_PASS_REVIEW.equals(status)) {
                List<Item> itemList = goodsService.findItemListByIds(ids);
                MessageInfo info = new MessageInfo(itemList, "Goods_Topic", "goods_update_tag", "updateStatus", MessageInfo.METHOD_UPDATE);
                SendResult send = producer.send(new Message(info.getTopic(), info.getTags(), info.getKeys(), JSON.toJSONString(info).getBytes(RemotingHelper.DEFAULT_CHARSET)));
                System.out.println(">>>>>>>>>>>" + send.getSendStatus());
            }
            return new Result(true, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "更新失败");
        }
    }

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<Goods> findAll() {
        return goodsService.findAll();
    }

    @RequestMapping("/findPage")
    public PageInfo<Goods> findPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return goodsService.findPage(pageNo, pageSize);
    }

    /**
     * 增加
     *
     * @param goods
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Goods goods) {
        try {
            goodsService.add(goods);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param goods
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Goods goods) {
        try {
            goodsService.update(goods);
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
    @RequestMapping("/findOne/{id}")
    public Goods findOne(@PathVariable(value = "id") Long id) {
        return goodsService.findOne(id);
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
            goodsService.delete(ids);
            MessageInfo info = new MessageInfo(ids, "Goods_Topic", "goods_delete_tag", "delete", MessageInfo.METHOD_DELETE);
            SendResult send = producer.send(new Message(info.getTopic(), info.getTags(), info.getKeys(), JSON.toJSONString(info).getBytes(RemotingHelper.DEFAULT_CHARSET)));
            System.out.println(">>>>>>>>>>>" + send.getSendStatus());
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    @RequestMapping("/search")
    public PageInfo<Goods> findPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                    @RequestBody Goods goods) {
        //查找提交审核的商品
        goods.setAuditStatus(SysConstants.GOODS_STATUS_SUBMIT_REVIEW);
        return goodsService.findPage(pageNo, pageSize, goods);
    }

}
