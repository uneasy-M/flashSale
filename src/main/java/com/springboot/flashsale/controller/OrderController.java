package com.springboot.flashsale.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.flashsale.pojo.OrderInfo;
import com.springboot.flashsale.pojo.User;
import com.springboot.flashsale.result.CodeMessage;
import com.springboot.flashsale.result.Result;
import com.springboot.flashsale.service.GoodsService;
import com.springboot.flashsale.service.OrderService;
import com.springboot.flashsale.vo.GoodsVo;
import com.springboot.flashsale.vo.OrderDetailVo;

@Controller
public class OrderController {
	@Autowired
	OrderService orderService;
	@Autowired
	GoodsService goodsService;
	
	
	
	@RequestMapping("/orderDetail")
	@ResponseBody
	//可以自定义标签@NeedLogin,减免每次判断登陆
	public Result<OrderDetailVo> info(@RequestParam("orderId") long orderId,User user){
		if(user==null) {
			return Result.error(CodeMessage.SESSION_ERROR);
		}
		
		OrderInfo orderInfo = orderService.getOrderByOrderId(orderId);
		
		if(orderInfo==null) {
			return Result.error(CodeMessage.ORDER_NOT_EXIST);
		}
		
		long goodsId = orderInfo.getGoods_id();
		GoodsVo goodsVo =  goodsService.getGoodsVoByGoodsId(goodsId);
		
		OrderDetailVo orderDetailVo = new OrderDetailVo();
		orderDetailVo.setGoodsVo(goodsVo);
		orderDetailVo.setOrderInfo(orderInfo);
		
		return Result.success(orderDetailVo);
	}
}
