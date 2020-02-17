package com.springboot.flashsale.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.springboot.flashsale.pojo.FlashSaleOrder;
import com.springboot.flashsale.pojo.OrderInfo;

@Mapper
public interface OrderMapper {
	/**
	 * 通过用户id和商品id查询秒杀商品订单
	 * 
	 * @param userId
	 * @param goodsId
	 * @return
	 */
	public FlashSaleOrder getFlashSaleOrderByUserIdAndGoodsId(FlashSaleOrder flashSaleOrder);

	/**
	 * 插入订单
	 * @param orderInfo
	 * @return返回订单id
	 */
	public long insertOrderInfo(OrderInfo orderInfo);

	/**
	 * 插入秒杀订单
	 * @param flashSaleOrder
	 */
	public void insertFlashSaleOrder(FlashSaleOrder flashSaleOrder);

	/**
	 * 通过订单id获取订单
	 * @param orderId
	 * @return
	 */
	public OrderInfo getOrderByOrderId(long orderId);

}
