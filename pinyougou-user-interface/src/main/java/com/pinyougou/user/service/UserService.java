package com.pinyougou.user.service;

import com.pinyougou.pojo.User;
import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.CoreService;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface UserService extends CoreService<User> {

	/**
	 * 返回分页列表
	 * @param pageNo 当前页码
	 * @param pageSize 每页记录数
	 * @return User集合
	 */
	 PageInfo<User> findPage(Integer pageNo, Integer pageSize);

	/**
	 * 分页
	 * @param pageNo 当前页码
	 * @param pageSize 每页记录数
	 * @param user User
	 * @return User集合
	 */
	PageInfo<User> findPage(Integer pageNo, Integer pageSize, User user);

	boolean checkSmsCode(String phone, String code);

	void createSmsCode(String phone);
}
