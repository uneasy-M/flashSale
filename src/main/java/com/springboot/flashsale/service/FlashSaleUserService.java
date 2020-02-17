package com.springboot.flashsale.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.springboot.flashsale.exception.GlobalException;
import com.springboot.flashsale.mapper.UserMapper;
import com.springboot.flashsale.pojo.User;
import com.springboot.flashsale.redis.RedisUtil;
import com.springboot.flashsale.redis.key.UserKey;
import com.springboot.flashsale.result.CodeMessage;
import com.springboot.flashsale.util.MD5Util;
import com.springboot.flashsale.util.UUIDUtil;
import com.springboot.flashsale.vo.LoginVo;

@Service
public class FlashSaleUserService {
	public static final String KOOKIE_NAME_TOKEN = "token";
	public static final int TOKEN_EXPIRE = 3600 * 24 * 2;
	UserKey tokenKey = UserKey.token;
	@Autowired
	UserMapper flashSaleUserMapper;

	@Autowired
	RedisUtil redisUtil;

	//从缓存或者数据库取对象
	public User getById(long id) {
		User user = (User) redisUtil.get(UserKey.getById, id+"");
		if(user!=null) {
			return user;
		}
		user = flashSaleUserMapper.getById(id);
		if(user!=null){
			redisUtil.set(UserKey.getById, id+"", user);
		}
		return user;
	}
	
	public boolean updatePassword(String token,long id,String oldPass,String newPass) {
		User user = getById(id);
		if(user==null) {
			throw new GlobalException(CodeMessage.USER_ERROR);
		}
		if(!oldPass.equals(user.getPassword())) {
			throw new GlobalException(CodeMessage.PASSWORD_ERROR);
		}
		//先更新数据库,再处理缓存
		//更新数据库
		//也可以在原来的User的基础上改,但是会传大量不相关数据
		User newUser = new User();
		newUser.setId(id);
		newUser.setPassword(MD5Util.formPassToDBPass(newPass, user.getSalt()));
		flashSaleUserMapper.updatePassword(newUser);
		//同时更新缓存
		//删除缓存的对象
		redisUtil.del(UserKey.getById,""+id);
		//更新session
		user.setPassword(newUser.getPassword());
		redisUtil.set(UserKey.token, token, user);
		return true;
	}

	// 从缓存取值
	public User getByToken(String token, HttpServletResponse response) {
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		User flashSaleUser = (User) redisUtil.get(tokenKey, token);
		// 重新往缓存设置,就可以达到延长有效期
		if (flashSaleUser != null) {
			addCookie(flashSaleUser, token, response);
		}
		return flashSaleUser;
	}

	public String login(HttpServletResponse response, LoginVo loginVo) {
		if (loginVo == null) {
			// 出现业务异常直接往外抛,统一异常处理
			throw new GlobalException(CodeMessage.SERVER_ERROR);
			// return CodeMessage.SERVER_ERROR;
		}
		String mobile = loginVo.getMobile();
		// form提交经过一次MD5过来的密码
		String formPassassword = loginVo.getPassword();
		User flashSaleUser = getById(Long.parseLong(mobile));
		// 判断是否存在mobile
		if (flashSaleUser == null) {
			// return CodeMessage.MOBILE_NOT_EXIST;
			// 出现业务异常直接往外抛,统一异常处理
			throw new GlobalException(CodeMessage.MOBILE_NOT_EXIST);
		}
		// 验证密码
		String dbPassword = flashSaleUser.getPassword();
		String dbSalt = flashSaleUser.getSalt();
		String caculatePass = MD5Util.formPassToDBPass(formPassassword, dbSalt);
		if (!caculatePass.equals(dbPassword)) {
			// 出现业务异常直接往外抛,统一异常处理
			throw new GlobalException(CodeMessage.PASSWORD_ERROR);
			// return CodeMessage.PASSWORD_ERROR;
		}
		// 添加Cookie
		// 只生成一次uuid
		String token = UUIDUtil.uuid();
		addCookie(flashSaleUser, token, response);
		
		return token;
		// return CodeMessage.SUCCESS;
	}

	private void addCookie(User flashSaleUser, String token, HttpServletResponse response) {
		// 登陆成功后生成cookie
		// String token = UUIDUtil.uuid();会每次延长有效期都会生成一个新的uuid
		// System.out.println("FlashSaleService的token:" + token);
		// FlashSaleUserKey tokenKey = FlashSaleUserKey.key;
		// 最后会合成一个tokenKey+token作为key,get时使用get(tokenKey+token)
		redisUtil.set(tokenKey, token, flashSaleUser, TOKEN_EXPIRE);
		Cookie cookie = new Cookie(KOOKIE_NAME_TOKEN, token);
		cookie.setMaxAge(TOKEN_EXPIRE);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	public boolean register(User flashSaleUser) {
		flashSaleUserMapper.register(flashSaleUser);
		if(flashSaleUser!=null) {
			return true;
		}
		return false;
	}
	
	
	
	

}
