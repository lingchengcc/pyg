package com.pinyougou.sellergoods.service.impl;

import java.util.Arrays;
import java.util.List;

import com.pinyougou.common.entity.SysConstants;
import com.pinyougou.entity.BigGoods;
import com.pinyougou.mapper.GoodsDescMapper;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.pojo.GoodsDesc;
import com.pinyougou.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import com.pinyougou.core.service.CoreServiceImpl;
import tk.mybatis.mapper.entity.Example;
import com.pinyougou.mapper.GoodsMapper;
import com.pinyougou.pojo.Goods;

import com.pinyougou.sellergoods.service.GoodsService;


/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class GoodsServiceImpl extends CoreServiceImpl<Goods> implements GoodsService {


    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsDescMapper goodsDescMapper;
    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    public GoodsServiceImpl(GoodsMapper goodsMapper) {
        super(goodsMapper, Goods.class);
        this.goodsMapper = goodsMapper;
    }

    @Override
    public void delete(Object[] ids) {
        Example exmaple = new Example(Goods.class);
        Example.Criteria criteria = exmaple.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        //要更新后的值
        Goods goods = new Goods();
        goods.setIsDelete(true);
        goodsMapper.updateByExampleSelective(goods, exmaple);
    }

    @Override
    public void updateStatus(Long[] ids, String status) {
        Goods record = new Goods();
        record.setAuditStatus(status);
        Example example = new Example(Goods.class);
        example.createCriteria().andIn("id", Arrays.asList(ids));
        goodsMapper.updateByExampleSelective(record, example);
    }

    @Override
    public void add(BigGoods bigGoods) {
        //1.获取goods
        Goods goods = bigGoods.getGoods();
        goods.setAuditStatus("0");
        goods.setIsDelete(false);
        goodsMapper.insert(goods);
        GoodsDesc goodsDesc = bigGoods.getGoodsDesc();
        goodsDesc.setGoodsId(goods.getId());
        goodsDescMapper.insert(goodsDesc);
        //3.获取skuList TODO
    }

    @Override
    public PageInfo<Goods> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<Goods> all = goodsMapper.selectAll();
        return new PageInfo<>(all);
    }

    @Override
    public PageInfo<Goods> findPage(Integer pageNo, Integer pageSize, Goods goods) {
        PageHelper.startPage(pageNo, pageSize);

        Example example = new Example(Goods.class);
        Example.Criteria criteria = example.createCriteria();
        //查询未删除的商品
        criteria.andEqualTo("isDelete", false);
        if (goods != null) {
            if (StringUtils.isNotBlank(goods.getSellerId())) {
                criteria.andEqualTo("sellerId", goods.getSellerId());
            }
            if (StringUtils.isNotBlank(goods.getGoodsName())) {
                criteria.andLike("goodsName", "%" + goods.getGoodsName() + "%");
            }
            if (StringUtils.isNotBlank(goods.getAuditStatus())) {
                criteria.andEqualTo("auditStatus", goods.getAuditStatus());
            }
            if (StringUtils.isNotBlank(goods.getIsMarketable())) {
                criteria.andLike("isMarketable", "%" + goods.getIsMarketable() + "%");
            }
            if (StringUtils.isNotBlank(goods.getCaption())) {
                criteria.andLike("caption", "%" + goods.getCaption() + "%");
            }
            if (StringUtils.isNotBlank(goods.getSmallPic())) {
                criteria.andLike("smallPic", "%" + goods.getSmallPic() + "%");
            }
            if (StringUtils.isNotBlank(goods.getIsEnableSpec())) {
                criteria.andLike("isEnableSpec", "%" + goods.getIsEnableSpec() + "%");
            }

        }
        List<Goods> all = goodsMapper.selectByExample(example);
        return new PageInfo<>(all);
    }

    @Override
    public List<Item> findItemListByIds(Long[] ids) {
        Example example = new Example(Item.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("goodsId", Arrays.asList(ids));
        criteria.andEqualTo("status", SysConstants.GOODS_STATUS_PASS_REVIEW);
        return itemMapper.selectByExample(example);
    }

}
