package com.springboot.flashsale.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.flashsale.pojo.FlashSaleOrder;
import com.springboot.flashsale.pojo.User;
import com.springboot.flashsale.rabbitmq.MQConfig;
import com.springboot.flashsale.service.FlashSaleService;
import com.springboot.flashsale.service.GoodsService;
import com.springboot.flashsale.service.OrderService;
import com.springboot.flashsale.vo.GoodsVo;

@Service
public class MQReceiver {
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	FlashSaleService flashSaleService;
	
	/**
	 * 一对一接收消息
	 * @param message
	 */
	@RabbitListener(queues = MQConfig.FLASH_SALE_QUEUE)
	public void receiveDirect(String message) {
		FlashSaleMessage flashSaleMessage = StringAndBean.stringToBean(message, FlashSaleMessage.class);
		User flashSaleUser =flashSaleMessage.getUser();
		long goodsId = flashSaleMessage.getGoodsId();
		
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		int stockCount = goods.getStock_count();
		
		// 判断库存
		if (stockCount <= 0) {
			return;
		}
		
		// 判断用户是否秒杀到了
		long userId = flashSaleUser.getId();
		FlashSaleOrder order = new FlashSaleOrder();
		order.setUser_id(userId);
		order.setGoods_id(goodsId);
		FlashSaleOrder flashSaleOrder = orderService.getFlashSaleOrderByUserIdAndGoodsId(order);
		if (flashSaleOrder != null) {
			return;
		}
		
		// 减库存,下订单,写入秒杀订单
		flashSaleService.flashSale(flashSaleUser, goods);
	}
}
