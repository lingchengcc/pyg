package com.pinyougou.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.common.entity.SysConstants;
import com.pinyougou.mapper.GoodsDescMapper;
import com.pinyougou.mapper.GoodsMapper;
import com.pinyougou.mapper.ItemCatMapper;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.Goods;
import com.pinyougou.pojo.GoodsDesc;
import com.pinyougou.pojo.Item;
import com.pinyougou.pojo.ItemCat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import tk.mybatis.mapper.entity.Example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/8 9:37
 **/
@Service
public class ItemPageServiceImpl implements ItemPageService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsDescMapper goodsDescMapper;
    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${pageDir}")
    private String pageDir;

    @Override
    public void genItemHtml(Long goodsId) throws IOException {
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        GoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
        List<ItemCat> itemCats = new ArrayList<>();
        itemCats.add(itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()));
        itemCats.add(itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()));
        itemCats.add(itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()));
        genHTML(goods, goodsDesc, itemCats);
    }

    @Override
    public void deleteById(Long[] ids) {
        try {
            for (Long goodsId : ids) {
                FileUtils.forceDelete(new File(pageDir + goodsId + ".html"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void genHTML(Goods goods, GoodsDesc goodsDesc, List<ItemCat> itemCats) throws IOException {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            //1.创建一个configuration对象
            //2.设置字符编码 和 模板加载的目录
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            //3.获取模板对象
            Template template = configuration.getTemplate("item.ftl");
            HashMap<String, Object> model = new HashMap<>(5);
            model.put("goods", goods);
            model.put("goodsDesc", goodsDesc);
            for (int i = 0; i < itemCats.size(); i++) {
                model.put("itemCat" + (i + 1), itemCats.get(i).getName());
            }
            //获取该SPU的所有的SKU的列表数据 存储到model中
            //select * from tb_item where goods_id = 1 and status=1 order by is_default desc
            Example exmaple = new Example(Item.class);
            Example.Criteria criteria = exmaple.createCriteria();
            criteria.andEqualTo("goodsId", goods.getId());
            criteria.andEqualTo("status", SysConstants.GOODS_STATUS_PASS_REVIEW);
            exmaple.setOrderByClause("is_default desc");
            List<Item> itemList = itemMapper.selectByExample(exmaple);
            model.put("skuList", itemList);
            fos = new FileOutputStream(new File(pageDir + goods.getId() + ".html"));
            osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            bw = new BufferedWriter(osw);
            //6.调用模板对象的process 方法输出到指定的文件中
            template.process(model, bw);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } finally {
                    osw.close();
                    fos.close();
                }
            }
        }
    }
}
