package com.springboot.flashsale.redis.key;

public class FlashSaleKey extends BasePrefix{

	public FlashSaleKey(String prefix) {
		super(prefix);
	}
	
	public static FlashSaleKey getGoodsStockIsOver = new FlashSaleKey("goodsStockFlag");

}
