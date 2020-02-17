package com.springboot.flashsale.test;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.springboot.flashsale.Application;

import redis.clients.jedis.Jedis;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RedisTest {
//	private Jedis jedis;
//
//	@Test
//    public void test() {
//		jedis = new Jedis("192.168.2.156",6379);
//		System.out.println(jedis.flushAll());
//    }
	public static AtomicLong nextNumber = null;
	@Test
	public void testAtomicLong() {
		nextNumber = new AtomicLong(100);
		for(int i =0;i<100;i++) {
			System.out.println(nextNumber.decrementAndGet());
		}
	}
}
