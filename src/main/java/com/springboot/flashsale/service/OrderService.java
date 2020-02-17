package com.springboot.flashsale.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.flashsale.mapper.OrderMapper;
import com.springboot.flashsale.pojo.FlashSaleOrder;
import com.springboot.flashsale.pojo.User;
import com.springboot.flashsale.pojo.OrderInfo;
import com.springboot.flashsale.vo.GoodsVo;

@Service
public class OrderService {

	@Autowired
	OrderMapper flashSaleOrderMapper;

	public FlashSaleOrder getFlashSaleOrderByUserIdAndGoodsId(FlashSaleOrder flashSaleOrder) {
		return flashSaleOrderMapper.getFlashSaleOrderByUserIdAndGoodsId(flashSaleOrder);
	}
	
	// 减库存,下订单,写入秒杀订单要求原子操作,所以使用事务
	@Transactional
	public OrderInfo createOrder(User flashSaleUser, GoodsVo goodsVo) {
		// 插入OrderInfo表
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreate_date(new Date());
		orderInfo.setDelivery_addr_id(222L);
		orderInfo.setGoods_count(1);
		orderInfo.setGoods_id(goodsVo.getId());
		orderInfo.setGoods_name(goodsVo.getGoods_name());
		orderInfo.setGoods_price(goodsVo.getFlash_sale_price());
		orderInfo.setOrder_channel(1);
		orderInfo.setStatus(0);
		orderInfo.setUser_id(flashSaleUser.getId());
		
		//注意这里返回的值并不是id
		flashSaleOrderMapper.insertOrderInfo(orderInfo);
		//返回的id已经被注入到pojo属性
		long orderId = orderInfo.getId();
		// 插入FlashSaleOrder表
		FlashSaleOrder flashSaleOrder = new FlashSaleOrder();
		flashSaleOrder.setGoods_id(goodsVo.getId());
		flashSaleOrder.setOrder_id(orderId);
		flashSaleOrder.setUser_id(flashSaleUser.getId());
		flashSaleOrderMapper.insertFlashSaleOrder(flashSaleOrder);
		return orderInfo;
	}

	/**
	 * 订单id查询订单
	 * @param orderId
	 * @return
	 */
	public OrderInfo getOrderByOrderId(long orderId) {
		return flashSaleOrderMapper.getOrderByOrderId(orderId);
	}


}
