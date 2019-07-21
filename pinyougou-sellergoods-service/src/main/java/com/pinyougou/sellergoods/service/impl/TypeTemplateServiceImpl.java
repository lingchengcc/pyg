package com.pinyougou.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.pinyougou.common.entity.SysConstants;
import com.pinyougou.mapper.BrandImgMapper;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.mapper.TypeTemplateMapper;
import com.pinyougou.pojo.BrandImg;
import com.pinyougou.pojo.ItemCat;
import com.pinyougou.pojo.SpecificationOption;
import com.pinyougou.pojo.TypeTemplate;
import com.pinyougou.sellergoods.service.BrandImgService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import com.pinyougou.core.service.CoreServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;
import com.pinyougou.sellergoods.service.TypeTemplateService;


/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class TypeTemplateServiceImpl extends CoreServiceImpl<TypeTemplate> implements TypeTemplateService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private BrandImgMapper brandImgMapper;

    @Autowired
    private SpecificationOptionMapper optionMapper;

    private TypeTemplateMapper typeTemplateMapper;

    @Autowired
    public TypeTemplateServiceImpl(TypeTemplateMapper typeTemplateMapper) {
        super(typeTemplateMapper, TypeTemplate.class);
        this.typeTemplateMapper = typeTemplateMapper;
    }

    @Override
    public PageInfo<TypeTemplate> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<TypeTemplate> all = typeTemplateMapper.selectAll();
        return new PageInfo<>(all);
    }

    @Override
    public void add(TypeTemplate record) {
        super.add(record);
        removeTypeFromRedis();
    }

    @Override
    public void delete(Object[] ids) {
        super.delete(ids);
        removeTypeFromRedis();
    }

    @Override
    public void update(TypeTemplate record) {
        super.update(record);
        removeTypeFromRedis();
    }

    @Override
    public PageInfo<TypeTemplate> findPage(Integer pageNo, Integer pageSize, TypeTemplate typeTemplate) {
        pushTypeToRedis();
        PageHelper.startPage(pageNo, pageSize);
        Example example = new Example(TypeTemplate.class);
        Example.Criteria criteria = example.createCriteria();

        if (typeTemplate != null) {
            if (StringUtils.isNotBlank(typeTemplate.getName())) {
                criteria.andLike("name", "%" + typeTemplate.getName() + "%");
            }
            if (StringUtils.isNotBlank(typeTemplate.getSpecIds())) {
                criteria.andLike("specIds", "%" + typeTemplate.getSpecIds() + "%");
            }
            if (StringUtils.isNotBlank(typeTemplate.getBrandIds())) {
                criteria.andLike("brandIds", "%" + typeTemplate.getBrandIds() + "%");
            }
            if (StringUtils.isNotBlank(typeTemplate.getCustomAttributeItems())) {
                criteria.andLike("customAttributeItems", "%" + typeTemplate.getCustomAttributeItems() + "%");
            }

        }
        List<TypeTemplate> all = typeTemplateMapper.selectByExample(example);
        return new PageInfo<>(all);
    }


    @Override
    public void pushTypeToRedis() {
        if (!redisTemplate.hasKey(SysConstants.BRAND_REDIS_KEY) && !redisTemplate.hasKey(SysConstants.SPECIFICATION_REDIS_KEY)) {
            final List<TypeTemplate> templates = findAll();
            for (TypeTemplate template : templates) {
                //存储品牌列表
                List<Map> brandList = JSON.parseArray(template.getBrandIds(), Map.class);
                //把品牌名称替换成品牌图片地址
                for (Map brand : brandList) {
                    int id = (Integer) brand.get("id");
                    Long ids = (long) id;
                    BrandImg brandImg = new BrandImg();
                    brandImg.setBrandId(ids);
                    List<BrandImg> brandImgs = brandImgMapper.select(brandImg);
                    if (brandImgs != null && brandImgs.size() != 0) {
                        BrandImg img = brandImgs.get(0);
                        brand.put("pic", img.getPic());
                    }
                }
                redisTemplate.boundHashOps(SysConstants.BRAND_REDIS_KEY).put(template.getId(), brandList);
                //存储规格列表,根据模板ID查询规格列表
                List<Map> specList = findSpecList(template.getId());
                redisTemplate.boundHashOps(SysConstants.SPECIFICATION_REDIS_KEY).put(template.getId(), specList);
            }
        }
    }

    @Override
    public void removeTypeFromRedis() {
        if (redisTemplate.hasKey(SysConstants.BRAND_REDIS_KEY)) {
            redisTemplate.delete(SysConstants.BRAND_REDIS_KEY);
        }
        if (redisTemplate.hasKey(SysConstants.SPECIFICATION_REDIS_KEY)) {
            redisTemplate.delete(SysConstants.SPECIFICATION_REDIS_KEY);
        }
        pushTypeToRedis();
    }

    @Override
    public List<Map> findSpecList(Long typeTemplateId) {

        //1.根据主键 查询模板的对象
        TypeTemplate typeTemplate = typeTemplateMapper.selectByPrimaryKey(typeTemplateId);
        //2.获取模板的对象中的规格列表String [{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        String specIds = typeTemplate.getSpecIds();
        //3.将字符串转成JSON对象数组
        List<Map> maps = JSON.parseArray(specIds, Map.class);
        for (Map map : maps) {
            //{"id":27,"text":"网络"}
            //4.循环遍历件SON数组 根据规格的ID 获取规格的下的所有的选项列表
            Integer id = (Integer) map.get("id");
            //select * from option where spec_id=id
            SpecificationOption condition = new SpecificationOption();
            condition.setSpecId(Long.valueOf(id));
            //[{optionName:'移动3G'},{optionName:'移动4G'}]
            List<SpecificationOption> optionList = optionMapper.select(condition);
            //5.拼接成：[{"id":27,"text":"网络",optionsList:[{optionName:'移动3G'},{optionName:'移动4G'}]},{"id":32,"text":"机身内存"}]
            map.put("optionList", optionList);
        }
        return maps;
    }
}
