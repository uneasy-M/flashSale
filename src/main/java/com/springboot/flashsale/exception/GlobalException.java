package com.springboot.flashsale.exception;

import com.springboot.flashsale.result.CodeMessage;
/**
 * 业务异常类
 * @author 孤独患者ME
 *
 */
public class GlobalException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private CodeMessage codeMessage;
	
	public GlobalException(CodeMessage codeMessage) {
		super(codeMessage.toString());
		this.codeMessage = codeMessage;
	}
	public CodeMessage getCodeMessage() {
		return codeMessage;
	}

	
}
