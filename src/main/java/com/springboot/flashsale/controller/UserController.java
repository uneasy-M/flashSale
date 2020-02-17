package com.springboot.flashsale.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.flashsale.pojo.User;
import com.springboot.flashsale.result.Result;

@Controller
public class UserController {
	@RequestMapping("/userInfo")
	@ResponseBody
	public Result<User> userInfo(User flashSaleUser) {
		return Result.success(flashSaleUser);
		//return null;
	}
}
