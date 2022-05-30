package com.iotinall.canteen.common.jsr303.validators;

import com.iotinall.canteen.common.jsr303.IdCard;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author xin-bing
 * @date 10/25/2019 15:31
 */
public class IdCardValidator implements ConstraintValidator<IdCard, String> {
    private IdCard.ValidGrade validGrade; // 校验等级
    private boolean required; // 是否必须

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(StringUtils.isBlank(value)) {
            return !required;
        }
        switch (validGrade) {
            case LOOSE:
                return looseValidate(value, context);
            case STRICT:
                return strictValidate(value, context);
        }
        return false;
    }

    @Override
    public void initialize(IdCard idCard) {
        validGrade = idCard.grade();
        required = idCard.required();
    }

    private static final Pattern LOOSE_PATTERN18 = Pattern.compile("^[1-9]\\d{5}(19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9xX]$");
    private static final Pattern LOOSE_PATTERN15 = Pattern.compile("^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$");
    private boolean looseValidate(String value, ConstraintValidatorContext context) {
        if(value.length() == 18) {
            return LOOSE_PATTERN18.matcher(value).matches();
        } else if (value.length() == 15) {
            return LOOSE_PATTERN15.matcher(value).matches();
        }
        return false;
    }

    private boolean strictValidate(String value, ConstraintValidatorContext context) {
        boolean flag = looseValidate(value, context);
        if(!flag) {
            return flag;
        }
        // do restrict operation
        return true;
    }
}
