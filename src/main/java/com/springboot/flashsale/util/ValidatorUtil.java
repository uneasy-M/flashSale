package com.springboot.flashsale.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ValidatorUtil {
	//private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");
	private static final Pattern mobile_pattern = Pattern.compile("^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$");

	public static boolean isMobile(String src) {
		if (StringUtils.isEmpty(src)) {
			return false;
		}
		Matcher m = mobile_pattern.matcher(src);
		return m.matches();
	}
	
	//还可写一堆ValidatorUtil方法
	
}
