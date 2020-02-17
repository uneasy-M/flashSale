package com.springboot.flashsale.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.springboot.flashsale.pojo.User;
import com.springboot.flashsale.service.FlashSaleUserService;

@Service
public class UserArgumentResolvers implements HandlerMethodArgumentResolver {
	@Autowired
	FlashSaleUserService flashSaleUserService;

	
	/**
	 * 用于判断是否是FlashSaleUser
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> clazz = parameter.getParameterType();
		return clazz == User.class;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		//获取HttpServletRequest,HttpServletResponse
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);

		//获取Parameter和Cookie中的token
		String paramToken = request.getParameter(FlashSaleUserService.KOOKIE_NAME_TOKEN);
		String cookieToken = getCookieVal(request, FlashSaleUserService.KOOKIE_NAME_TOKEN);
		//判断是否有值
		if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
		return flashSaleUserService.getByToken(token, response);
	}

	///遍历cookies,取到含有token的cookie
	private String getCookieVal(HttpServletRequest request, String kookieNameToken) {
		Cookie[] cookies = request.getCookies();
		//修复并发量高时出现空指针异常
		if(cookies==null||cookies.length<=0) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(kookieNameToken)) {
				return cookie.getValue();
			}
		}
		return null;
	}

}
