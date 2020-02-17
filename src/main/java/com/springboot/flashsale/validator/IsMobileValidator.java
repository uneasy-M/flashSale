package com.springboot.flashsale.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.springboot.flashsale.util.ValidatorUtil;

/**
 * 对@IsMobile注解的实现
 * @author 孤独患者ME
 *
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile,String>{
	
	//true代表不能为空,false代表可以为空
	private boolean required = false;
	
	
	@Override
	public void initialize(IsMobile constraintAnnotation) {
		//@IsMobile的required
		required = constraintAnnotation.required();
	}

	//IsMobile注解的实现
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(required) {
			return ValidatorUtil.isMobile(value);
		}else {
			if(StringUtils.isEmpty(value)) {
				return true;
			}else {
				return ValidatorUtil.isMobile(value);
			}
		}
	}
	
}
