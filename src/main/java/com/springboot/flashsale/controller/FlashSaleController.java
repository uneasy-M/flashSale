package com.springboot.flashsale.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.flashsale.pojo.FlashSaleOrder;
import com.springboot.flashsale.pojo.User;
import com.springboot.flashsale.rabbitmq.FlashSaleMessage;
import com.springboot.flashsale.rabbitmq.MQSender;
import com.springboot.flashsale.redis.RedisUtil;
import com.springboot.flashsale.redis.key.GoodsKey;
import com.springboot.flashsale.result.CodeMessage;
import com.springboot.flashsale.result.Result;
import com.springboot.flashsale.service.FlashSaleService;
import com.springboot.flashsale.service.FlashSaleUserService;
import com.springboot.flashsale.service.GoodsService;
import com.springboot.flashsale.service.OrderService;
import com.springboot.flashsale.vo.GoodsVo;

@Controller
public class FlashSaleController implements InitializingBean{
	
	private Map<Long,Boolean> localOverMap = new HashMap<>();
	
	@Autowired
	FlashSaleUserService flashSaleUserService;

	@Autowired
	GoodsService goodsService;

	@Autowired
	OrderService orderService;

	@Autowired
	FlashSaleService flashSaleService;
	
	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	MQSender mQSender;
	
	/**
	 * 系统初始化时加载商品数量到缓存
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		List<GoodsVo> goodsList =  goodsService.listGoodsVo();
		if(goodsList==null) {
			return;
		}
		for (GoodsVo good : goodsList) {
			redisUtil.set(GoodsKey.getFlashSaleGoodsStock, good.getId()+"", good.getStock_count());
			//内存标记localOverMap作为为系统访问标记，初始化本地系统标记为false，表示有库存
			localOverMap.put(good.getId(), false);
		}
	}
	
	/**
	 * get,post的区别
	 * get幂等
	 */
	@RequestMapping(value="/do_flashSale",method=RequestMethod.POST)
	@ResponseBody
	//goodsId在数据库找不到时出现空指针异常
	public Result<Integer> doFlashSale(Model model, User flashSaleUser, @RequestParam("goodsId") long goodsId) {
		if (flashSaleUser == null) {
			return Result.error(CodeMessage.SESSION_ERROR);
		}
		
		//如果没有了直接返回失败，不再继续往下走后面的操作了减少了Redis以及其他的操作
		//内存标记localOverMap作为为系统访问标记，如果本地系统标记为true了(即库存为空)就返回失败
		boolean isOver = localOverMap.get(goodsId);
		if(isOver) {
			return Result.error(CodeMessage.FLSAH_SALE_OVER);
		}
		
		//预减库存
		long stock = redisUtil.decr(GoodsKey.getFlashSaleGoodsStock, goodsId+"",1);
		if(stock<0) {
			//内存标记localOverMap作为为系统访问标记，如果本地系统标记为true了(即库存为空)就返回失败
			localOverMap.put(goodsId, true);
			return Result.error(CodeMessage.FLSAH_SALE_OVER);
		}
		
		// 判断用户是否秒杀到了
		long userId = flashSaleUser.getId();
		FlashSaleOrder order = new FlashSaleOrder();
		order.setUser_id(userId);
		order.setGoods_id(goodsId);
		FlashSaleOrder flashSaleOrder = orderService.getFlashSaleOrderByUserIdAndGoodsId(order);
		if (flashSaleOrder != null) {
			return Result.error(CodeMessage.FLSAH_SALE_REPEATW);
		}
		//入队
		FlashSaleMessage flashSaleMessage = new FlashSaleMessage();
		flashSaleMessage.setGoodsId(goodsId);
		flashSaleMessage.setUser(flashSaleUser);
		//消息队列之后再真正的减库存,下订单,写入秒杀订单
		mQSender.sendFlashSaleMessage(flashSaleMessage);
		
		//返回0代表排队中
		return Result.success(0);
	}
	
	//轮询结果
	/**
	 * 秒杀成功就返回订单id
	 * 失败返回-1
	 * 返回0排队中
	 * @param model
	 * @param flashSaleUser
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value="/flashSaleResult",method=RequestMethod.GET)
	@ResponseBody
	//goodsId在数据库找不到时出现空指针异常
	public Result<Long> flashSaleResult(Model model, User flashSaleUser, @RequestParam("goodsId") long goodsId) {
		if (flashSaleUser == null) {
			return Result.error(CodeMessage.SESSION_ERROR);
		}
		//不能直接传两个参数，只能传对象，所以通过FlashSaleOrder来封装userId,goodsId
		FlashSaleOrder order = new FlashSaleOrder();
		order.setUser_id(flashSaleUser.getId());
		order.setGoods_id(goodsId);
		
		long result = flashSaleService.getFlashSaleResult(order);
		
		return Result.success(result);
	}
}
