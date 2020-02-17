package com.springboot.flashsale.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

	public static final String FLASH_SALE_QUEUE = "flash_sale_queue";
	
	@Bean
	public Queue flashSaleQueue() {
		return new Queue(MQConfig.FLASH_SALE_QUEUE, true);
	}
}
