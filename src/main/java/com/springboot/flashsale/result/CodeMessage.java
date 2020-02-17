package com.springboot.flashsale.result;


public class CodeMessage {
	private int code;
	private String msg;

	// 通用的错误码
	public static CodeMessage SUCCESS = new CodeMessage(0, "success");
	public static CodeMessage SERVER_ERROR = new CodeMessage(500100, "服务端异常");
	public static CodeMessage BIND_ERROR = new CodeMessage(500101, "参数校验异常：%s");
	// 登录模块 5002XX
	public static CodeMessage SESSION_ERROR = new CodeMessage(500210, "Session不存在或者已经失效");
	public static CodeMessage PASSWORD_EMPTY = new CodeMessage(500211, "登录密码不能为空");
	public static CodeMessage MOBILE_EMPTY = new CodeMessage(500212, "手机号不能为空");
	public static CodeMessage MOBILE_ERROR = new CodeMessage(500213, "手机号格式错误");
	public static CodeMessage MOBILE_NOT_EXIST = new CodeMessage(500214, "手机号不存在");
	public static CodeMessage PASSWORD_ERROR = new CodeMessage(500215, "密码错误");
	public static CodeMessage USER_ERROR = new CodeMessage(500216, "找不到用户");

	// 商品模块 5003XX

	// 订单模块 5004XX
	public static CodeMessage ORDER_NOT_EXIST = new CodeMessage(500400, "订单不存在");
	
	// 秒杀模块 5005XX
	public static CodeMessage FLSAH_SALE_OVER = new CodeMessage(500500, "商品库存不足");
	public static CodeMessage FLSAH_SALE_REPEATW = new CodeMessage(500501, "你已经参加过秒杀");
	
	//构造私有,只能使用定义好的错误代码
	private CodeMessage(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
	
	//
	public CodeMessage fillArgs(Object...args) {
		int code = this.code;
		String message = String.format(this.msg, args);
		return new CodeMessage(code,message);
	}
	

}
