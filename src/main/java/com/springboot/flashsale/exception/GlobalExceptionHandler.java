package com.springboot.flashsale.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.flashsale.result.CodeMessage;
import com.springboot.flashsale.result.Result;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
	@ExceptionHandler(value=Exception.class)
	public Result<String> defaultErrorHandler (HttpServletRequest req,Exception e) {
		e.printStackTrace();
		if(e instanceof GlobalException) {
			GlobalException ex = (GlobalException)e;
			//System.out.println("来自GlobalExceptionHandler:GlobalException--->:"+ex.getCodeMessage());
			return Result.error(ex.getCodeMessage());
		}else if(e instanceof BindException) {
			BindException ex = (BindException)e;
			List<ObjectError> errors = ex.getAllErrors();
			ObjectError error = errors.get(0);
			String msg = error.getDefaultMessage();
			//System.out.println("来自GlobalExceptionHandler:BindException--->:"+msg);
			return Result.error(CodeMessage.BIND_ERROR.fillArgs(msg));
		}else {
			//System.out.println("来自GlobalExceptionHandler:Exception--->:"+CodeMessage.SERVER_ERROR);
			return Result.error(CodeMessage.SERVER_ERROR);
		}
	}
	/*public ModelAndView defaultErrorHandler (HttpServletRequest req,Exception e) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        //mav.setViewName("errorPage");
        mav.setViewName("thymeleafError");
        return mav;
	}*/
}
