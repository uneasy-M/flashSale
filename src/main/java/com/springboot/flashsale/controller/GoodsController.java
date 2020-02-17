package com.springboot.flashsale.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.alibaba.druid.util.StringUtils;
import com.springboot.flashsale.pojo.User;
import com.springboot.flashsale.redis.RedisUtil;
import com.springboot.flashsale.redis.key.GoodsKey;
import com.springboot.flashsale.redis.key.KeyPrefix;
import com.springboot.flashsale.result.Result;
import com.springboot.flashsale.service.FlashSaleUserService;
import com.springboot.flashsale.service.GoodsService;
import com.springboot.flashsale.vo.GoodsDetailVo;
import com.springboot.flashsale.vo.GoodsVo;

@Controller
public class GoodsController {

	@Autowired
	FlashSaleUserService flashSaleUserService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	GoodsService goodsService;
	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;
	@Autowired
	ApplicationContext applicationContext;
	
	GoodsKey getGoodsListKey = GoodsKey.getGoodsList;
	GoodsKey getGoodsDetailKey = GoodsKey.getGoodsDetail;
	
	@RequestMapping(value="/goodsList",produces = "text/html")
	@ResponseBody
	public String goodsList(Model model, User flashSaleUser,HttpServletRequest request,HttpServletResponse response) {
		
		// 取缓存中的页面要在数据库查询之前,QPS可以从5000左右提升到8000左右
		//缓存的策略https://blog.csdn.net/tTU1EvLDeLFq5btqiK/article/details/78693323
		String html = (String) redisUtil.get(getGoodsListKey, "");
		if (!StringUtils.isEmpty(html)) {
			return html;
		}
		// 查询商品列表
		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		model.addAttribute("goodsList", goodsList);

		// 手动渲染
		SpringWebContext springWebContext = new SpringWebContext(request, response, request.getServletContext(),
				request.getLocale(), model.asMap(), applicationContext);
		html = thymeleafViewResolver.getTemplateEngine().process("goods_list", springWebContext);
		if (!StringUtils.isEmpty(html)) {
			redisUtil.set(getGoodsListKey, "", html, 60);
		}
		return html;
	}

	@RequestMapping("/goodsDetail/{goodsId}")
	@ResponseBody
	public Result<GoodsDetailVo> goodsDetail(Model model, User flashSaleUser, @PathVariable("goodsId") long goodsId,HttpServletRequest request,HttpServletResponse response) {
		// 查询商品
		GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);

		// 秒杀开始结束时间getTime()转化为毫秒
		long startDate = goodsVo.getStart_date().getTime();
		long endDate = goodsVo.getEnd_date().getTime();
		long now = System.currentTimeMillis();

		int flashSaleStatus = 0;
		long remainSeconds = 0;

		if (now < startDate) {// 秒杀未开始,倒计时
			flashSaleStatus = 0;
			remainSeconds = (startDate - now) / 1000;
		} else if (now > endDate) {// 秒杀结束,
			flashSaleStatus = 2;
			remainSeconds = -1;
		} else {// 秒杀正在进行
			flashSaleStatus = 1;
			remainSeconds = 0;
		}
		
		GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
		goodsDetailVo.setGoodsVo(goodsVo);
		goodsDetailVo.setUser(flashSaleUser);
		goodsDetailVo.setRemainSeconds(remainSeconds);
		goodsDetailVo.setFlashSaleStatus(flashSaleStatus);
	
		return Result.success(goodsDetailVo);
	}
	
	/**
	 * 
	 * @param pre 前缀
	 * @param key 键
	 * @param defaultTemplete 如果缓存没有则手动渲染的模板
	 * @param timeOut 有效时间
	 * @param request
	 * @param response 
	 * @param variables model.asMap()
	 * @param appctx ApplicationContext
	 * @return
	 */
	public String pageCache(KeyPrefix pre,String key,String defaultTemplete,long timeOut,HttpServletRequest request, HttpServletResponse response, Map<String, ?> variables, ApplicationContext appctx) {
		// 取缓存中的页面
		String html = (String) redisUtil.get(pre, key);
		if (!StringUtils.isEmpty(html)) {
			return html;
		}

		// 手动渲染
		SpringWebContext springWebContext = new SpringWebContext(request, response, request.getServletContext(),
				request.getLocale(), variables, appctx);
		html = thymeleafViewResolver.getTemplateEngine().process(defaultTemplete, springWebContext);
		if (!StringUtils.isEmpty(html)) {
			redisUtil.set(pre, key, html, timeOut);
		}
		return html;
	}

	/*
	 * //假如有多个业务代码,但是都需要到@CookieValue,@RequestParam,HttpServletResponse,
	 * flashSaleUser
	 * //重复太多所以把这部分代码放到了UserArgumentResolvers,详情看WebConfig,UserArgumentResolvers
	 * 
	 * @RequestMapping("/goodsList") public String goodsList( Model model,
	 * 
	 * @CookieValue(value=flashSaleUserService.KOOKIE_NAME_TOKEN,required=false)String
	 * cookieToken,
	 * 
	 * @RequestParam(value=flashSaleUserService.KOOKIE_NAME_TOKEN,required=false)String
	 * paramToken, HttpServletResponse response) {
	 * if(StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)) {
	 * return "login"; } String token =
	 * StringUtils.isEmpty(paramToken)?cookieToken:paramToken; FlashSaleUser
	 * flashSaleUser = flashSaleUserService.getByToken(token,response);
	 * model.addAttribute("flashSaleUser", flashSaleUser); return "goods_list"; }
	 */
}
