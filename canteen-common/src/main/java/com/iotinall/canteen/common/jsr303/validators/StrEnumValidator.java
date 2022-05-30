package com.iotinall.canteen.common.jsr303.validators;

import com.iotinall.canteen.common.jsr303.StrEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * str 枚举校验器
 * time: 4/23/2019
 *
 * @author xin-bing
 */
public class StrEnumValidator implements ConstraintValidator<StrEnum, String> {
    private Set<String> set;
    @Override
    public void initialize(StrEnum idValidator) {
        String[] values = idValidator.values();
        set = Stream.of(values).collect(HashSet::new, Set::add, Set::addAll);
    }

    @Override
    public boolean isValid(String text, ConstraintValidatorContext ctx) {
        return text == null || set.contains(text);
    }
}