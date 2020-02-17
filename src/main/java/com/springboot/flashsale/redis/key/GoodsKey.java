package com.springboot.flashsale.redis.key;

public class GoodsKey extends BasePrefix{

	public GoodsKey(String prefix) {
		super(prefix);
	}

	public static GoodsKey getGoodsList = new GoodsKey("goodsList");
	public static GoodsKey getGoodsDetail = new GoodsKey("goodsDetail");
	public static GoodsKey getFlashSaleGoodsStock = new GoodsKey("flashSaleGoodsStock");
}
