package com.springboot.flashsale.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.springboot.flashsale.pojo.User;

@Mapper
public interface UserMapper {
	/**
	 * 通过id取user
	 * @param id
	 * @return
	 */
	public User getById(long id);
	
	/**
	 * 注册
	 * @param flashSaleUser
	 */
	public void register(User flashSaleUser);
	
	/**
	 * 修改密码
	 * @param flashSaleUser
	 */
	public void updatePassword(User flashSaleUser);
}
