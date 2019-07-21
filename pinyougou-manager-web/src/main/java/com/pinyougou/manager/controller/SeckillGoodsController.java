package com.pinyougou.manager.controller;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.pinyougou.common.entity.MessageInfo;
import com.pinyougou.common.entity.SysConstants;
import com.pinyougou.seckill.service.SeckillGoodsService;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.SeckillGoods;
import com.github.pagehelper.PageInfo;
import com.pinyougou.entity.Result;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {

    @Reference
    private SeckillGoodsService seckillGoodsService;
    @Autowired
    private DefaultMQProducer producer;

    @RequestMapping("/updateStatus/{status}")
    public Result updateStatus(@RequestBody Long[] ids, @PathVariable(value = "status") String status) {
        try {
            seckillGoodsService.updateStatus(ids, status);
            if (SysConstants.GOODS_STATUS_PASS_REVIEW.equals(status)) {
                List<SeckillGoods> seckillGoodsList = seckillGoodsService.findSeckillGoods(ids);
                MessageInfo info = new MessageInfo(seckillGoodsList, "SECKILL_TOPIC", "Tags_genHtml", "seckillGoods_updateStatus", MessageInfo.METHOD_UPDATE);
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
    public List<SeckillGoods> findAll() {
        return seckillGoodsService.findAll();
    }

    @RequestMapping("/findPage")
    public PageInfo<SeckillGoods> findPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return seckillGoodsService.findPage(pageNo, pageSize);
    }

    /**
     * 增加
     *
     * @param seckillGoods
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody SeckillGoods seckillGoods) {
        try {
            seckillGoodsService.add(seckillGoods);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param seckillGoods
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody SeckillGoods seckillGoods) {
        try {
            seckillGoodsService.update(seckillGoods);
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
    public SeckillGoods findOne(@PathVariable(value = "id") Long id) {
        return seckillGoodsService.findOne(id);
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
            seckillGoodsService.delete(ids);
            MessageInfo info = new MessageInfo(ids, "SECKILL_TOPIC", "Tags_SecKill_Delete", "seckillGoods_delete", MessageInfo.METHOD_DELETE);
            SendResult send = producer.send(new Message(info.getTopic(), info.getTags(), info.getKeys(), JSON.toJSONString(info).getBytes(RemotingHelper.DEFAULT_CHARSET)));
            System.out.println(">>>>>>>>>>>" + send.getSendStatus());
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    @RequestMapping("/search")
    public PageInfo<SeckillGoods> findPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                           @RequestBody SeckillGoods seckillGoods) {
        //查找未审核的秒杀商品
        seckillGoods.setStatus("1");
        return seckillGoodsService.findPage(pageNo, pageSize, seckillGoods);
    }

}
