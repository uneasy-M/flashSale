package com.springboot.flashsale.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.flashsale.result.Result;
import com.springboot.flashsale.service.FlashSaleUserService;
import com.springboot.flashsale.vo.LoginVo;

@Controller
public class LoginController {
	@Autowired
	FlashSaleUserService flashSaleUserService;

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping(value="/do_Login",method=RequestMethod.POST )
	@ResponseBody
	public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
		
		// 数据校验,现在已经全部放到flashSaleService
//		String mobile = loginVo.getMobile();
//		String inputPass = loginVo.getPassword();
//		if (StringUtils.isEmpty(inputPass)) {
//			return Result.error(CodeMessage.PASSWORD_EMPTY);
//		}
//		//校验手机号码是否为空
//		if (StringUtils.isEmpty(mobile)) {
//			return Result.error(CodeMessage.MOBILE_EMPTY);
//		}
//		//校验手机号码格式
//		if (!ValidatorUtil.isMobile(mobile)) {
//			return Result.error(CodeMessage.MOBILE_ERROR);
//		}

		// 登陆
		// CodeMessage codeMessage = flashSaleService.login(loginVo);
		String token =flashSaleUserService.login(response,loginVo);
		/*
		 * if (codeMessage.getCode() == 0) { return Result.success(true); } else {
		 * Result.error(codeMessage); }
		 */
		return Result.success(token);
	}

	/*
	 * @RequestMapping("/do_Login")
	 * 
	 * @ResponseBody public Result<Boolean> doLogin(LoginVo loginVo) {
	 * log.info(loginVo.toString()); // 数据校验 String mobile = loginVo.getMobile();
	 * String inputPass = loginVo.getPassword(); if (StringUtils.isEmpty(inputPass))
	 * { return Result.error(CodeMessage.PASSWORD_EMPTY); } //校验手机号码是否为空 if
	 * (StringUtils.isEmpty(mobile)) { return
	 * Result.error(CodeMessage.MOBILE_EMPTY); } //校验手机号码格式 if
	 * (!ValidatorUtil.isMobile(mobile)) { return
	 * Result.error(CodeMessage.MOBILE_ERROR); }
	 * 
	 * // 登陆 CodeMessage codeMessage = flashSaleService.login(loginVo); if
	 * (codeMessage.getCode() == 0) { return Result.success(true); } else {
	 * Result.error(codeMessage); } return null; }
	 */
}
