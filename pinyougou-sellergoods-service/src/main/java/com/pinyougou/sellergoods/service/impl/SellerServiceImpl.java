package com.pinyougou.sellergoods.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import com.pinyougou.core.service.CoreServiceImpl;
import tk.mybatis.mapper.entity.Example;
import com.pinyougou.mapper.SellerMapper;
import com.pinyougou.pojo.Seller;
import com.pinyougou.sellergoods.service.SellerService;


/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class SellerServiceImpl extends CoreServiceImpl<Seller> implements SellerService {


    private SellerMapper sellerMapper;

    @Autowired
    public SellerServiceImpl(SellerMapper sellerMapper) {
        super(sellerMapper, Seller.class);
        this.sellerMapper = sellerMapper;
    }


    @Override
    public PageInfo<Seller> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<Seller> all = sellerMapper.selectAll();
        return new PageInfo<>(all);
    }


    @Override
    public PageInfo<Seller> findPage(Integer pageNo, Integer pageSize, Seller seller) {
        PageHelper.startPage(pageNo, pageSize);

        Example example = new Example(Seller.class);
        Example.Criteria criteria = example.createCriteria();

        if (seller != null) {
            if (StringUtils.isNotBlank(seller.getSellerId())) {
                criteria.andLike("sellerId", "%" + seller.getSellerId() + "%");
            }
            if (StringUtils.isNotBlank(seller.getName())) {
                criteria.andLike("name", "%" + seller.getName() + "%");
            }
            if (StringUtils.isNotBlank(seller.getNickName())) {
                criteria.andLike("nickName", "%" + seller.getNickName() + "%");
            }
            if (StringUtils.isNotBlank(seller.getPassword())) {
                criteria.andLike("password", "%" + seller.getPassword() + "%");
            }
            if (StringUtils.isNotBlank(seller.getEmail())) {
                criteria.andLike("email", "%" + seller.getEmail() + "%");
            }
            if (StringUtils.isNotBlank(seller.getMobile())) {
                criteria.andLike("mobile", "%" + seller.getMobile() + "%");
            }
            if (StringUtils.isNotBlank(seller.getTelephone())) {
                criteria.andLike("telephone", "%" + seller.getTelephone() + "%");
            }
            if (StringUtils.isNotBlank(seller.getStatus())) {
                criteria.andLike("status", "%" + seller.getStatus() + "%");
            }
            if (StringUtils.isNotBlank(seller.getAddressDetail())) {
                criteria.andLike("addressDetail", "%" + seller.getAddressDetail() + "%");
            }
            if (StringUtils.isNotBlank(seller.getLinkmanName())) {
                criteria.andLike("linkmanName", "%" + seller.getLinkmanName() + "%");
            }
            if (StringUtils.isNotBlank(seller.getLinkmanQq())) {
                criteria.andLike("linkmanQq", "%" + seller.getLinkmanQq() + "%");
            }
            if (StringUtils.isNotBlank(seller.getLinkmanMobile())) {
                criteria.andLike("linkmanMobile", "%" + seller.getLinkmanMobile() + "%");
            }
            if (StringUtils.isNotBlank(seller.getLinkmanEmail())) {
                criteria.andLike("linkmanEmail", "%" + seller.getLinkmanEmail() + "%");
            }
            if (StringUtils.isNotBlank(seller.getLicenseNumber())) {
                criteria.andLike("licenseNumber", "%" + seller.getLicenseNumber() + "%");
            }
            if (StringUtils.isNotBlank(seller.getTaxNumber())) {
                criteria.andLike("taxNumber", "%" + seller.getTaxNumber() + "%");
            }
            if (StringUtils.isNotBlank(seller.getOrgNumber())) {
                criteria.andLike("orgNumber", "%" + seller.getOrgNumber() + "%");
            }
            if (StringUtils.isNotBlank(seller.getLogoPic())) {
                criteria.andLike("logoPic", "%" + seller.getLogoPic() + "%");
            }
            if (StringUtils.isNotBlank(seller.getBrief())) {
                criteria.andLike("brief", "%" + seller.getBrief() + "%");
            }
            if (StringUtils.isNotBlank(seller.getLegalPerson())) {
                criteria.andLike("legalPerson", "%" + seller.getLegalPerson() + "%");
            }
            if (StringUtils.isNotBlank(seller.getLegalPersonCardId())) {
                criteria.andLike("legalPersonCardId", "%" + seller.getLegalPersonCardId() + "%");
            }
            if (StringUtils.isNotBlank(seller.getBankUser())) {
                criteria.andLike("bankUser", "%" + seller.getBankUser() + "%");
            }
            if (StringUtils.isNotBlank(seller.getBankName())) {
                criteria.andLike("bankName", "%" + seller.getBankName() + "%");
            }

        }
        List<Seller> all = sellerMapper.selectByExample(example);
        return new PageInfo<>(all);
    }

    @Override
    public void add(Seller record) {
        record.setStatus("0");
        record.setCreateTime(new Date());
        super.add(record);
    }
}
