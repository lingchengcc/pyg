package com.pinyougou.shop.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Seller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author silent
 * @version 1.0
 * @date 2019/6/28 10:42
 **/
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Reference
    private SellerService sellerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Seller seller = sellerService.findOne(username);
        User roleSeller = null;
        if (seller != null) {
            if (!"1".equals(seller.getStatus())) {
                return null;
            }
            roleSeller = new User(username, seller.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_SELLER"));
        }
        return roleSeller;
    }
}
