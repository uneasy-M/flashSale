package com.springboot.flashsale.util;

import java.util.UUID;

public class UUIDUtil {
	public static String uuid() {
		//由于生成的UUID有"-",所以要替换掉
		return UUID.randomUUID().toString().replace("-", "");
	}
}
