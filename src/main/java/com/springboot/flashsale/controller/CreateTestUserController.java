package com.springboot.flashsale.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.flashsale.pojo.User;
import com.springboot.flashsale.result.Result;
import com.springboot.flashsale.service.FlashSaleUserService;
import com.springboot.flashsale.util.CreateTestUserUtil;

@Controller
public class CreateTestUserController {
	
	@Autowired
	FlashSaleUserService flashSaleUserService;
	@Autowired
	CreateTestUserUtil createTestUser;
	
	@RequestMapping("/createTestUser")
	public String createTestUser() {
		return "createTestUser";
	}
	
	@RequestMapping("/doCreateTestUser")
	@ResponseBody
	public Result<Boolean> register(HttpServletRequest request) throws IOException {
		
		int createTestUserCount=Integer.parseInt(request.getParameter("createTestUserCount"));
		long time1 = System.currentTimeMillis();
		boolean b=false;
		List<User> userList=createTestUser.createTestUser(createTestUserCount);
		long time2 = System.currentTimeMillis();
		long total1=(time2-time1)/1000;
		
		for (User flashSaleUser : userList) {
			b=flashSaleUserService.register(flashSaleUser);
		}
		if(!b) {
			throw new RuntimeException("创建用户出错");
		}
		long time3 = System.currentTimeMillis();
		long total3=(time3-time1)/1000;
		
		createTestUser.createTestUserToken(userList);
		long time4 = System.currentTimeMillis();
		System.out.println("生成"+createTestUserCount+"个用户信息耗时"+total1+"秒");
		System.out.println(createTestUserCount+"个用户信息写入数据库耗时"+total3+"秒");
		System.out.println(createTestUserCount+"个用户信息写入数据库+生成登陆token+写入缓存+导出登陆信息表耗时"+((time4-time1)/1000)+"秒");
		return Result.success(true);
	}
}
