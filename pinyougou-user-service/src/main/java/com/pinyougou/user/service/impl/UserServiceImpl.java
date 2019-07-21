package com.pinyougou.user.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.pinyougou.user.service.UserService;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import com.pinyougou.core.service.CoreServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;
import com.pinyougou.mapper.UserMapper;
import com.pinyougou.pojo.User;


/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class UserServiceImpl extends CoreServiceImpl<User> implements UserService {

    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DefaultMQProducer producer;
    @Value("${template_code}")
    private String templateCode;
    @Value("${sign_name}")
    private String signName;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        super(userMapper, User.class);
        this.userMapper = userMapper;
    }

    @Override
    public PageInfo<User> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<User> all = userMapper.selectAll();
        return new PageInfo<>(all);
    }

    @Override
    public PageInfo<User> findPage(Integer pageNo, Integer pageSize, User user) {
        PageHelper.startPage(pageNo, pageSize);

        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();

        if (user != null) {
            if (StringUtils.isNotBlank(user.getUsername())) {
                criteria.andLike("username", "%" + user.getUsername() + "%");
            }
            if (StringUtils.isNotBlank(user.getPassword())) {
                criteria.andLike("password", "%" + user.getPassword() + "%");
            }
            if (StringUtils.isNotBlank(user.getPhone())) {
                criteria.andLike("phone", "%" + user.getPhone() + "%");
            }
            if (StringUtils.isNotBlank(user.getEmail())) {
                criteria.andLike("email", "%" + user.getEmail() + "%");
            }
            if (StringUtils.isNotBlank(user.getSourceType())) {
                criteria.andLike("sourceType", "%" + user.getSourceType() + "%");
            }
            if (StringUtils.isNotBlank(user.getNickName())) {
                criteria.andLike("nickName", "%" + user.getNickName() + "%");
            }
            if (StringUtils.isNotBlank(user.getName())) {
                criteria.andLike("name", "%" + user.getName() + "%");
            }
            if (StringUtils.isNotBlank(user.getStatus())) {
                criteria.andLike("status", "%" + user.getStatus() + "%");
            }
            if (StringUtils.isNotBlank(user.getHeadPic())) {
                criteria.andLike("headPic", "%" + user.getHeadPic() + "%");
            }
            if (StringUtils.isNotBlank(user.getQq())) {
                criteria.andLike("qq", "%" + user.getQq() + "%");
            }
            if (StringUtils.isNotBlank(user.getIsMobileCheck())) {
                criteria.andLike("isMobileCheck", "%" + user.getIsMobileCheck() + "%");
            }
            if (StringUtils.isNotBlank(user.getIsEmailCheck())) {
                criteria.andLike("isEmailCheck", "%" + user.getIsEmailCheck() + "%");
            }
            if (StringUtils.isNotBlank(user.getSex())) {
                criteria.andLike("sex", "%" + user.getSex() + "%");
            }
        }
        List<User> all = userMapper.selectByExample(example);
        return new PageInfo<>(all);
    }

    @Override
    public void add(User record) {
        final Date date = new Date();
        record.setCreated(date);
        record.setUpdated(date);
        record.setPassword(DigestUtils.md5DigestAsHex(record.getPassword().getBytes()));
        userMapper.insert(record);
    }

    @Override
    public boolean checkSmsCode(String phone, String code) {
        //得到缓存中存储的验证码
        String sysCode = (String) redisTemplate.boundHashOps("SMS_CODE").get(phone);
        if (sysCode == null) {
            return false;
        }
        return sysCode.equals(code);
    }

    @Override
    public void createSmsCode(String phone) {

        String code;
        try {
            code = (long) (Math.random() * 9 + 1) * 100000 + "";
            redisTemplate.boundHashOps("SMS_CODE").put(phone, code);
            HashMap<String, String> hashMap = new HashMap<>(4);
            hashMap.put("mobile", phone);
            hashMap.put("sign_name", signName);
            hashMap.put("template_code", templateCode);
            hashMap.put("param", "{\"code\":\"" + code + "\"}");
            Message message = new Message("SMS_TOPIC", "SEND_MESSAGE_TAG", "createSmsCode", JSON.toJSONString(hashMap).getBytes());
            producer.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
