package com.springboot.flashsale.result;

public class Result<T> {
	private int code;
	private String msg;
	private T data;

	//成功时的构造
	private Result(T data) {
		this.code = 0;
		this.msg = "success";
		this.data = data;
	}

	//错误时的构造
	private Result(CodeMessage codeMessage) {
		if (codeMessage == null) {
			return;
		}
		this.code = codeMessage.getCode();
		this.msg = codeMessage.getMsg();
	}

	/**
	 *  成功时候的调用
	 * */
	public static <T> Result<T> success(T data) {
		return new Result<T>(data);
	}

	/**
	 *  失败时候的调用
	 * */
	public static <T> Result<T> error(CodeMessage codeMessage) {
		return new Result<T>(codeMessage);
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public T getData() {
		return data;
	}

}
