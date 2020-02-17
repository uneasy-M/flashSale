package com.springboot.flashsale.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.springboot.flashsale.pojo.FlashSaleGoods;
import com.springboot.flashsale.vo.GoodsVo;
@Mapper
public interface GoodsVoMapper {
	/**
	 * 获取商品列表
	 * @return
	 */
	public List<GoodsVo> listGoodsVo();

	/**
	 * 通过id查询GoodsVo
	 * @param GoodsId
	 * @return
	 */
	public GoodsVo getGoodsVoByGoodsId(long goodsId);

	/**
	 * 减少库存
	 * @param goods
	 */
	public int reduceStock(FlashSaleGoods flashSaleGoods);
}
