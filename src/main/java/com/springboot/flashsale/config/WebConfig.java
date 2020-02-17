package com.springboot.flashsale.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
/**
 * 自定义注入FlashSaleUser
 * 需要继承WebMvcConfigurerAdapter
 * @author 孤独患者ME
 *
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	@Autowired 
	UserArgumentResolvers userArgumentResolvers;
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		//需要传入HandlerMethodArgumentResolver类型,所以新建UserArgumentResolvers并实现HandlerMethodArgumentResolver
		argumentResolvers.add(userArgumentResolvers);
	}
}
