package com.iotinall.canteen.common.jsr303.validators;

import com.iotinall.canteen.common.jsr303.Pwd;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author xin-bing
 * @date 10/17/2019 14:43
 */
public class PwdValidator implements ConstraintValidator<Pwd, String> {

    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9-_.*@]{5,15}");
    @Override
    public void initialize(Pwd constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return true;
        }
        return PATTERN.matcher(value).matches();
    }
}
