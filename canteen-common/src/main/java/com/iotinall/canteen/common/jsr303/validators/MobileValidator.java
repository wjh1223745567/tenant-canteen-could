package com.iotinall.canteen.common.jsr303.validators;

import com.iotinall.canteen.common.jsr303.Mobile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * description: 手机号码校验器
 * time: 4/27/2019 14:37
 * @author xin-bing
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {
 
    private boolean required=false;
    //国内手机号码格式校验器
    private static final Pattern CHINESE_MOBILE_PATTERN = Pattern.compile("^[1](([3][0-9])|([4][579])|([5][0-9])|([6][6])|([7][35678])|([8][0-9])|([9][89]))[0-9]{8}$");

    @Override
    public void initialize(Mobile constraintAnnotation) {
        required=constraintAnnotation.required();
    }
 
    //判断是否合法
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required) { // 如果必填，那么判断手机号是否为空，为空则返回false
            if (value == null || value.isEmpty()) {
                return false;
            }
        }
        if(value != null) { //不为空则校验是否matches手机格式
            return CHINESE_MOBILE_PATTERN.matcher(value).matches();
        }
        return true;
    }
}