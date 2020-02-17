package com.springboot.flashsale.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
	public static String md5(String str) {
		return DigestUtils.md5Hex(str);
	}

	private static final String salt = "1a2b3c4d";

	// 把用户输入的密码转化为提交的密码
	public static String inputPassToFormPass(String inputPass) {
		String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
		return md5(str);
	}

	// 把form密码转化为提交到数据库的密码
	public static String formPassToDBPass(String formPass, String salt) {
		String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
		return md5(str);
	}
	
	//最终把用户输入的密码转化为提交到数据库的密码
	public static String inputPassToDBPass(String inputPass, String saltDB) {
		String formPass = inputPassToFormPass(inputPass);
		String dbPass = formPassToDBPass(formPass,saltDB);
		return dbPass;
	}
	
	public static void main(String[] args) {
		// 结果:d3b1294a61a07da9b49b6e22b2cbd7f9
		//System.out.println(inputPassToFormPass("123456"));
		//b7797cce01b4b131b433b6acf4add449
		//System.out.println(formPassToDBPass(inputPassToFormPass("123456"), "1a2b3c4d"));
		//b7797cce01b4b131b433b6acf4add449
		System.out.println(inputPassToDBPass("123456","1a2b3c4d"));
	}
}
