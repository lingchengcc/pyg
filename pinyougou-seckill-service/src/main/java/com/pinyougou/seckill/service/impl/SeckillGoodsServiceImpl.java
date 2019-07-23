package com.pinyougou.seckill.service.impl;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.pinyougou.common.entity.SysConstants;
import com.pinyougou.pojo.*;
import com.pinyougou.seckill.service.SeckillGoodsService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import com.pinyougou.core.service.CoreServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import tk.mybatis.mapper.entity.Example;
import com.pinyougou.mapper.SeckillGoodsMapper;


/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class SeckillGoodsServiceImpl extends CoreServiceImpl<SeckillGoods> implements SeckillGoodsService {

    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${pageDir}")
    private String pageDir;

    @Autowired
    public SeckillGoodsServiceImpl(SeckillGoodsMapper seckillGoodsMapper) {
        super(seckillGoodsMapper, SeckillGoods.class);
        this.seckillGoodsMapper = seckillGoodsMapper;
    }

    @Override
    public List<SeckillGoods> findAll() {
        return redisTemplate.boundHashOps(SysConstants.SEC_KILL_GOODS).values();
    }

    @Override
    public PageInfo<SeckillGoods> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<SeckillGoods> all = seckillGoodsMapper.selectAll();
        return new PageInfo<>(all);
    }

    @Override
    public PageInfo<SeckillGoods> findPage(Integer pageNo, Integer pageSize, SeckillGoods seckillGoods) {
        PageHelper.startPage(pageNo, pageSize);

        Example example = new Example(SeckillGoods.class);
        Example.Criteria criteria = example.createCriteria();

        if (seckillGoods != null) {
            if (StringUtils.isNotBlank(seckillGoods.getTitle())) {
                criteria.andLike("title", "%" + seckillGoods.getTitle() + "%");
            }
            if (StringUtils.isNotBlank(seckillGoods.getSmallPic())) {
                criteria.andLike("smallPic", "%" + seckillGoods.getSmallPic() + "%");
            }
            if (StringUtils.isNotBlank(seckillGoods.getSellerId())) {
                criteria.andLike("sellerId", "%" + seckillGoods.getSellerId() + "%");
            }
            if (StringUtils.isNotBlank(seckillGoods.getStatus())) {
                criteria.andLike("status", "%" + seckillGoods.getStatus() + "%");
            }
            if (StringUtils.isNotBlank(seckillGoods.getIntroduction())) {
                criteria.andLike("introduction", "%" + seckillGoods.getIntroduction() + "%");
            }

        }
        List<SeckillGoods> all = seckillGoodsMapper.selectByExample(example);
        return new PageInfo<>(all);
    }

    @Override
    public void updateStatus(Long[] ids, String status) {
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setStatus(status);
        Example example = new Example(SeckillGoods.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        seckillGoodsMapper.updateByExampleSelective(seckillGoods, example);
    }

    @Override
    public List<SeckillGoods> findSeckillGoods(Long[] ids) {
        Example example = new Example(SeckillGoods.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        criteria.andEqualTo("status", SysConstants.GOODS_STATUS_PASS_REVIEW);
        return seckillGoodsMapper.selectByExample(example);
    }

    @Override
    public void genItemHtml(SeckillGoods seckillGoods) throws IOException {
        genHTML(seckillGoods);
    }

    @Override
    public void deleteById(Long[] ids) {
        try {
            for (Long seckillGoodsId : ids) {
                FileUtils.forceDelete(new File(pageDir + seckillGoodsId + ".html"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void genHTML(SeckillGoods seckillGoods) throws IOException {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            //1.创建一个configuration对象
            //2.设置字符编码 和 模板加载的目录
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            //3.获取模板对象
            Template template = configuration.getTemplate("item.ftl");
            HashMap<String, Object> model = new HashMap<>(1);
            model.put("seckillGoods", seckillGoods);
            fos = new FileOutputStream(new File(pageDir + seckillGoods.getId() + ".html"));
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
    @Override
    public Map getGoodsById(Long id) {
        SeckillGoods tbSeckillGoods = (SeckillGoods) redisTemplate.boundHashOps(SysConstants.SEC_KILL_GOODS).get(id);
        Map map = new HashMap();
        if(tbSeckillGoods!=null ){

            //剩余时间毫秒数
            map.put("time",tbSeckillGoods.getEndTime().getTime()-System.currentTimeMillis());
            map.put("count",tbSeckillGoods.getStockCount());
            return map;
        }
        return map;
    }

}
