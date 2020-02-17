package com.springboot.flashsale.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.flashsale.pojo.User;
import com.springboot.flashsale.redis.RedisUtil;
import com.springboot.flashsale.redis.key.FlashSaleKey;
import com.springboot.flashsale.pojo.FlashSaleOrder;
import com.springboot.flashsale.pojo.OrderInfo;
import com.springboot.flashsale.vo.GoodsVo;

@Service
public class FlashSaleService {

	@Autowired
	GoodsService goodsService;
	@Autowired
	OrderService orderService;
	@Autowired
	RedisUtil redisUtil;
	
	// 减库存,下订单,写入秒杀订单要求原子操作,所以使用事务
	@Transactional
	public OrderInfo flashSale(User flashSaleUser, GoodsVo goodsVo) {
		// 减库存
		boolean b = goodsService.reduceStock(goodsVo);
		//在flash_sale_order表里添加唯一索引，
		// 增加order_info和flash_sale_order两个表的信息
		// 增加一个判断,防止订单超出预期
		// 通过返回受影响行数判断是否减库存成功
		if (b) {
			OrderInfo orderInfo = orderService.createOrder(flashSaleUser, goodsVo);
			return orderInfo;
		}
		// 标记商品库存不足
		setGoodsIsOver(goodsVo.getId());
		return null;
	}

	/**
	 * 秒杀成功就返回订单id
	 * 失败返回-1
	 * 返回0排队中
	 * @param flashSaleOrder
	 * @return
	 */
	public long getFlashSaleResult(FlashSaleOrder flashSaleOrder) {
		FlashSaleOrder order = orderService.getFlashSaleOrderByUserIdAndGoodsId(flashSaleOrder);
		if (order != null) {
			return order.getOrder_id();
		} else {
			// 失败原因，有可能因为库存不足，也有可能因为有库存，抢到了但是没生成订单
			boolean isOver = getGoodsIsOver(flashSaleOrder.getGoods_id());
			if (isOver) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	private void setGoodsIsOver(Long goodsId) {
		redisUtil.set(FlashSaleKey.getGoodsStockIsOver, goodsId+"", true);
	}

	private boolean getGoodsIsOver(Long goodsId) {
		return redisUtil.hasKey(FlashSaleKey.getGoodsStockIsOver, goodsId+"");
	}

}
