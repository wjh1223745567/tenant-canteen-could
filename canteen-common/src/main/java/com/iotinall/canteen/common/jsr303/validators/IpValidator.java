package com.iotinall.canteen.common.jsr303.validators;

import com.iotinall.canteen.common.jsr303.Ip;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 正则表达式校验
 * time: 5/16/2019
 *
 * @author xin-bing
 */
public class IpValidator implements ConstraintValidator<Ip, String> {
    private static final Pattern IPV4_PATTERN = Pattern.compile("^(([1-9]?\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.){3}([1-9]?\\d|1\\d{2}|2[0-4]\\d|25[0-5])$");
    private static final Pattern IPV6_PATTERN = Pattern.compile("^(([\\da-fA-F]{1,4}):){8}$");
    private Ip.IpType ipType;
    @Override
    public void initialize(Ip constraintAnnotation) {
        ipType = constraintAnnotation.ipType();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null || value.isEmpty()) {
            return true;
        }
        if(ipType == Ip.IpType.IPV4) {
            return IPV4_PATTERN.matcher(value).matches();
        } else if(ipType == Ip.IpType.IPV6) {
            return IPV6_PATTERN.matcher(value).matches();
        }
        return IPV4_PATTERN.matcher(value).matches() || IPV6_PATTERN.matcher(value).matches();
    }
}