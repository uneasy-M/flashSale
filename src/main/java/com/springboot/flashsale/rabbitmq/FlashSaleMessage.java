package com.springboot.flashsale.rabbitmq;

import com.springboot.flashsale.pojo.User;

public class FlashSaleMessage {
	private User user;
	private long goodsId;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

}
