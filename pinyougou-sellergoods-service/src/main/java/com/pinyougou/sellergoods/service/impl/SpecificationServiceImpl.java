package com.pinyougou.sellergoods.service.impl;

import java.util.List;

import com.pinyougou.entity.SpecificationOptions;
import com.pinyougou.mapper.SpecificationMapper;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.pojo.Specification;
import com.pinyougou.pojo.SpecificationOption;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import com.pinyougou.core.service.CoreServiceImpl;
import tk.mybatis.mapper.entity.Example;
import com.pinyougou.sellergoods.service.SpecificationService;


/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class SpecificationServiceImpl extends CoreServiceImpl<Specification> implements SpecificationService {


    private SpecificationMapper specificationMapper;
    private SpecificationOptionMapper specificationOptionMapper;

    @Autowired
    public SpecificationServiceImpl(SpecificationMapper specificationMapper, SpecificationOptionMapper specificationOptionMapper) {
        super(specificationMapper, Specification.class);
        this.specificationMapper = specificationMapper;
        this.specificationOptionMapper = specificationOptionMapper;
    }

    @Override
    public PageInfo<Specification> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<Specification> all = specificationMapper.selectAll();
        return new PageInfo<>(all);
    }

    @Override
    public PageInfo<Specification> findPage(Integer pageNo, Integer pageSize, Specification specification) {
        PageHelper.startPage(pageNo, pageSize);

        Example example = new Example(Specification.class);
        Example.Criteria criteria = example.createCriteria();

        if (specification != null) {
            if (StringUtils.isNotBlank(specification.getSpecName())) {
                criteria.andLike("specName", "%" + specification.getSpecName() + "%");
            }

        }
        List<Specification> all = specificationMapper.selectByExample(example);
        return new PageInfo<>(all);
    }

    @Override
    public SpecificationOptions findSpecificationOptions(Long id) {
        Specification specification = specificationMapper.selectByPrimaryKey(id);
        Example example = new Example(SpecificationOption.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("specId", id);
        List<SpecificationOption> optionList = specificationOptionMapper.selectByExample(example);
        return new SpecificationOptions(specification, optionList);
    }

    @Override
    public void updateSpecificationOptions(SpecificationOptions specificationOptions) {
        Specification specification = specificationOptions.getSpecification();
        List<SpecificationOption> optionList = specificationOptions.getOptionList();
        //更新specification
        specificationMapper.updateByPrimaryKey(specification);
        //删除optionList
        Example example = new Example(SpecificationOption.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("specId", specification.getId());
        specificationOptionMapper.deleteByExample(example);
        //添加optionList
        for (SpecificationOption option : optionList) {
            specificationOptionMapper.insert(option);
        }
    }

    @Override
    public void addSpecificationOptions(SpecificationOptions specificationOptions) {
        Specification specification = specificationOptions.getSpecification();
        List<SpecificationOption> optionList = specificationOptions.getOptionList();
        //添加specification,主键自动返回
        specificationMapper.insert(specification);
        //添加SpecificationOption
        for (SpecificationOption option : optionList) {
            option.setSpecId(specification.getId());
            specificationOptionMapper.insert(option);
        }


    }
}
