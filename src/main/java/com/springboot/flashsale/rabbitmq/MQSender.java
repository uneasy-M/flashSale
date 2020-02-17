package com.springboot.flashsale.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {
	
	@Autowired
	AmqpTemplate amqpTemplate;
	
	/**
	 * 
	 * @param flashSaleMessage
	 */
	public void sendFlashSaleMessage(FlashSaleMessage flashSaleMessage) {
		String meg = StringAndBean.beanToString(flashSaleMessage);
		amqpTemplate.convertAndSend(MQConfig.FLASH_SALE_QUEUE,meg);
	}
	
}
