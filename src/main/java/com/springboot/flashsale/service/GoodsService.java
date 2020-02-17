package com.springboot.flashsale.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.flashsale.mapper.GoodsVoMapper;
import com.springboot.flashsale.pojo.FlashSaleGoods;
import com.springboot.flashsale.vo.GoodsVo;

@Service
public class GoodsService {
	@Autowired
	GoodsVoMapper goodsVoMapper;

	/**
	 * 获取商品列表
	 * 
	 * @return
	 */
	public List<GoodsVo> listGoodsVo() {
		return goodsVoMapper.listGoodsVo();
	}

	/**
	 * 通过id获取商品
	 * 
	 * @param goodsId
	 * @return
	 */
	public GoodsVo getGoodsVoByGoodsId(long goodsId) {
		return goodsVoMapper.getGoodsVoByGoodsId(goodsId);

	}

	// 减库存
	@Transactional
	public boolean reduceStock(GoodsVo goodsVo) {
		FlashSaleGoods flashSaleGoods = new FlashSaleGoods();
		flashSaleGoods.setGoods_id(goodsVo.getId());
		//通过返回受影响行数判断是否减库存成功
		//在sql链接上增加useAffectedRows=true,开启update返回受影响行数
		return goodsVoMapper.reduceStock(flashSaleGoods) > 0 ? true : false;
	}
}
