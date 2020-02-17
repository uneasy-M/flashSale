package com.springboot.flashsale.vo;

import com.springboot.flashsale.pojo.User;

public class GoodsDetailVo {
	private User user;
	private int flashSaleStatus = 0;
	private long remainSeconds = 0;
	private GoodsVo goodsVo;
	public int getFlashSaleStatus() {
		return flashSaleStatus;
	}
	public void setFlashSaleStatus(int flashSaleStatus) {
		this.flashSaleStatus = flashSaleStatus;
	}
	public long getRemainSeconds() {
		return remainSeconds;
	}
	public void setRemainSeconds(long remainSeconds) {
		this.remainSeconds = remainSeconds;
	}
	public GoodsVo getGoodsVo() {
		return goodsVo;
	}
	public void setGoodsVo(GoodsVo goodsVo) {
		this.goodsVo = goodsVo;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
